package bk.edu.repository;

import bk.edu.data.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
   Page<UserEntity> findAll(Pageable pageable);

    UserEntity findByPhoneNumber(String phoneNumber);

    UserEntity findByEmail(String email);

    UserEntity findByUserId(Integer userId);
}
