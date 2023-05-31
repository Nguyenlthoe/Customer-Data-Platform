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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class PublisherController {
    @Autowired
    PublisherService publisherService;

    @Autowired
    PublisherMapper publisherMapper;

    @RequestMapping(value = "/publisher", method = RequestMethod.POST)
    public ResponseEntity<?> createPublisher(@RequestBody PublisherRequest PublisherRequest) {
        PublisherEntity publisherEntity = publisherService.createPublisher(PublisherRequest);

        MyResponse response = MyResponse
                .builder()
                .buildCode(200)
                .buildMessage("Successfully")
                .buildData(publisherMapper.publisherEntityToDto(publisherEntity))
                .get();
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/publisher/{pageId}", method = RequestMethod.GET)
    public ResponseEntity<?> getAuthor(@PathVariable int pageId) {
        Pageable pageable = PageRequest.of(pageId - 1, 10, Sort.by("createdAt").descending());
        Page<PublisherEntity> publisherEntityPage = publisherService.getListPublisher(pageable);
        List<PublisherDto> publisherDtoList = publisherMapper.listPublisherEntityToDto(publisherEntityPage.toList());

        Map<String, Object> mapReturn = new HashMap<>();
        mapReturn.put("publishers", publisherDtoList);
        mapReturn.put("totalPage", publisherEntityPage.getTotalPages());
        mapReturn.put("pageSize", 10);
        mapReturn.put("pageOffset", pageId);
        MyResponse response = MyResponse
                .builder()
                .buildCode(200)
                .buildMessage("Successfully")
                .buildData(mapReturn)
                .get();
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/all/publisher", method = RequestMethod.GET)
    public ResponseEntity<?> getAllPublisher() {
        List<PublisherEntity> publisherEntities = publisherService.getAllPublisher();
        MyResponse response = MyResponse
                .builder()
                .buildCode(200)
                .buildMessage("Successfully")
                .buildData(publisherMapper.listPublisherEntityToDto(publisherEntities))
                .get();
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/publisher/{publisherId}", method = RequestMethod.PUT)
    public ResponseEntity<?>  updatePublisher(@PathVariable int publisherId, @RequestBody PublisherRequest publisherRequest){
        PublisherEntity publisherEntity = publisherService.updatePublisher(publisherRequest, publisherId);
        MyResponse response = MyResponse
                .builder()
                .buildCode(200)
                .buildMessage("Successfully")
                .buildData(publisherEntity)
                .get();
        return ResponseEntity.ok(response);
    }
}
