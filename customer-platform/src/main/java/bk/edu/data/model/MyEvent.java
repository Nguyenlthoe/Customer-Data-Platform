package bk.edu.data.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyEvent implements Serializable {
    private String event_id;

    private Long collector_tstamp;

    private String user_id;

    private String domain_userid;

    private String event;

    private Integer book_id;

    private int category_id;

    private int publisher_id;

    private int author_id;

    public void setBook(int book_id, int category_id, int publisher_id, int author_id){
        this.book_id = book_id;
        this.category_id = category_id;
        this.publisher_id = publisher_id;
        this.author_id = author_id;
    }

}
