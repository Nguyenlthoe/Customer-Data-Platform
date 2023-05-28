package bk.edu.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Config {
    public static final SimpleDateFormat FORMAT_DATE = new SimpleDateFormat("yyyy/MM/dd");

    public static final Algorithm JWT_ALGORITHM = Algorithm.HMAC256("nguyenlt");
    public static final JWTVerifier JWT_VERIFIER = JWT.require(JWT_ALGORITHM )
            .withIssuer("auth")
            .build();

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
