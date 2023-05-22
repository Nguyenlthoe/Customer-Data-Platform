package bk.edu.controller;

import bk.edu.data.entity.CustomerEntity;
import bk.edu.data.entity.SegmentEntity;
import bk.edu.data.request.SegmentRequest;
import bk.edu.data.response.base.MyResponse;
import bk.edu.service.SegmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class SegmentController {
    @Autowired
    SegmentService segmentService;


    @RequestMapping(value = "/api/v1/segment", method = RequestMethod.GET)
    public ResponseEntity<?> getSegment(@RequestParam(value = "segmentId") Integer segmentId) {
        SegmentEntity segment = segmentService.getSegment(segmentId);

        MyResponse response = MyResponse
                .builder()
                .buildCode(200)
                .buildMessage("Successfully")
                .buildData(segment.getCustomers())
                .get();
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/api/v1/segment/{segmentId}", method = RequestMethod.GET)
    public ResponseEntity<?> getCustomerBySegment(@PathVariable int segmentId,
                                                  @RequestParam(value = "pageId", required = false) Optional<Integer> page) {
        SegmentEntity segment = segmentService.getSegment(segmentId);
        int pageInt = page.orElse(0);
        Pageable pageable = PageRequest.of(pageInt, 10, Sort.by("name").descending());
        Page<CustomerEntity> customerEntityPage = segmentService.getCustomersBySegment(segmentId, pageable);

        Map<String, Object> mapReturn = new HashMap<>();
        mapReturn.put("audiences", customerEntityPage.toList());
        mapReturn.put("totalPages", customerEntityPage.getTotalPages());
        mapReturn.put("totalElements",customerEntityPage.getTotalElements());
        mapReturn.put("pageSize", 10);
        mapReturn.put("pageOffset", pageInt);
        MyResponse response = MyResponse
                .builder()
                .buildCode(200)
                .buildMessage("Successfully")
                .buildData(segment.getCustomers())
                .get();
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/api/v1/segment", method = RequestMethod.POST)
    public ResponseEntity<?> createSegment(@RequestParam(value = "userId") Integer userId,
                                           @RequestBody SegmentRequest segmentRequest) {
        SegmentEntity segment = segmentService.createSegment(segmentRequest, userId);

        MyResponse response = MyResponse
                .builder()
                .buildCode(200)
                .buildMessage("Successfully")
                .buildData(segment)
                .get();
        return ResponseEntity.ok(response);
    }
}
