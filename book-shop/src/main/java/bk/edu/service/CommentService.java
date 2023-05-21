package bk.edu.service;

import bk.edu.data.entity.CommentEntity;
import bk.edu.data.mapper.CommentMapper;
import bk.edu.data.req.CommentRequest;
import bk.edu.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;

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
}
