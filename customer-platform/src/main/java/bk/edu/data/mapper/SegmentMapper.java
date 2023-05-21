package bk.edu.data.mapper;

import bk.edu.data.entity.SegmentEntity;
import bk.edu.data.request.SegmentRequest;
import bk.edu.exception.SegmentRequestInvalid;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class SegmentMapper {
    public SegmentEntity segmentRequestToEntity(SegmentRequest segmentRequest){
        SegmentEntity segmentEntity = new SegmentEntity();
        segmentEntity.setName(segmentRequest.getName());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            segmentEntity.setRule(objectMapper.writeValueAsString(segmentRequest.getConditions()));
        } catch (Exception e) {
            throw new SegmentRequestInvalid("Invalid format Condition");
        }

        segmentEntity.setIsDeleted(0);
        return segmentEntity;
    }
}
