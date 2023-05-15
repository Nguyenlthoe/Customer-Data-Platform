package bk.edu.data.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "bookshop_book")
public class BookEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    @Id
    private Integer bookId;

    @Column(name = "name")
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "release_date")
    private Date releaseDate;

    @Column(name = "view_counts")
    private int viewCount;

    @Column(name = "url_image")
    private String urlImage;

    @Column(name = "price")
    private int price;

    @ManyToMany
    @JoinTable(
            name = "bookshop_category_book_association",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<CategoryEntity> categories;

    @ManyToMany
    @JoinTable(
            name = "bookshop_author_book_association",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<AuthorEntity> authors;

    @Column(name = "sales")
    private int sales;

    @Column(name = "created_at")
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private PublisherEntity  publisher;

    @Column(name = "updated_at")
    private Date updatedAt;

    @OneToMany(mappedBy = "book")
    private Set<CartEntity> cart;

    @OneToMany(mappedBy = "book")
    private Set<BillRelation> bills;

    public BookEntity(){
        this.viewCount = 0;
        this.sales = 0;
        this.createdAt = new Date();
        this.updatedAt = createdAt;
    }
}
