package bk.edu.data.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {
    private String token;

    private Integer userId;
}
