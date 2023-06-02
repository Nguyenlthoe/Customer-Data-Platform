package bk.edu.data.response.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CommentDto {

    private Integer id;
    private String content;

    private String userName;

    private Integer userId;
}
