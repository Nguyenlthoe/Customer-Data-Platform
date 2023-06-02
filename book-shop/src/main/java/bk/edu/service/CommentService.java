package bk.edu.service;

import bk.edu.data.entity.BookEntity;
import bk.edu.data.entity.CommentEntity;
import bk.edu.data.mapper.CommentMapper;
import bk.edu.data.req.CommentRequest;
import bk.edu.exception.RequestInvalid;
import bk.edu.repository.BookRepository;
import bk.edu.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    CommentMapper commentMapper;
    public CommentEntity getCommentById(int id){
        return commentRepository.findCommentByCommentId(id);
    }

    public CommentEntity createComment(CommentRequest commentRequest){
        CommentEntity commentEntity = commentMapper.commentRequestToEntity(commentRequest);
        commentRepository.saveAndFlush(commentEntity);
        return commentEntity;
    }

    public Page<CommentEntity> getCommentByBookId(Integer bookId, Pageable pageable) {
        BookEntity book = bookRepository.findByBookId(bookId);
        if(book == null){
            throw new RequestInvalid("Book Id not found");
        }
        return commentRepository.findAllByBook(book, pageable);
    }
}
