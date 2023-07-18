package bk.edu.repository;

import bk.edu.data.entity.AdminEntity;
import bk.edu.data.entity.SegmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SegmentRepository extends JpaRepository<SegmentEntity, Integer> {
    SegmentEntity findBySegmentId(int id);

    SegmentEntity findByAdminAndName(AdminEntity adminEntity, String name);

    SegmentEntity findByAdminAndNameAndIsDeleted(AdminEntity adminEntity, String name, int i);

    SegmentEntity findBySegmentIdAndIsDeleted(int segmentId, int i);

    Page<SegmentEntity> findAllByIsDeleted(int i, Pageable pageable);
}
