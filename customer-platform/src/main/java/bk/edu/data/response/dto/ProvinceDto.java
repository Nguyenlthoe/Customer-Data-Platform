package bk.edu.data.response.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ProvinceDto {
    int code;
    String name;
    String division_type;
    String codename;
    String phone_code;
    List<Integer> districts;
}
