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
import java.util.Optional;

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

    @RequestMapping(value = "/detail/book/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getDetailBook(@PathVariable int id){
        BookEntity bookEntity = bookService.getBookDetail(id);

        MyResponse response = MyResponse
                .builder()
                .buildCode(200)
                .buildMessage("Successfully")
                .buildData(bookMapper.bookEntityToDto(bookEntity))
                .get();
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/book/{pageId}", method = RequestMethod.GET)
    public ResponseEntity<?> getBook(@PathVariable int pageId,
                                     @RequestParam(value = "categoryId", required = false)Optional<Integer> categoryId,
                                     @RequestParam(value = "authorId", required = false)Optional<Integer> authorId,
                                     @RequestParam(value = "publisherId", required = false)Optional<Integer> publisherId,
                                     @RequestParam(value = "keyword", required = false)Optional<String> keyword,
                                     @RequestParam(value = "low", required = false) Optional<Integer> low,
                                     @RequestParam(value = "high", required = false) Optional<Integer> high) {
        String kw = keyword.orElse("");
        kw = "%" + kw + "%";
        Integer priceStart = low.orElse(0);
        Integer priceEnd = high.orElse(2000000);
        Page<BookEntity> bookEntityPage;
        Pageable pageable = PageRequest.of(pageId - 1, 10, Sort.by("created_at").descending());
        if (categoryId.isPresent()){
            bookEntityPage = bookService.getBookByCategory(categoryId.get(), kw, priceStart, priceEnd , pageable);
        } else {
            bookEntityPage = bookService.getListBook(kw, priceStart, priceEnd, pageable);
        }

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

    @RequestMapping(value = "/all/book", method = RequestMethod.GET)
    public ResponseEntity<?> getAllBook() {
        List<BookDto> books = bookMapper.listBookEntityToDto(bookService.getAllBook());

        Map<String, Object> mapReturn = new HashMap<>();
        MyResponse response = MyResponse
                .builder()
                .buildCode(200)
                .buildMessage("Successfully")
                .buildData(books)
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
}
