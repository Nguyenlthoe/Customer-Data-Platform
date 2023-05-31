package bk.edu.controller;

import bk.edu.data.entity.BookEntity;
import bk.edu.data.mapper.BookMapper;
import bk.edu.data.req.BookRequest;
import bk.edu.data.response.base.MyResponse;
import bk.edu.data.response.dto.BookDto;
import bk.edu.service.BookService;
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

@RestController
public class BookController {
    @Autowired
    BookMapper bookMapper;

    @Autowired
    BookService bookService;

    @RequestMapping(value = "/book", method = RequestMethod.POST)
    public ResponseEntity<?> createBook(@RequestBody BookRequest bookRequest) {
        BookEntity bookEntity = bookService.createBook(bookRequest);
        MyResponse response = MyResponse
                .builder()
                .buildCode(200)
                .buildMessage("Successfully")
                .buildData(bookMapper.bookEntityToDto(bookEntity))
                .get();
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/book/{pageId}", method = RequestMethod.GET)
    public ResponseEntity<?> getBook(@PathVariable int pageId) {
        Pageable pageable = PageRequest.of(pageId - 1, 10, Sort.by("createdAt").descending());
        Page<BookEntity> bookEntityPage = bookService.getListBook(pageable);
        List<BookDto> bookDtoList = bookMapper.listBookEntityToDto(bookEntityPage.toList());

        Map<String, Object> mapReturn = new HashMap<>();
        mapReturn.put("books", bookDtoList);
        mapReturn.put("totalPage", bookEntityPage.getTotalPages());
        mapReturn.put("pageSize", 10);
        mapReturn.put("pageOffset", pageId);
        MyResponse response = MyResponse
                .builder()
                .buildCode(200)
                .buildMessage("Successfully")
                .buildData(mapReturn)
                .get();
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/book/{bookId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateBook(@PathVariable int bookId,
                                          @RequestBody BookRequest bookRequest) {
        BookEntity bookEntity = bookService.updateBook(bookRequest, bookId);
        BookDto bookDto = bookMapper.bookEntityToDto(bookEntity);
        MyResponse response = MyResponse
                .builder()
                .buildCode(200)
                .buildMessage("Successfully")
                .buildData(bookDto)
                .get();
        return ResponseEntity.ok(response);
    }
    @RequestMapping(value = "/book/category/{categoryId}", method = RequestMethod.GET)
    public ResponseEntity<?> getBookByCategory(@PathVariable Integer categoryId,
                                               @RequestParam(defaultValue = "0") int page){
        Pageable pageable = PageRequest.of(page, 10, Sort.by("name").descending());

        List<BookEntity> listBook = bookService.getBookByCategory(categoryId, pageable);

        List<BookDto> bookDtoList = bookMapper.listBookEntityToDto(listBook);

        MyResponse response = MyResponse
                .builder()
                .buildCode(200)
                .buildMessage("Successfully")
                .buildData(bookDtoList)
                .get();
        return ResponseEntity.ok(response); // temp
    }
}
