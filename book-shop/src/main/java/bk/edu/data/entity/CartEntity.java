package bk.edu.data.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "bookshop_cart")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
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

    public CartEntity(CartRelationKey cartRelationKey, Integer quantity){
        this.id = cartRelationKey;
        this.quantity = quantity;
    }
}
