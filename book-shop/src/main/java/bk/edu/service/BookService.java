package bk.edu.service;

import bk.edu.data.entity.AuthorEntity;
import bk.edu.data.entity.BookEntity;
import bk.edu.data.entity.CategoryEntity;
import bk.edu.data.entity.PublisherEntity;
import bk.edu.data.mapper.BookMapper;
import bk.edu.data.mapper.PublisherMapper;
import bk.edu.data.req.BookRequest;
import bk.edu.exception.BookRequestInvalid;
import bk.edu.repository.AuthorRepository;
import bk.edu.repository.BookRepository;
import bk.edu.repository.CategoryRepository;
import bk.edu.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookService {
    @Autowired
    BookRepository bookRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    PublisherRepository publisherRepository;

    @Autowired
    BookMapper bookMapper;

    public Page<BookEntity> getListBook(String keyword, int low, int high, Pageable pageable) {
        Page<BookEntity> bookEntityPage = bookRepository.findAllByRangeAndKeyword(low, high, keyword, pageable);
        return bookEntityPage;
    }

    public BookEntity updateBook(BookRequest bookRequest, int bookId) {
        BookEntity bookEntity = bookRepository.findById(bookId);
        if(bookEntity == null){
            throw new BookRequestInvalid("Book not found");
        }
        if(bookRequest.getAuthorIds() != null){
            Set<AuthorEntity> authorEntities = new HashSet<>();
            List<String> listAuthor = new ArrayList<>(Arrays.asList(bookRequest.getAuthorIds().split(",")));
            listAuthor.forEach(authorId -> {
                AuthorEntity author = authorRepository.findByAuthorId(Integer.parseInt(authorId));
                if(author == null){
                    throw new BookRequestInvalid("Author not exist");
                }
                authorEntities.add(author);
            });
            System.out.println("hello");

            bookEntity.setAuthors(authorEntities);
        }

        if(bookRequest.getCategoryIds() != null){
            List<String> listCategory = new ArrayList<>(Arrays.asList(bookRequest.getCategoryIds().split(",")));
            Set<CategoryEntity> categoryEntities = new HashSet<>();

            listCategory.forEach(categoryId -> {
                CategoryEntity category = categoryRepository.findByCategoryId(Integer.parseInt(categoryId));
                if(category == null){
                    throw new BookRequestInvalid("Publisher not exist");
                }
                categoryEntities.add(category);
            });


            bookEntity.setCategories(categoryEntities);
        }

        if (bookRequest.getPublisherId() != null) {
            PublisherEntity publisher = publisherRepository.findByPublisherId(bookRequest.getPublisherId());
            if (publisher == null){
                throw new BookRequestInvalid("Publisher not exist");
            }

            bookEntity.setPublisher(publisher);
        }

        if (bookRequest.getDescription() != null){
            bookEntity.setDescription(bookRequest.getDescription());
        }

        if (bookRequest.getName() != null){
            bookEntity.setName(bookRequest.getName());
        }

        if (bookRequest.getPrice() != null){
            bookEntity.setPrice(bookRequest.getPrice());
        }

        if (bookRequest.getUrlImage() != null){
            bookEntity.setUrlImage(bookRequest.getUrlImage());
        }

        bookRepository.saveAndFlush(bookEntity);

        return bookEntity;
    }

    public BookEntity createBook(BookRequest bookRequest) {
        BookEntity bookEntity = bookMapper.bookRequestToEntity(bookRequest);
        bookRepository.saveAndFlush(bookEntity);
        return bookEntity;
    }

    public Page<BookEntity> getBookByCategory(Integer categoryId, String keyword, int low, int high, Pageable pageable){
        Page<BookEntity> listBook = bookRepository.findByCategoryAndRangeAndKeyword(categoryId, low, high, keyword, pageable);

        return listBook;
    }

    public BookEntity getBookDetail(int id) {
        BookEntity bookEntity = bookRepository.findById(id);
        return bookEntity;
    }

    public List<BookEntity> getAllBook() {
        List<BookEntity> books = bookRepository.findAll();
        return books;
    }
}
