package bk.edu.data.mapper;

import bk.edu.data.entity.BookEntity;
import bk.edu.data.entity.CommentEntity;
import bk.edu.data.entity.UserEntity;
import bk.edu.data.req.CommentRequest;
import bk.edu.data.response.dto.CommentDto;
import bk.edu.exception.BookRequestInvalid;
import bk.edu.exception.RequestInvalid;
import bk.edu.repository.BookRepository;
import bk.edu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service

public class CommentMapper {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    UserRepository userRepository;

    public CommentDto commentEntityToDto(CommentEntity commentEntity){
        return new CommentDto(commentEntity.getCommentId(), commentEntity.getContent());
    }

    public CommentEntity commentRequestToEntity(CommentRequest commentRequest){
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setContent(commentRequest.getContent());
        commentEntity.setCreateAt(new Date());
        commentEntity.setUpdateAt(new Date());
        BookEntity book = bookRepository.findByBookId(commentRequest.getBook_id());
        if( book == null) throw new BookRequestInvalid("Book do not exist");
        else commentEntity.setBook(book);
        UserEntity user = userRepository.findByUserId(commentRequest.getUser_id());
        if( user == null) throw new RequestInvalid("User do not exist");
        else commentEntity.setUser(user);
        return commentEntity;
    }
}
