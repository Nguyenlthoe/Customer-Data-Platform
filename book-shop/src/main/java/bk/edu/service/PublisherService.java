package bk.edu.service;

import bk.edu.data.entity.PublisherEntity;
import bk.edu.data.mapper.PublisherMapper;
import bk.edu.data.req.PublisherRequest;
import bk.edu.exception.BookRequestInvalid;
import bk.edu.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublisherService {
    @Autowired
    PublisherRepository publisherRepository;

    @Autowired
    PublisherMapper publisherMapper;
    public PublisherEntity createPublisher(PublisherRequest publisherRequest) {
        PublisherEntity publisherEntity = publisherMapper.publisherRequestToDto(publisherRequest);
        publisherRepository.saveAndFlush(publisherEntity);
        return publisherEntity;
    }

    public Page<PublisherEntity> getListPublisher(Pageable pageable) {
        Page<PublisherEntity> publisherEntities = publisherRepository.findAll(pageable);
        return publisherEntities;
    }

    public PublisherEntity updatePublisher(PublisherRequest publisherRequest, int publisherId){
        PublisherEntity publisherEntity = publisherRepository.findByPublisherId(publisherId);
        if(publisherEntity == null) throw new BookRequestInvalid("Publisher not exist");
        if(publisherRequest.getName() != null) publisherEntity.setName(publisherRequest.getName());
        publisherRepository.saveAndFlush(publisherEntity);
        return publisherEntity;
    }

    public List<PublisherEntity> getAllPublisher() {
        return publisherRepository.findAll();
    }

    public PublisherEntity getDetailPublisher(int id) {
        return publisherRepository.findByPublisherId(id);
    }
}
