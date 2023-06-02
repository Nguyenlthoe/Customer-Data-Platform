package bk.edu.controller;

import bk.edu.data.entity.CommentEntity;
import bk.edu.data.mapper.CommentMapper;
import bk.edu.data.req.CommentRequest;
import bk.edu.data.response.base.MyResponse;
import bk.edu.data.response.dto.CommentDto;
import bk.edu.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
public class CommentController {
    @Autowired
    CommentService commentService;

    @Autowired
    CommentMapper commentMapper;

    @RequestMapping(value = "/comment/{commentId}", method = RequestMethod.GET)
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

    @RequestMapping(value ="/comment", method = RequestMethod.POST)
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

    @RequestMapping(value = "/comment", method = RequestMethod.GET)
    public ResponseEntity<?> getComment(@RequestParam(value = "bookId") Integer bookId,
                                        @RequestParam(value = "page", required = false) Optional<Integer> page){
        int pageInt = page.orElse(1);
        Pageable pageable = PageRequest.of(pageInt - 1, 10, Sort.by("createAt").descending());
        Page<CommentEntity> commentEntities = commentService.getCommentByBookId(bookId, pageable);
        List<CommentDto> commentDtoList = commentMapper.listCommentEntityToDto(commentEntities.toList());
        Map<String, Object> mapReturn = new HashMap<>();
        mapReturn.put("comments", commentDtoList);
        mapReturn.put("totalPage", commentEntities.getTotalPages());
        mapReturn.put("pageSize", 10);
        mapReturn.put("pageOffset", pageInt);

        MyResponse response = MyResponse
                .builder()
                .buildCode(200)
                .buildMessage("Successfully")
                .buildData(mapReturn)
                .get();
        return ResponseEntity.ok(response);
    }


}
