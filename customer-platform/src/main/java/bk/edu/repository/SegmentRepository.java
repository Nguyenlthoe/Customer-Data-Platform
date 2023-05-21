package bk.edu.repository;

import bk.edu.data.entity.AdminEntity;
import bk.edu.data.entity.SegmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SegmentRepository extends JpaRepository<SegmentEntity, Integer> {
    SegmentEntity findBySegmentId(int id);

    SegmentEntity findByAdminAndName(AdminEntity adminEntity, String name);

    SegmentEntity findByAdminAndNameAndIsDeleted(AdminEntity adminEntity, String name, int i);
}
