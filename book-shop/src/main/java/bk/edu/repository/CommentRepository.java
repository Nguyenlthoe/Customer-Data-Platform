package bk.edu.repository;

import bk.edu.data.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
    CommentEntity findCommentByCommentId(int commentId);
}
