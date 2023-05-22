package bk.edu.config;

import java.util.HashMap;
import java.util.Map;

public class ConditionConfig {
    public static class OperatorConfig {
        public static final int EQUAL = 1;
        public static final int GREATER = 2;
        public static final int LESS = 3;
        public static final int GREATER_AND_EQUAL = 4;
        public static final int LESS_AND_EQUAL = 5;
        public static final int CONTAIN = 6;

        public static Map<Integer, String> mapOperatorIdToName;
        public static Map<String, Integer> mapNameToOperatorId;

        static {
            mapOperatorIdToName = new HashMap<>();
            mapOperatorIdToName.put(1, "equal");
            mapOperatorIdToName.put(2, "greater");
            mapOperatorIdToName.put(3, "less");
            mapOperatorIdToName.put(4, "greater_equal");
            mapOperatorIdToName.put(5, "less_equal");
            mapOperatorIdToName.put(6, "contain");

            mapNameToOperatorId = new HashMap<>();
            mapNameToOperatorId.put("equal", 1);
            mapNameToOperatorId.put("greater", 2);
            mapNameToOperatorId.put("less", 3);
            mapNameToOperatorId.put("greater_equal", 4);
            mapNameToOperatorId.put("less_equal", 5);
            mapNameToOperatorId.put("contain", 6);
        }
    }

    public static class TypeConfig {
        public static final int INTEGER = 1;
        public static final int LONG = 2;
        public static final int FLOAT = 3;
        public static final int TIMESTAMP = 4;
        public static final int STRING = 5;
        public static final int DATETIME = 6;
        public static final int BOOLEAN = 7;

        public static Map<Integer, String> mapTypeIdToName;
        public static Map<String, Integer> mapNameToTypeId;

        static {
            mapTypeIdToName = new HashMap<>();
            mapTypeIdToName.put(1, "integer");
            mapTypeIdToName.put(2, "long");
            mapTypeIdToName.put(3, "float");
            mapTypeIdToName.put(4, "timestamp");
            mapTypeIdToName.put(5, "string");
            mapTypeIdToName.put(6, "datetime");
            mapTypeIdToName.put(7, "boolean");

            mapNameToTypeId = new HashMap<>();
            mapNameToTypeId.put("integer", 1);
            mapNameToTypeId.put("long", 2);
            mapNameToTypeId.put("float", 3);
            mapNameToTypeId.put("timestamp", 4);
            mapNameToTypeId.put("string", 5);
            mapNameToTypeId.put("datetime", 6);
            mapNameToTypeId.put("boolean", 7);
        }
    }
}

