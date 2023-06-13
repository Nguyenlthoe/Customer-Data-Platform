package bk.edu.job;

import bk.edu.config.Config;
import bk.edu.data.model.BookContext;
import bk.edu.data.model.EventKafka;
import bk.edu.data.model.MyEvent;
import bk.edu.utils.SparkUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
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
import scala.Serializable;
import scala.Tuple2;

import java.net.URL;
import java.util.*;
import java.util.function.Consumer;

public class HandleEventStreaming {

    private SparkUtils sparkUtils;

    public HandleEventStreaming(){
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
    public static EventKafka transformRow(Row row) {
        String value = row.getAs(0);
        return new EventKafka(value);
    }
    public void run() throws InterruptedException {
        JavaStreamingContext javaStreamingContext = sparkUtils.javaStreamingContext;
        JavaInputDStream<ConsumerRecord<Objects, Objects>> stream =
                KafkaUtils.createDirectStream(
                        sparkUtils.javaStreamingContext,
                        LocationStrategies.PreferConsistent(),
                        ConsumerStrategies.Subscribe(Config.kafka.TOPIC, Config.kafka.KAFKA_PARAM));
        stream.foreachRDD((consumerRecordJavaRDD, time) -> {
            JavaRDD<EventKafka> rows = consumerRecordJavaRDD
                    .map(consumerRecord -> RowFactory.create(consumerRecord.value(), consumerRecord.topic()))
                    .map(HandleEventStreaming::transformRow)
                    .filter(Objects::nonNull);
            JavaRDD<MyEvent> myEventJavaRDD = rows.map(HandleEventStreaming::transformEvent);
            Dataset<Row> df = sparkUtils.session.createDataFrame(myEventJavaRDD, MyEvent.class);
            df.show();
            df.printSchema();
//            Dataset<Row> df = sparkUtils.session.createDataFrame(rows, EventKafka.class);
//            Dataset<Row> dfFinal = df.select("collector_tstamp", "event", "event_id", "event_name", "contexts", "unstruct_event", "user_id", "domain_userid");
//            dfFinal.show();
        });

        javaStreamingContext.start();
        javaStreamingContext.awaitTermination();
    }

    private static MyEvent transformEvent(EventKafka eventKafka) {
        MyEvent myEvent = new MyEvent();
        myEvent.setEvent_name(eventKafka.getEvent_name());
        myEvent.setEvent_id(eventKafka.getEvent_id());
        myEvent.setCollector_tstamp(eventKafka.getCollector_tstamp());
        myEvent.setUser_id(eventKafka.getUser_id());
        myEvent.setDomain_userid(eventKafka.getDomain_userid());
        JSONArray data = eventKafka.getContexts().getJSONArray("data");
        for(int i = 0; i < data.length(); i++){
            JSONObject jsonObject = data.getJSONObject(i);
            if(jsonObject.getString("schema").equals("iglu:com.bookshop/product_context/jsonschema/1-0-0")){
                JSONObject databook = jsonObject.getJSONObject("data");
                try {
                    BookContext bookContext = new BookContext(databook.getInt("product_id"), databook.getInt("category_id"),
                            databook.getInt("publisher_id"), databook.getInt("author_id"), databook.getInt("price"));
                    myEvent.getBooks().add(bookContext);
                } catch (Exception e){

                }

            }
        }

        myEvent.setEvent(eventKafka.getUnstruct_event().getJSONObject("data").getString("data"));
        return myEvent;
    };
}
