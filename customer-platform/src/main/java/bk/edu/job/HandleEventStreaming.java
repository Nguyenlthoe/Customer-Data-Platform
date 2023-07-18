package bk.edu.job;

import bk.edu.config.Config;
import bk.edu.data.model.BookContext;
import bk.edu.data.model.EventKafka;
import bk.edu.data.model.MyEvent;
import bk.edu.utils.MySqlUtils;
import bk.edu.utils.SparkUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.ForeachPartitionFunction;
import org.apache.spark.api.java.function.MapPartitionsFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.catalyst.encoders.RowEncoder;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.jmx.export.naming.IdentityNamingStrategy;
import scala.Serializable;
import scala.Tuple2;
import static org.apache.spark.sql.functions.col;
import java.net.URL;
import java.sql.Timestamp;
import java.util.*;
import java.util.function.Consumer;

public class HandleEventStreaming {
    private StructType structType = new StructType(new StructField[]{
            new StructField("user_id", DataTypes.IntegerType, true, null)
    });

    private SparkUtils sparkUtils;

    public HandleEventStreaming(boolean log, String name) {
        this.sparkUtils = new SparkUtils("streaming: " + name, log, true);
    }

    public static void main(String[] args) {
        try {
            boolean log = false;
            if(args[0].equals("true")){
                log = true;
            }
            HandleEventStreaming matching = new HandleEventStreaming(log, args[2]);
            matching.run(args[1]); // streaming can throw exception
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static MyEvent transformRow(Row row) {
        String value = row.getAs(0);
        String[] propertyIndex = value.split("\t");
        String event_name = propertyIndex[126];
        String event_id = propertyIndex[6];
        MyEvent myEvent = new MyEvent();
        long collector_tstamp = Timestamp.valueOf(propertyIndex[3]).getTime();
        String user_id = propertyIndex[12];
        String domain_userid = propertyIndex[15];
        JSONObject unstruct_event = null;
        try {
             unstruct_event = new JSONObject(propertyIndex[58]);
        } catch (Exception e){

        }
        JSONObject context = new JSONObject(propertyIndex[52]);
        JSONArray data = context.getJSONArray("data");


        myEvent.setCollector_tstamp(collector_tstamp);
        myEvent.setUser_id(user_id);
        myEvent.setDomain_userid(domain_userid);
        myEvent.setEvent_name(event_name);
        myEvent.setEvent_id(event_id);
        try {
            myEvent.setEvent(unstruct_event.getJSONObject("data").getString("data"));
        } catch (Exception e) {
            myEvent.setEvent("page_view");
        }
        if (!myEvent.getEvent().toString().contains("purchase")
                && myEvent.getEvent_name().equals("product_action")) {
            for (int i = 0; i < data.length(); i++) {

                try {
                    JSONObject jsonObject = data.getJSONObject(i);
                    if (jsonObject.getString("schema").equals("iglu:com.bookshop/product_context/jsonschema/1-0-0")) {
                        JSONObject databook = jsonObject.getJSONObject("data");
                        myEvent.setBook(databook.getInt("product_id"), databook.getInt("category_id"),
                                databook.getInt("publisher_id"), databook.getInt("author_id"), databook.getInt("price"));
                        break;
                    }
                } catch (Exception ignore) {
                }
            }
            if(myEvent.getBook_id() == null){
                myEvent = null;
            }
        } else {
            myEvent = null;
        }

        return myEvent;
    }

    public void handleEventView(Dataset<Row> df){
        Dataset<Row> bookCustomerDf = df
                .filter(col("event").like("%view%"))
                .select("user_id", "book_id").distinct();
        Dataset<Row> customerUpdateDf = bookCustomerDf.mapPartitions((MapPartitionsFunction<Row, Row>) t -> {
            MySqlUtils mySqlUtils = new MySqlUtils();
            List<Row> rows = new ArrayList<>();
            while (t.hasNext()){
                Row row = t.next();
                try {
                    int userId = Integer.parseInt(row.getString(0));
                    int bookId = row.getInt(1);
                    if(!mySqlUtils.checkExistCustomerBook(userId, bookId)){
                        mySqlUtils.insertCustomerBook(userId, bookId);

                        rows.add(RowFactory.create(userId));
                        System.out.println(String.format("Insert new (userId, bookId): (%d, %d)",
                                userId, bookId));
                    }
                } catch (Exception ignore){
                }
            }
            mySqlUtils.close();
            return rows.iterator();
        }, RowEncoder.apply(structType));

        customerUpdateDf.foreachPartition((ForeachPartitionFunction<Row>) t -> {
            MySqlUtils mySqlUtils = new MySqlUtils();
            while (t.hasNext()){
                Row row = t.next();
                int userId = row.getInt(0);
                mySqlUtils.updateBookRead(userId);
            }
            mySqlUtils.close();
        });
        System.out.println("Number user view updated: " + customerUpdateDf.count());
    }

    public void handleUpdateShortHobbies(Dataset<Row> df){
        Dataset<Row> categoryCustomerDf = df.select("user_id", "category_id").distinct();
        categoryCustomerDf.foreachPartition((ForeachPartitionFunction<Row>) t -> {
            MySqlUtils mySqlUtils = new MySqlUtils();
            while (t.hasNext()){
                Row row = t.next();
                try {
                    int userId = Integer.parseInt(row.getString(0));
                    int categoryId = row.getInt(1);
                    if(!mySqlUtils.checkExistCustomerCategory(userId, categoryId)){
                        mySqlUtils.insertCustomerCategory(userId, categoryId);
                    } else {
                        mySqlUtils.updateCustomerCategory(userId, categoryId);
                    }
                } catch (Exception ignore){

                }
            }
            mySqlUtils.close();
        });
        Dataset<Row> customerDf = df.select("user_id").distinct();
        customerDf.foreachPartition((ForeachPartitionFunction<Row>) t -> {
            MySqlUtils mySqlUtils = new MySqlUtils();
            while (t.hasNext()){
                Row row = t.next();
                try {
                    int userId = Integer.parseInt(row.getString(0));
                    mySqlUtils.updateShortHobbies(userId);
                } catch (Exception ignore){

                }
            }
            mySqlUtils.close();
        });
        System.out.println("Number user short_hobbies has updated :" + customerDf.count());
    }
    public void run(String option) throws InterruptedException {
        JavaStreamingContext javaStreamingContext = sparkUtils.javaStreamingContext;
        JavaInputDStream<ConsumerRecord<Objects, Objects>> stream =
                KafkaUtils.createDirectStream(
                        sparkUtils.javaStreamingContext,
                        LocationStrategies.PreferConsistent(),
                        ConsumerStrategies.Subscribe(Config.KAFKA.TOPIC, Config.KAFKA.KAFKA_PARAM));
        stream.foreachRDD((consumerRecordJavaRDD, time) -> {
            JavaRDD<MyEvent> rows = consumerRecordJavaRDD
                    .map(consumerRecord -> RowFactory.create(consumerRecord.value(), consumerRecord.topic()))
                    .map(HandleEventStreaming::transformRow)
                    .filter(Objects::nonNull);
            Dataset<Row> df = sparkUtils.session.createDataFrame(rows, MyEvent.class);
            df.persist();
            if(option.equals("view")){
                handleEventView(df);
            } else if (option.equals("hobby")){
                handleUpdateShortHobbies(df);
            }
            df.show();
            df.unpersist();
        });

        javaStreamingContext.start();
        javaStreamingContext.awaitTermination();
    }
}
