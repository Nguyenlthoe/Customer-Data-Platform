package bk.edu.controller;

import bk.edu.data.entity.SegmentEntity;
import bk.edu.data.request.SegmentRequest;
import bk.edu.data.response.base.MyResponse;
import bk.edu.service.SegmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
