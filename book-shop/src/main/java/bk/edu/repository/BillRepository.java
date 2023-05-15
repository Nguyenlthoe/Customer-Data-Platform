package bk.edu.repository;

import bk.edu.data.entity.BillEntity;
import bk.edu.data.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillRepository extends JpaRepository<BillEntity, Integer> {
    BillEntity findByBillId(int id);

    List<BillEntity> findAllByUser(UserEntity userEntity);

    List<BillEntity> findAllByUserAndStatus(UserEntity user, Integer status);
}
