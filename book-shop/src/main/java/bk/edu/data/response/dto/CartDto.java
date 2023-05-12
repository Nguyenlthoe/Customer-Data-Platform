package bk.edu.data.response.dto;


import lombok.Getter;

import java.util.List;

@Getter
public class CartDto {
    private Integer userId;

    private List<BookDto> books;

    private Integer total;

    public CartDto(int userId, List<BookDto> books){
        this.userId = userId;
        this.books = books;
    }

    public void updateTotal(){
        final Integer[] sum = {0};
        books.forEach(bookDto -> {
            sum[0] += bookDto.getQuantity() * bookDto.getPrice();
        });
        this.total = sum[0];
    }
}
