package bk.edu.config;

import org.apache.kafka.common.serialization.StringDeserializer;

import java.text.SimpleDateFormat;
import java.util.*;

public class Config {
    public static final SimpleDateFormat FORMAT_DATE = new SimpleDateFormat("yyyy/MM/dd");

    public static final SimpleDateFormat FORMAT_DATE_SQL = new SimpleDateFormat("yyyy-MM-dd");

    public static final SimpleDateFormat FORMAT_DATETIME_SQL = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final int DATE_SHORT_HOBBY = 3;

    public static final int LIMIT_LONG_HOBBY = 4;
    
    public static class KAFKA {
        public static Map<String, Object> KAFKA_PARAM;
        
        public static Collection<String> TOPIC;
        
        static {
            KAFKA_PARAM = new HashMap<>();

            KAFKA_PARAM.put("bootstrap.servers", "52.231.108.82:9092,20.214.141.95:9092");
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

        public static String HOST = "52.231.108.82:3306";

        public static String DBNAME = "customer-data-platform";
    }

    public static class ELASTIC {
        public static final String[] HOSTS = {"20.214.141.95"};

        public static final String PORT = "9200";

        public static final String USER = "elastic";

        public static final String PASSWORD = "MagicWord";

        public static final String TRACKING_INDEX = "tracking_action_product";
    }
}
