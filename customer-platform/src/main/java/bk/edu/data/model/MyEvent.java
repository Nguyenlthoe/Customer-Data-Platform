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

    private String event_name;

    private String event;

    private List<BookContext> books= new ArrayList<>();

}
