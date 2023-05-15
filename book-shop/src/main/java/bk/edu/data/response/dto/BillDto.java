package bk.edu.data.response.dto;

import bk.edu.data.entity.BillEntity;
import lombok.Getter;

import java.util.List;

@Getter
public class BillDto {
    private Integer billId;

    private String name;

    private String phoneNumber;

    private String address;

    private List<BookDto> books;

    private Integer total;

    public BillDto(BillEntity bill, List<BookDto> books){
        this.billId = bill.getBillId();
        this.name = bill.getName();
        this.phoneNumber = bill.getPhoneNumber();
        this.address = bill.getAddress();
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
