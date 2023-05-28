import bk.edu.service.UploadService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.impl.JWTParser;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.JWTVerifier;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

public class TestJwt {
    public static void main(String args[]){
//        Algorithm algorithm = Algorithm.HMAC256("nguyenlt");
//        String jwtToken = JWT.create()
//                .withIssuer("auth")
//                .withSubject("sign in")
//                .withClaim("userId", "1")
//                .withClaim("isAdmin", true)
//                .withIssuedAt(new Date())
//                .withExpiresAt(new Date(System.currentTimeMillis() + 5000L))
//                .withJWTId(UUID.randomUUID()
//                        .toString())
//                .sign(algorithm);
//        JWTVerifier verifier = JWT.require(algorithm)
//                .withIssuer("auth")
//                .build();
//        DecodedJWT decodedJWT = verifier.verify(jwtToken);
//        Claim claim = decodedJWT.getClaim("userId");
//        String userId = claim.asString();
//        Claim claim1 = decodedJWT.getClaim("isAdmin");
//        Boolean isAdmin = claim1.asBoolean();
//        System.out.println(isAdmin);
        System.out.println(UploadService.CURRENT_FOLDER);
        Path staticPath = Paths.get("static");
        Path imagePath = Paths.get("images");
        System.out.println(imagePath.getRoot());
    }
}
