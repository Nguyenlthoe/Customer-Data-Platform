package bk.edu.repository;

import bk.edu.data.entity.BookEntity;
import bk.edu.data.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
    CommentEntity findCommentByCommentId(int commentId);

    Page<CommentEntity> findAllByBook(BookEntity book, Pageable pageable);
}
