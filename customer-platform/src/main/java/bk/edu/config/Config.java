package bk.edu.config;


import java.text.SimpleDateFormat;
import java.util.*;

public class Config {
    public static final SimpleDateFormat FORMAT_DATE = new SimpleDateFormat("yyyy/MM/dd");

    public static final SimpleDateFormat FORMAT_DATE_SQL = new SimpleDateFormat("yyyy-MM-dd");

    public static final SimpleDateFormat FORMAT_DATETIME_SQL = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final int DATE_SHORT_HOBBY = 3;

    public static final int LIMIT_LONG_HOBBY = 4;

    public static class MYSQL {
        public static String USER = "book_shop";

        public static String PASSWORD = "package1107N";

        public static String HOST = "52.231.108.82:3306";

        public static String DBNAME = "customer-data-platform";
    }

}
