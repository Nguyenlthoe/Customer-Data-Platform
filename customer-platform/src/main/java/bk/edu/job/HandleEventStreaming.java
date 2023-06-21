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
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
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

import java.net.URL;
import java.sql.Timestamp;
import java.util.*;
import java.util.function.Consumer;

public class HandleEventStreaming {

    private SparkUtils sparkUtils;

    public HandleEventStreaming() {
        this.sparkUtils = new SparkUtils("streaming", false, true);
    }

    public static void main(String[] args) {
        try {
            HandleEventStreaming matching = new HandleEventStreaming();
            matching.run(); // streaming can throw exception
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
        if (myEvent.getEvent().toString().contains("view") && myEvent.getEvent_name().equals("product_action")) {
            for (int i = 0; i < data.length(); i++) {

                try {
                    JSONObject jsonObject = data.getJSONObject(i);
                    if (jsonObject.getString("schema").equals("iglu:com.bookshop/product_context/jsonschema/1-0-0")) {
                        JSONObject databook = jsonObject.getJSONObject("data");
                        myEvent.setBook(databook.getInt("product_id"), databook.getInt("category_id"),
                                databook.getInt("publisher_id"), databook.getInt("author_id"), databook.getInt("price"));
                        break;
                    }
                } catch (Exception e) {

                }
            }
        } else {
            myEvent = null;
        }

        return myEvent;
    }

    public void run() throws InterruptedException {
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

        df.show();
        //df.printSchema();
        //System.out.println(df.count());
        Dataset<Row> bookCustomerDf = df.select("user_id", "book_id").distinct();
        bookCustomerDf.foreachPartition((ForeachPartitionFunction<Row>) t -> {
            MySqlUtils mySqlUtils = new MySqlUtils();
            while (t.hasNext()){
                Row row = t.next();
                try {
                    int userId = Integer.parseInt(row.getString(0));
                    int bookId = row.getInt(1);
                    if(!mySqlUtils.checkExistCustomerBook(userId, bookId)){
                        mySqlUtils.insertCustomerBook(userId, bookId);
                        mySqlUtils.updateBookRead(userId);
                    }
                } catch (Exception ignore){

                }
            }
            mySqlUtils.close();
        });
        System.out.println(bookCustomerDf.count());
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
        System.out.println("Số lượng user có cập nhật" + customerDf.count());
//            Dataset<Row> df = sparkUtils.session.createDataFrame(rows, EventKafka.class);
//            Dataset<Row> dfFinal = df.select("collector_tstamp", "event", "event_id", "event_name", "contexts", "unstruct_event", "user_id", "domain_userid");
//            dfFinal.show();
        });

        javaStreamingContext.start();
        javaStreamingContext.awaitTermination();
    }

    public static MyEvent transformEvent(EventKafka eventKafka) {
        MyEvent myEvent = new MyEvent();
        myEvent.setEvent_name(eventKafka.getEvent_name());
        myEvent.setEvent_id(eventKafka.getEvent_id());
        myEvent.setCollector_tstamp(eventKafka.getCollector_tstamp());
        myEvent.setUser_id(eventKafka.getUser_id());
        myEvent.setDomain_userid(eventKafka.getDomain_userid());
        JSONArray data = eventKafka.getContexts().getJSONArray("data");
        try {
            myEvent.setEvent(eventKafka.getUnstruct_event().getJSONObject("data").getString("data"));
        } catch (Exception e) {
            myEvent.setEvent("page_view");
        }
        if (myEvent.getEvent().equals("view")) {
            for (int i = 0; i < data.length(); i++) {

                try {
                    JSONObject jsonObject = data.getJSONObject(i);
                    if (jsonObject.getString("schema").equals("iglu:com.bookshop/product_context/jsonschema/1-0-0")) {
                        JSONObject databook = jsonObject.getJSONObject("data");
                        myEvent.setBook(databook.getInt("product_id"), databook.getInt("category_id"),
                                databook.getInt("publisher_id"), databook.getInt("author_id"), databook.getInt("price"));
                        break;
                    }
                } catch (Exception e) {

                }
            }
        } else {
            myEvent.setBook(-1, -1, -1, -1, -1);
        }

        return myEvent;
    }

    ;
}
