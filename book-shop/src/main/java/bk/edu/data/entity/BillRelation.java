package bk.edu.data.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Table(name = "bookshop_bill_book_association")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BillRelation {
    @EmbeddedId
    BillRelationKey id;

    @ManyToOne
    @MapsId("billId")
    @JoinColumn(name = "bill_id")
    BillEntity bill;

    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    BookEntity book;

    Integer quantity;

    public BillRelation(BillRelationKey id, Integer quantity){
        this.id = id;
        this.quantity = quantity;
    }
}
