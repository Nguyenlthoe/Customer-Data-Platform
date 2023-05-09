package bk.edu.data.mapper;

import bk.edu.config.Config;
import bk.edu.data.entity.AuthorEntity;
import bk.edu.data.entity.BookEntity;
import bk.edu.data.entity.CategoryEntity;
import bk.edu.data.entity.PublisherEntity;
import bk.edu.data.req.BookRequest;
import bk.edu.exception.BookRequestInvalid;
import bk.edu.repository.AuthorRepository;
import bk.edu.repository.CategoryRepository;
import bk.edu.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BookMapper {
    @Autowired
    PublisherRepository publisherRepository;

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    CategoryRepository categoryRepository;

    public BookEntity bookRequestToEntity(BookRequest bookRequest){
        BookEntity bookEntity = new BookEntity();
        List<String> listAuthor = new ArrayList<>(Arrays.asList(bookRequest.getAuthorIds().split(",")));
        List<String> listCategory = new ArrayList<>(Arrays.asList(bookRequest.getCategoryIds().split(",")));
        Set<AuthorEntity> authorEntities = new HashSet<>();
        Set<CategoryEntity> categoryEntities = new HashSet<>();

        listAuthor.forEach(authorId -> {
            AuthorEntity author = authorRepository.findByAuthorId(Integer.parseInt(authorId));
            if(author == null){
                throw new BookRequestInvalid("Author not exist");
            }
            authorEntities.add(author);
        });

        listCategory.forEach(categoryId -> {
            CategoryEntity category = categoryRepository.findByCategoryId(Integer.parseInt(categoryId));
            if(category == null){
                throw new BookRequestInvalid("Publisher not exist");
            }
            categoryEntities.add(category);
        });

        PublisherEntity publisher = publisherRepository.findByPublisherId(bookRequest.getPublisherId());
        if (publisher == null){
            throw new BookRequestInvalid("Publisher not exist");
        }

        bookEntity.setAuthors(authorEntities);
        bookEntity.setCategories(categoryEntities);
        bookEntity.setPublisher(publisher);
        bookEntity.setDescription(bookRequest.getDescription());
        bookEntity.setName(bookRequest.getName());
        bookEntity.setPrice(bookRequest.getPrice());
        bookEntity.setUrlImage(bookRequest.getUrlImage());
        Date releaseDate;
        try {
            releaseDate = Config.FORMAT_DATE.parse(bookRequest.getReleaseDate());
        } catch (Exception e) {
            throw new BookRequestInvalid("release_date must have format: yyyy-MM-dd");
        }
        bookEntity.setReleaseDate(releaseDate);
        return bookEntity;
    }
}
