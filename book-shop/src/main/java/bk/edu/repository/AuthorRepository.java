package bk.edu.repository;

import bk.edu.data.entity.AuthorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<AuthorEntity, Integer> {
    AuthorEntity findByAuthorId(int id);

    AuthorEntity findByAuthorIdAndName(int id, String name);

    Page<AuthorEntity> findAll(Pageable pageable);
}
