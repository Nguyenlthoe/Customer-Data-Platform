package bk.edu.data.req;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class BookRequest {
    private String name;

    private String description;

    private String urlImage;

    private Integer price;

    private String releaseDate;

    private String categoryIds;

    private String authorIds;

    private Integer publisherId;
}
