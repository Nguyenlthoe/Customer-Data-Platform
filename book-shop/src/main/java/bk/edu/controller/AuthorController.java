package bk.edu.controller;

import bk.edu.data.entity.AuthorEntity;
import bk.edu.data.mapper.AuthorMapper;
import bk.edu.data.req.AuthorRequest;
import bk.edu.data.response.base.MyResponse;
import bk.edu.data.response.dto.AuthorDto;
import bk.edu.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AuthorController {
    @Autowired
    AuthorMapper authorMapper;

    @Autowired
    AuthorService authorService;

    @RequestMapping(value = "/api/v1/author", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthor(@RequestBody AuthorRequest authorRequest) {
        AuthorEntity authorEntity = authorService.createAuthor(authorRequest);

        MyResponse response = MyResponse
                .builder()
                .buildCode(200)
                .buildMessage("Successfully")
                .buildData(authorMapper.authorEntityToDto(authorEntity))
                .get();
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/api/v1/author/{pageId}", method = RequestMethod.GET)
    public ResponseEntity<?> getAuthor(@PathVariable int pageId) {
        Pageable pageable = PageRequest.of(pageId, 10, Sort.by("createdAt").descending());
        Page<AuthorEntity> authorEntityPage = authorService.getListAuthor(pageable);
        List<AuthorDto> authorDtoList = authorMapper.listAuthorEntityToDto(authorEntityPage.toList());
        MyResponse response = MyResponse
                .builder()
                .buildCode(200)
                .buildMessage("Successfully")
                .buildData(authorDtoList)
                .get();
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/api/v1/author/{authorId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateAuthor(@PathVariable int authorId,
                                          @RequestBody AuthorRequest authorRequest) {
        AuthorEntity authorEntity = authorService.updateAuthor(authorRequest, authorId);
        MyResponse response = MyResponse
                .builder()
                .buildCode(200)
                .buildMessage("Successfully")
                .buildData(authorEntity)
                .get();
        return ResponseEntity.ok(response);
    }
}
