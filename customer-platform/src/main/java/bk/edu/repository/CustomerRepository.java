package bk.edu.repository;

import bk.edu.data.entity.CustomerEntity;
import bk.edu.data.entity.SegmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer> {
    @Query(value = "SELECT *  FROM bookshop_customer c " +
            "INNER JOIN cdp_segment_customer_association sc ON sc.user_id = c.user_id " +
            "where sc.segment_id = :segmentId ",
            countQuery = "SELECT count(*)  FROM bookshop_customer c " +
                    "INNER JOIN cdp_segment_customer_association sc ON sc.user_id = c.user_id " +
                    "where sc.segment_id = :segmentId ",nativeQuery = true)
    Page<CustomerEntity> findAllBySegmentId(int segmentId, Pageable pageable);

    CustomerEntity findByUserId(int id);
}
