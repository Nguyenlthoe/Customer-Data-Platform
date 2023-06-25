package bk.edu.controller;

import bk.edu.data.entity.CustomerEntity;
import bk.edu.data.entity.SegmentEntity;
import bk.edu.data.mapper.CustomerMapper;
import bk.edu.data.response.base.MyResponse;
import bk.edu.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @Autowired
    CustomerMapper customerMapper;

    @RequestMapping(value = "/api/v1/customer/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getSegment(@PathVariable int id) {
        CustomerEntity customerEntity = customerService.getCustomer(id);

        MyResponse response = MyResponse
                .builder()
                .buildCode(200)
                .buildMessage("Successfully")
                .buildData(customerMapper.customerEntityToDto(customerEntity))
                .get();
        return ResponseEntity.ok(response);
    }
}
