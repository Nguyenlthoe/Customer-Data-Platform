package bk.edu.config;

import org.apache.kafka.common.serialization.StringDeserializer;

import java.text.SimpleDateFormat;
import java.util.*;

public class Config {
    public static final SimpleDateFormat FORMAT_DATE = new SimpleDateFormat("yyyy/MM/dd");

    public static final SimpleDateFormat FORMAT_DATE_SQL = new SimpleDateFormat("yyyy-MM-dd");

    public static final SimpleDateFormat FORMAT_DATETIME_SQL = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public static class kafka {
        public static Map<String, Object> KAFKA_PARAM;
        
        public static Collection<String> TOPIC;
        
        static {
            KAFKA_PARAM = new HashMap<>();

            KAFKA_PARAM.put("bootstrap.servers", "172.25.0.8:9092,172.25.0.9:9093");
            KAFKA_PARAM.put("key.deserializer", StringDeserializer.class);
            KAFKA_PARAM.put("value.deserializer", StringDeserializer.class);
            KAFKA_PARAM.put("group.id", "handle_event" + System.currentTimeMillis());
            KAFKA_PARAM.put("auto.offset.reset", "latest");
            KAFKA_PARAM.put("enable.auto.commit", false);

            List<String> topics = new ArrayList<>();
            topics.add("enriched");
            TOPIC = topics;
        }
    }
}
