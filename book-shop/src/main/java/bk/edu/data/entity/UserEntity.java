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
@Table(name = "bookshop_customer")
public class UserEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    @Id
    private Integer userId;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;


    @Column(name = "phone_number")
    private String phoneNumber;


    @Column(name = "password")
    private String password;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "gender")
    private Integer gender;

    @Column(name = "birthday")
    private Date birthday;

    @Column(name = "url_avatar")
    private String urlAvatar;

    @Column(name = "long_hobbies")
    private String hobby;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "address")
    private String address;

    @OneToMany(mappedBy = "user")
    private Set<CartEntity> carts;

    @OneToMany(mappedBy = "user")
    private Set<BillEntity> bills;

    public UserEntity(){
        this.createdAt = new Date();
        this.updatedAt = createdAt;
    }
}
