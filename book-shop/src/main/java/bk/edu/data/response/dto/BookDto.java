package bk.edu.data.response.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class BookDto {
    private Integer id;

    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;

    private Integer price;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer sale;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer viewCount;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private RelationDto publisher;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<RelationDto> authors;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<RelationDto> categories;

    private String urlImage;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer quantity;
}
