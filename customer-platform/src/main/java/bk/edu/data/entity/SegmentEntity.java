package bk.edu.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "cdp_segment")
public class SegmentEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "segment_id")
    @Id
    private Integer segmentId;

    @Column(name = "name")
    private String name;

    @Column(name = "rule", columnDefinition = "LONGTEXT")
    private String rule;

    @Column(name = "is_deleted")
    private Integer isDeleted;

    @Column(name = "status")
    private Integer status;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private AdminEntity  admin;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @ManyToMany
    @JoinTable(
            name = "cdp_segment_customer_association",
            joinColumns = @JoinColumn(name = "segment_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<CustomerEntity> customers;

    public SegmentEntity(){
        this.createdAt = new Date();
        this.updatedAt = createdAt;
    }
}
