package bk.edu.config;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class Config {
    public static final SimpleDateFormat FORMAT_DATE = new SimpleDateFormat("yyyy/MM/dd");

    public static class BillStatus {
        public static final int ORDER = 1;
        public static final int SHIP = 2;
        public static final int DONE = 3;

        public static Map<Integer, String> mapStatusIdToName;
        public static Map<String, Integer> mapNameToStatusId;

        static {
            mapStatusIdToName = new HashMap<>();
            mapStatusIdToName.put(1, "order");
            mapStatusIdToName.put(2, "ship");
            mapStatusIdToName.put(3, "done");

            mapNameToStatusId = new HashMap<>();
            mapNameToStatusId.put("order", 1);
            mapNameToStatusId.put("ship", 2);
            mapNameToStatusId.put("done", 3);
        }
    }
}
