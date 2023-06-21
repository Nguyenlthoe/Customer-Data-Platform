package bk.edu.config;

import org.apache.kafka.common.serialization.StringDeserializer;

import java.text.SimpleDateFormat;
import java.util.*;

public class Config {
    public static final SimpleDateFormat FORMAT_DATE = new SimpleDateFormat("yyyy/MM/dd");

    public static final SimpleDateFormat FORMAT_DATE_SQL = new SimpleDateFormat("yyyy-MM-dd");

    public static final SimpleDateFormat FORMAT_DATETIME_SQL = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public static class KAFKA {
        public static Map<String, Object> KAFKA_PARAM;
        
        public static Collection<String> TOPIC;
        
        static {
            KAFKA_PARAM = new HashMap<>();

            KAFKA_PARAM.put("bootstrap.servers", "20.196.248.69:9092,20.196.245.32:9092");
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

    public static class MYSQL {
        public static String USER = "book_shop";

        public static String PASSWORD = "package1107N";

        public static String HOST = "20.196.248.69:3306";

        public static String DBNAME = "customer-data-platform";
    }
}
