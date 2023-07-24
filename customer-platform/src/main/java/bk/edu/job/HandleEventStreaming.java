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
import org.apache.spark.storage.StorageLevel;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
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
        myEvent.setEvent_id(event_id);
        try {
            myEvent.setEvent(unstruct_event.getJSONObject("data").getString("data"));
        } catch (Exception e) {
            myEvent.setEvent("page_view");
        }
        if (!myEvent.getEvent().toString().contains("purchase")) {
            for (int i = 0; i < data.length(); i++) {

                try {
                    JSONObject jsonObject = data.getJSONObject(i);
                    if (jsonObject.getString("schema").equals("iglu:com.bookshop/product_context/jsonschema/1-0-0")) {
                        JSONObject databook = jsonObject.getJSONObject("data");
                        myEvent.setBook(databook.getInt("product_id"), databook.getInt("category_id"),
                                databook.getInt("publisher_id"), databook.getInt("author_id"));
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

    public void handleEventView(Dataset<Row> df, Long timeStart){
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
                    }
                } catch (Exception ignore){
                    ignore.printStackTrace();
                }
            }
            mySqlUtils.close();
            return rows.iterator();
        }, RowEncoder.apply(structType));

        customerUpdateDf.persist(StorageLevel.MEMORY_ONLY());
        customerUpdateDf.foreachPartition((ForeachPartitionFunction<Row>) t -> {
            MySqlUtils mySqlUtils = new MySqlUtils();
            while (t.hasNext()){
                Row row = t.next();
                mySqlUtils.updateBookRead(row.getInt(0));
            }
            mySqlUtils.close();
        });
        System.out.println("Time process: " + (System.currentTimeMillis() - timeStart) / 1000 + "s");
        System.out.println("Number user view updated: " + customerUpdateDf.count());

        customerUpdateDf.unpersist();
    }

    public void handleUpdateShortHobbies(Dataset<Row> df, Long timeStart){
        Dataset<Row> categoryCustomerDf = df.select("user_id", "category_id").distinct().mapPartitions((MapPartitionsFunction<Row, Row>) t -> {
            List<Row> rows = new ArrayList<>();
            MySqlUtils mySqlUtils = new MySqlUtils();
            Connection connection = mySqlUtils.getConnection();
            PreparedStatement preparedExist = connection
                .prepareStatement("SELECT * FROM cdp_customer_category_association " +
                    "WHERE user_id = ? and category_id = ?");
            PreparedStatement preparedInsert = connection
                .prepareStatement("INSERT INTO `cdp_customer_category_association` " +
                    "(`user_id`, `category_id`, `updated_at`) VALUES ( ? , ? , ? );\n");
            PreparedStatement preparedUpdate = connection
                .prepareStatement("UPDATE `cdp_customer_category_association` " +
                    "SET `updated_at` = ? WHERE (`user_id` = ? ) and (`category_id` = ? );\n");

            while (t.hasNext()){
                Row row = t.next();
                try {
                    int userId = Integer.parseInt(row.getString(0));
                    int categoryId = row.getInt(1);
                    if(!mySqlUtils.checkExistCustomerCategory(userId, categoryId, preparedExist)){
                        mySqlUtils.insertCustomerCategory(userId, categoryId, preparedInsert);
                        rows.add(RowFactory.create(userId));
                    } else {
                        mySqlUtils.updateCustomerCategory(userId, categoryId, preparedUpdate);
                    }
                } catch (Exception ignore){

                }
            }
            mySqlUtils.close();
            return rows.iterator();
        }, RowEncoder.apply(structType));
        categoryCustomerDf.persist(StorageLevel.MEMORY_ONLY());
        categoryCustomerDf.distinct().foreachPartition((ForeachPartitionFunction<Row>) t -> {
            MySqlUtils mySqlUtils = new MySqlUtils();
            while (t.hasNext()){
                Row row = t.next();
                try {
                    mySqlUtils.updateShortHobbies(row.getInt(0));
                } catch (Exception ignore){

                }
            }
            mySqlUtils.close();
        });

        System.out.println("Time process: " + (System.currentTimeMillis() - timeStart) / 1000 + "s");
        System.out.println("Number user short_hobbies has updated :" + categoryCustomerDf.count());
        categoryCustomerDf.unpersist();
    }
    public void run(String option) throws InterruptedException {
        JavaStreamingContext javaStreamingContext = sparkUtils.javaStreamingContext;
        JavaInputDStream<ConsumerRecord<Objects, Objects>> stream =
                KafkaUtils.createDirectStream(
                        sparkUtils.javaStreamingContext,
                        LocationStrategies.PreferConsistent(),
                        ConsumerStrategies.Subscribe(Config.KAFKA.TOPIC, Config.KAFKA.KAFKA_PARAM));
        stream.foreachRDD((consumerRecordJavaRDD, time) -> {
            Long start = System.currentTimeMillis();
            JavaRDD<MyEvent> rows = consumerRecordJavaRDD
                    .map(consumerRecord -> RowFactory.create(consumerRecord.value(), consumerRecord.topic()))
                    .map(HandleEventStreaming::transformRow)
                    .filter(Objects::nonNull);
            Dataset<Row> df = sparkUtils.session.createDataFrame(rows, MyEvent.class);
            df.persist(StorageLevel.MEMORY_ONLY());
            if(option.equals("view")){
                handleEventView(df, start);
            } else if (option.equals("hobby")){
                handleUpdateShortHobbies(df, start);
            }
            System.out.println("Number event handle: " + df.count());
            df.show();
            df.unpersist();
        });

        javaStreamingContext.start();
        javaStreamingContext.awaitTermination();
    }
}
