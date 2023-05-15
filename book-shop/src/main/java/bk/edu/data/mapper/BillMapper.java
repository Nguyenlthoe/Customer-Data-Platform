package bk.edu.data.mapper;

import bk.edu.config.Config;
import bk.edu.data.entity.BillEntity;
import bk.edu.data.entity.BookEntity;
import bk.edu.data.entity.UserEntity;
import bk.edu.data.req.BillRequest;
import bk.edu.data.response.dto.BillDto;
import bk.edu.data.response.dto.BookDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BillMapper {
    @Autowired
    BookMapper bookMapper;
    public BillEntity billRequestToEntity(BillRequest billRequest, UserEntity user) {
        BillEntity bill = new BillEntity();
        bill.setUser(user);
        bill.setAddress(billRequest.getAddress());
        bill.setStatus(Config.BillStatus.ORDER);
        bill.setPhoneNumber(billRequest.getPhoneNumber());
        bill.setName(billRequest.getName());

        return bill;
    }

    public BillDto billEntityToDto(BillEntity bill){

        List<BookEntity> books = new ArrayList<>();
        bill.getBillRelations().forEach(billRelation -> {
            books.add(billRelation.getBook());
        });
        List<BookDto> bookDtoList = bookMapper.listBookEntityToDto(books);
        BillDto billDto = new BillDto(bill, bookDtoList);
        return billDto;
    }

    public List<BillDto> listBillEntityToDto(UserEntity user, List<BillEntity> bills) {
        List<BillDto> billDtoList = new ArrayList<>();
        bills.forEach(bill -> {
            billDtoList.add(billEntityToDto(bill));
        });
        return billDtoList;
    }
}
