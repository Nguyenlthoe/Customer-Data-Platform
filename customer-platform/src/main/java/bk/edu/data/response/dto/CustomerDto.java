package bk.edu.data.response.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.util.List;

@Getter
@Setter
public class CustomerDto {
    private Integer userId;

    private String name;

    private String email;

    private String phoneNumber;

    private Integer gender;

    private String birthday;

    private String urlAvatar;

    private String hobbies;

    private String shortHobbies;

    private Integer avgBookValue;

    private Integer avgBillValue;

    private Integer minBillValue;

    private Integer totalBookView;

    private String province;

    public CustomerDto(Integer userId, String name, String email, String phoneNumber,
                       Integer gender, String urlAvatar,
                       Double avgBookValue, Double avgBillValue, Integer minBillValue, Integer totalBookView, String province) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.urlAvatar = urlAvatar;
        this.avgBookValue = (int)((double) avgBookValue);
        this.avgBillValue = (int)((double) avgBillValue);
        this.minBillValue = minBillValue;
        this.totalBookView = totalBookView;
        this.province = province;
    }
}
