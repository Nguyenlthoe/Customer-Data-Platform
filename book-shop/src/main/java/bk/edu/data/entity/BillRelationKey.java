package bk.edu.data.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor

public class BillRelationKey implements Serializable {
    @Column(name = "bill_id")
    Integer billId;

    @Column(name = "book_id")
    Integer bookId;
}
