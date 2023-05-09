package bk.edu.data.mapper;

import bk.edu.data.entity.PublisherEntity;
import bk.edu.data.req.PublisherRequest;
import bk.edu.data.response.dto.PublisherDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PublisherMapper {
    public PublisherDto publisherEntityToDto(PublisherEntity publisherEntity){
        return new PublisherDto(publisherEntity.getPublisherId(), publisherEntity.getName());
    }

    public PublisherEntity publisherRequestToDto(PublisherRequest publisherRequest){
        PublisherEntity PublisherEntity = new PublisherEntity();
        PublisherEntity.setName(publisherRequest.getName());
        return PublisherEntity;
    }

    public List<PublisherDto> listPublisherEntityToDto(List<PublisherEntity> publisherEntities){
        List<PublisherDto> publishers = new ArrayList<>();
        publisherEntities.forEach(publisherEntity -> {
            publishers.add(publisherEntityToDto(publisherEntity));
        });
        return publishers;
    }
}
