package bk.edu.controller;

import bk.edu.data.entity.CustomerEntity;
import bk.edu.data.entity.SegmentEntity;
import bk.edu.data.mapper.CustomerMapper;
import bk.edu.data.mapper.SegmentMapper;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class SegmentController {
    @Autowired
    SegmentService segmentService;

    @Autowired
    CustomerMapper customerMapper;

    @Autowired
    SegmentMapper segmentMapper;

    @RequestMapping(value = "/api/v1/page/segment/{segmentId}", method = RequestMethod.GET)
    public ResponseEntity<?> getCustomerBySegment(@PathVariable int segmentId,
                                                  @RequestParam(value = "pageId", required = false) Optional<Integer> page) {
        SegmentEntity segment = segmentService.getSegment(segmentId);
        int pageInt = page.orElse(0);
        Pageable pageable = PageRequest.of(pageInt, 10, Sort.Direction.DESC, "user_id");
        Page<CustomerEntity> customerEntityPage = segmentService.getCustomersBySegment(segmentId, pageable);

        Map<String, Object> mapReturn = new HashMap<>();
        mapReturn.put("customers", customerMapper.listCustomerEntityToDto(customerEntityPage.toList()));
        mapReturn.put("totalPages", customerEntityPage.getTotalPages());
        mapReturn.put("totalElements",customerEntityPage.getTotalElements());
        mapReturn.put("pageSize", 10);
        mapReturn.put("pageOffset", pageInt);
        MyResponse response = MyResponse
                .builder()
                .buildCode(200)
                .buildMessage("Successfully")
                .buildData(mapReturn)
                .get();
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/api/v1/segment/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getSegment(@PathVariable(value = "id") int segmentId) {
        SegmentEntity segment = segmentService.getSegment(segmentId);

        MyResponse response = MyResponse
                .builder()
                .buildCode(200)
                .buildMessage("Successfully")
                .buildData(segmentMapper.segmentEntityToDto(segment))
                .get();
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/api/v1/segment/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateSegment(@PathVariable(value = "id") int segmentId,
                                           @RequestBody SegmentRequest segmentRequest) {
        SegmentEntity segment = segmentService.updateSegment(segmentId, segmentRequest);

        MyResponse response = MyResponse
                .builder()
                .buildCode(200)
                .buildMessage("Successfully")
                .buildData(segment.getCustomers())
                .get();
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/api/v1/segment", method = RequestMethod.GET)
    public ResponseEntity<?> getListSegment(@RequestParam(value = "page", required = false) Optional<Integer> page) {
        int pageInt = page.orElse(0);
        Pageable pageable = PageRequest.of(pageInt, 10, Sort.Direction.DESC, "segmentId");
        Page<SegmentEntity> segmentEntities = segmentService.getListSegment(pageable);

        Map<String, Object> mapReturn = new HashMap<>();
        mapReturn.put("segments", segmentMapper.listSegmentEntityToDto(segmentEntities.toList()));
        mapReturn.put("totalPages", segmentEntities.getTotalPages());
        mapReturn.put("totalElements",segmentEntities.getTotalElements());
        mapReturn.put("pageSize", 10);
        mapReturn.put("pageOffset", pageInt);
        MyResponse response = MyResponse
                .builder()
                .buildCode(200)
                .buildMessage("Successfully")
                .buildData(mapReturn)
                .get();
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/api/v1/segment", method = RequestMethod.POST)
    public ResponseEntity<?> createSegment(@RequestBody SegmentRequest segmentRequest) {
        SegmentEntity segment = segmentService.createSegment(segmentRequest);

        MyResponse response = MyResponse
                .builder()
                .buildCode(200)
                .buildMessage("Successfully")
                .buildData(segment)
                .get();
        return ResponseEntity.ok(response);
    }
}
