package bk.edu.service;

import bk.edu.data.entity.BookEntity;
import bk.edu.data.entity.CartEntity;
import bk.edu.data.entity.CartRelationKey;
import bk.edu.data.entity.UserEntity;
import bk.edu.data.mapper.CartMapper;
import bk.edu.data.response.dto.CartDto;
import bk.edu.exception.CartRequestInvalid;
import bk.edu.repository.BookRepository;
import bk.edu.repository.CartRepository;
import bk.edu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class CartService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    CartMapper cartMapper;

    public void addItem(Integer bookId, Integer userId, Integer quantity) {
        BookEntity book = bookRepository.findByBookId(bookId);
        UserEntity user = userRepository.findByUserId(userId);

        if(user == null){
            throw new CartRequestInvalid("User not found");
        }

        if(book == null){
            throw new CartRequestInvalid("Book not found");
        }

        CartRelationKey cartRelationKey = new CartRelationKey(userId, bookId);
        Optional<CartEntity> cartEntity = cartRepository.findById(cartRelationKey);
        CartEntity cart;
        if(cartEntity.isPresent()){
            cart = cartEntity.get();
            cart.setQuantity(cart.getQuantity() + quantity);
        } else {
            cart = new CartEntity(cartRelationKey, user, book, quantity);
        }
        cartRepository.saveAndFlush(cart);
    }

    public CartDto getCart(Integer userId) {

        UserEntity user = userRepository.findByUserId(userId);

        if(user == null){
            throw new CartRequestInvalid("User not found");
        }
        List<CartEntity> cartEntities = cartRepository.findAllByUser(user);

        return cartMapper.listCartEntityToDto(userId, cartEntities);
    }
}
