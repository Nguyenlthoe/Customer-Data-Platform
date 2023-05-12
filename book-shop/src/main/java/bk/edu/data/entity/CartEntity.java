package bk.edu.data.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "bookshop_cart")
@Setter
@Getter
public class CartEntity {
    @EmbeddedId
    CartRelationKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    UserEntity user;

    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    BookEntity book;

    Integer quantity;
}
