package bk.edu.controller;

import bk.edu.data.mapper.CartMapper;
import bk.edu.data.response.base.MyResponse;
import bk.edu.data.response.dto.CartDto;
import bk.edu.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CartController {
    @Autowired
    CartService cartService;

    @Autowired
    CartMapper cartMapper;

    @RequestMapping(value = "/api/v1/cart/add", method = RequestMethod.GET)
    public ResponseEntity<?> getAuthor(@RequestParam(value = "userId",required = true) Integer userId,
                                       @RequestParam(value = "bookId") Integer bookId,
                                       @RequestParam(value = "quantity") Integer quantity) {
        cartService.addItem(bookId, userId, quantity);
        MyResponse myResponse = MyResponse
                .builder()
                .buildCode(200)
                .buildMessage("success")
                .get();
        return ResponseEntity.ok(myResponse);
    }

    @RequestMapping(value = "/api/v1/cart", method = RequestMethod.GET)
    public ResponseEntity<?> getAuthor(@RequestParam(value = "userId",required = true) Integer userId) {
        CartDto cart = cartService.getCart(userId);
        MyResponse myResponse = MyResponse
                .builder()
                .buildCode(200)
                .buildData(cart)
                .buildMessage("success")
                .get();
        return ResponseEntity.ok(myResponse);
    }
}
