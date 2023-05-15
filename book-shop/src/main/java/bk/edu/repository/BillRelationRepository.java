package bk.edu.repository;

import bk.edu.data.entity.BillEntity;
import bk.edu.data.entity.BillRelation;
import bk.edu.data.entity.BillRelationKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRelationRepository extends JpaRepository<BillRelation, BillRelationKey> {
    BillRelation findByBill(BillEntity bill);
}
