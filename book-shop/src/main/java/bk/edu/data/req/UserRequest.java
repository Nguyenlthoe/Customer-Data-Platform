package bk.edu.data.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {

    private String name;

    private String email;

    private String phoneNumber;

    private Integer gender;

    private String address;

    private String password;

    private String birthday;

    private String urlAvatar;
}
