package bk.edu.repository;

import bk.edu.data.entity.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<BookEntity, Integer> {
    BookEntity findByBookId(int id);

    BookEntity findById(int id);

    Page<BookEntity> findAll(Pageable pageable);
}
