package bk.edu.data.response.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class BookDto {
    private int id;

    private String name;

    private String description;

    private int price;

    private int sale;

    private int viewCount;

    private RelationDto publisher;

    private List<RelationDto> authors;

    private List<RelationDto> categories;
}
