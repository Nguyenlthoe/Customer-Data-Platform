package bk.edu.repository;

import bk.edu.data.entity.BookEntity;
import bk.edu.data.entity.CartEntity;
import bk.edu.data.entity.CartRelationKey;
import bk.edu.data.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<CartEntity, CartRelationKey> {
    CartEntity findByUserAndBook(UserEntity userEntity, BookEntity bookEntity);

    List<CartEntity> findAllByUser(UserEntity user);
}
