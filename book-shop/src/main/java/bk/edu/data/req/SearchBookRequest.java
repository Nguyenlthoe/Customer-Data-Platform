package bk.edu.data.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchBookRequest {
    private String keyword;

    private RangeRequest range;
}
