package bk.edu.data.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "bookshop_bill")
public class BillEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bill_id")
    @Id
    private Integer billId;


    @Column(name = "created_at")
    private Date createdAt;


    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "name")
    private String name;

    @Column(name = "status")
    private Integer status;

    @Column(name = "total")
    private Integer total;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity  user;

    @OneToMany(mappedBy = "bill")
    private Set<BillRelation> billRelations;

    public BillEntity(){
        this.createdAt = new Date();
    }
}
