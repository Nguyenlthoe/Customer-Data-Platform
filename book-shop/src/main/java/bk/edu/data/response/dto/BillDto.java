package bk.edu.data.response.dto;

import bk.edu.config.Config;
import bk.edu.data.entity.BillEntity;
import bk.edu.data.entity.BookEntity;
import lombok.Getter;

import java.util.List;

@Getter
public class BillDto {
    private Integer id;

    private String name;

    private String phoneNumber;

    private String address;

    private List<BookDto> books;

    private String date;

    private Integer total;

    public BillDto(BillEntity bill, List<BookDto> books){
        this.id = bill.getBillId();
        this.name = bill.getName();
        this.phoneNumber = bill.getPhoneNumber();
        this.address = bill.getAddress();
        this.books = books;
        this.total = bill.getTotal();
        this.date = Config.FORMAT_DATE.format(bill.getCreatedAt());
    }
}
