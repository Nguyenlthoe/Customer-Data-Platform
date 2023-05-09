package bk.edu.controller;

import bk.edu.data.entity.PublisherEntity;
import bk.edu.data.mapper.PublisherMapper;
import bk.edu.data.req.PublisherRequest;
import bk.edu.data.response.base.MyResponse;
import bk.edu.data.response.dto.PublisherDto;
import bk.edu.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PublisherController {
    @Autowired
    PublisherService publisherService;

    @Autowired
    PublisherMapper publisherMapper;

    @RequestMapping(value = "/api/v1/publisher", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthor(@RequestBody PublisherRequest PublisherRequest) {
        PublisherEntity publisherEntity = publisherService.createPublisher(PublisherRequest);

        MyResponse response = MyResponse
                .builder()
                .buildCode(200)
                .buildMessage("Successfully")
                .buildData(publisherMapper.publisherEntityToDto(publisherEntity))
                .get();
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/api/v1/publisher/{pageId}", method = RequestMethod.GET)
    public ResponseEntity<?> getAuthor(@PathVariable int pageId) {
        Pageable pageable = PageRequest.of(pageId, 10, Sort.by("createdAt").descending());
        Page<PublisherEntity> publisherEntityPage = publisherService.getListPublisher(pageable);
        List<PublisherDto> publisherDtoList = publisherMapper.listPublisherEntityToDto(publisherEntityPage.toList());
        MyResponse response = MyResponse
                .builder()
                .buildCode(200)
                .buildMessage("Successfully")
                .buildData(publisherDtoList)
                .get();
        return ResponseEntity.ok(response);
    }
}
