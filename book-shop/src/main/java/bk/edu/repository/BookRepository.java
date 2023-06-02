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

    @Query(value = "SELECT * FROM bookshop_book AS b WHERE b.book_id IN " +
            "(SELECT b1.book_id FROM bookshop_category_book_association" +
            " AS b1 WHERE b1.category_id = :categoryId)",
            countQuery = "SELECT count(*) FROM bookshop_book AS b WHERE b.book_id IN " +
                    "(SELECT b1.book_id FROM bookshop_category_book_association" +
                    " AS b1 WHERE b1.category_id = :categoryId)",
            nativeQuery = true)
    Page<BookEntity> findByCategory(@Param("categoryId")Integer categoryId, Pageable pageable);

    @Query(value = "SELECT * FROM bookshop_book as b \n" +
            "INNER JOIN bookshop_author_book_association as ba ON ba.book_id = b.book_id \n" +
            "INNER JOIN bookshop_author a ON a.author_id = ba.author_id \n" +
            "WHERE (a.name like :keyword OR b.name like :keyword ) and b.price >= :low and b.price <= :high \n" +
            "and b.book_id in ((SELECT b1.book_id FROM bookshop_category_book_association AS b1 WHERE b1.category_id = :categoryId ))",
            countQuery = "SELECT count(*) FROM bookshop_book as b \n" +
                    "INNER JOIN bookshop_author_book_association as ba ON ba.book_id = b.book_id \n" +
                    "INNER JOIN bookshop_author a ON a.author_id = ba.author_id \n" +
                    "WHERE (a.name like :keyword OR b.name like :keyword ) and b.price >= :low and b.price <= :high \n" +
                    "and b.book_id in ((SELECT b1.book_id FROM bookshop_category_book_association AS b1 WHERE b1.category_id = :categoryId )) ",
            nativeQuery = true)
    Page<BookEntity> findByCategoryAndRangeAndKeyword
            (@Param("categoryId")Integer categoryId,
             @Param("low")Integer low, @Param("high") Integer high,
             @Param("keyword") String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM bookshop_book as b \n" +
            "INNER JOIN bookshop_author_book_association as ba ON ba.book_id = b.book_id \n" +
            "INNER JOIN bookshop_author a ON a.author_id = ba.author_id \n" +
            "WHERE (a.name like :keyword OR b.name like :keyword ) and b.price >= :low and b.price <= :high ",
            countQuery = "SELECT count(*) FROM bookshop_book as b \n" +
                    "INNER JOIN bookshop_author_book_association as ba ON ba.book_id     = b.book_id \n" +
                    "INNER JOIN bookshop_author a ON a.author_id = ba.author_id \n" +
                    "WHERE (a.name like :keyword OR b.name like :keyword ) and b.price >= :low and b.price <= :high ",
            nativeQuery = true)
    Page<BookEntity> findAllByRangeAndKeyword(
             @Param("low")Integer low, @Param("high") Integer high,
             @Param("keyword") String keyword, Pageable pageable);
    Page<BookEntity> findAll(Pageable pageable);
}
