package bk.edu.controller;

import bk.edu.data.entity.CommentEntity;
import bk.edu.data.mapper.CommentMapper;
import bk.edu.data.req.CommentRequest;
import bk.edu.data.response.base.MyResponse;
import bk.edu.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class CommentController {
    @Autowired
    CommentService commentService;

    @Autowired
    CommentMapper commentMapper;

    @RequestMapping(value = "/api/v1/comment/{commentId}", method = RequestMethod.GET)
    public ResponseEntity<?> getComment(@PathVariable int commentId){
        CommentEntity commentEntity = commentService.getCommentById(commentId);
        MyResponse response = MyResponse
                .builder()
                .buildCode(200)
                .buildMessage("Successfully")
                .buildData(commentMapper.commentEntityToDto(commentEntity))
                .get();
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value ="/api/v1/comment", method = RequestMethod.POST)
    public ResponseEntity<?> createComment(@RequestBody CommentRequest commentRequest){
        CommentEntity commentEntity = commentService.createComment(commentRequest);
        MyResponse response = MyResponse
                .builder()
                .buildCode(200)
                .buildMessage("Successfully")
                .buildData(commentMapper.commentEntityToDto(commentEntity))
                .get();
        return ResponseEntity.ok(response);
    }

}
