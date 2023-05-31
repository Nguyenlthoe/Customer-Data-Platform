package bk.edu.data.response.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
public class UserDto {
    private Integer userId;

    private String name;

    private String email;

    private String phoneNumber;

    private Integer gender;

    private String birthday;

    private String urlAvatar;

}
