package bk.edu.repository;

import bk.edu.data.entity.PublisherEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PublisherRepository extends JpaRepository<PublisherEntity, Integer> {
    PublisherEntity findByPublisherId(int commentId);

    Page<PublisherEntity> findAll(Pageable pageable);
}
