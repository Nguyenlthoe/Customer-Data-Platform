package bk.edu.data.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class CartRelationKey implements Serializable {
    @Column(name = "user_id")
    Integer userId;

    @Column(name = "book_id")
    Integer bookId;
}
