package bk.edu.data.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "bookshop_publisher")
public class PublisherEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "publisher_id")
    @Id
    private Integer publisherId;

    @Column(name = "name")
    private String name;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    public PublisherEntity(){
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }
}