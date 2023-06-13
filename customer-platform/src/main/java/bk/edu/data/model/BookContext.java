package bk.edu.data.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookContext implements Serializable {
    private int book_id;

    private int category_id;

    private int publisher_id;

    private int author_id;

    private int price;
}
