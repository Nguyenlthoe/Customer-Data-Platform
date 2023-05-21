package bk.edu.data.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {
    private String content;
    private Integer book_id;
    private Integer user_id;
}
