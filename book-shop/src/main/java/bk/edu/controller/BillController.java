package bk.edu.controller;

import bk.edu.data.mapper.BillMapper;
import bk.edu.data.req.BillRequest;
import bk.edu.data.response.base.MyResponse;
import bk.edu.data.response.dto.BillDto;
import bk.edu.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BillController {
    @Autowired
    BillService billService;

    @Autowired
    BillMapper billMapper;

    @RequestMapping(value = "/api/v1/bill", method = RequestMethod.POST)
    public ResponseEntity<?> addBill(@RequestParam(value = "userId",required = true) Integer userId,
                                     @RequestBody BillRequest billRequest) {
        billService.createBill(billRequest, userId);
        MyResponse myResponse = MyResponse
                .builder()
                .buildCode(200)
                .buildMessage("success")
                .get();
        return ResponseEntity.ok(myResponse);
    }

    @RequestMapping(value = "/api/v1/bill", method = RequestMethod.GET)
    public ResponseEntity<?> getListBill(@RequestParam(value = "userId",required = true) Integer userId,
                                         @RequestParam(value = "status", required = true) Integer status) {
        List<BillDto> bill = billService.getBills(userId, status);
        MyResponse myResponse = MyResponse
                .builder()
                .buildCode(200)
                .buildData(bill)
                .buildMessage("success")
                .get();
        return ResponseEntity.ok(myResponse);
    }
}
