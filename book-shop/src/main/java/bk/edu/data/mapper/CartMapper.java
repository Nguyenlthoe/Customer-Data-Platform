package bk.edu.data.mapper;

import bk.edu.data.entity.BookEntity;
import bk.edu.data.entity.CartEntity;
import bk.edu.data.response.dto.BookDto;
import bk.edu.data.response.dto.CartDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartMapper {
    @Autowired
    BookMapper bookMapper;

    public CartDto listCartEntityToDto(Integer userId, List<CartEntity> carts){
        List<BookDto> books = new ArrayList<>();
        carts.forEach(cartEntity -> {
            BookEntity book = cartEntity.getBook();
            books.add(bookMapper.bookEntityToDtoRelation(book, cartEntity.getQuantity()));
        });
        CartDto cartDto = new CartDto(userId, books);
        cartDto.updateTotal();
        return cartDto;
    }
}
