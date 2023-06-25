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

    private List<RelationDto> hobbies;

    private Double avgBookValue;

    private Double avgBillValue;

    private Integer minBillValue;

    public CustomerDto(Integer userId, String name, String email, String phoneNumber,
                       Integer gender, String urlAvatar,
                       Double avgBookValue, Double avgBillValue, Integer minBillValue) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.urlAvatar = urlAvatar;
        this.avgBookValue = avgBookValue;
        this.avgBillValue = avgBillValue;
        this.minBillValue = minBillValue;
    }
}
