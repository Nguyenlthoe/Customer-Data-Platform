package bk.edu.data.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bookshop_comment")
public class CommentEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    @Id
    private Integer commentId;

    @Column(name = "content")
    private String content;

    @Column(name = "create_at")
    private Date createAt;

    @Column(name = "update_at")
    private Date updateAt;

    @ManyToOne
    @JoinColumn (name = "book_id")
    private  BookEntity book;

    @ManyToOne
    @JoinColumn (name = "user_id")
    private UserEntity user;
}
