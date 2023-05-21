package bk.edu.repository;

import bk.edu.data.entity.BookEntity;
import bk.edu.data.entity.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<BookEntity, Integer> {
    BookEntity findByBookId(int id);

    BookEntity findById(int id);

    @Query(value = "SELECT * FROM bookshop_book AS b WHERE b.book_id IN (SELECT b1.book_id FROM bookshop_category_book_association AS b1 WHERE b1.category_id = :categoryId)", nativeQuery = true)
    List<BookEntity> findByCategory(@Param("categoryId")Integer categoryId, Pageable pageable);
    Page<BookEntity> findAll(Pageable pageable);
}
