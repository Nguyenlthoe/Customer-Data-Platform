package bk.edu.data.mapper;

import bk.edu.config.ConditionConfig;
import bk.edu.data.entity.SegmentEntity;
import bk.edu.data.model.ConditionInfo;
import bk.edu.data.request.ConditionRequest;
import bk.edu.data.request.SegmentRequest;
import bk.edu.exception.RequestInvalid;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SegmentMapper {
    public SegmentEntity segmentRequestToEntity(SegmentRequest segmentRequest){
        SegmentEntity segmentEntity = new SegmentEntity();
        segmentEntity.setName(segmentRequest.getName());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<ConditionInfo> conditions = conditionRequestListToInfo(segmentRequest.getConditions());
            segmentEntity.setRule(objectMapper.writeValueAsString(conditions));
        } catch (Exception e) {
            throw new RequestInvalid("Invalid format Condition");
        }

        segmentEntity.setIsDeleted(0);
        return segmentEntity;
    }

    public ConditionInfo conditionRequestToInfo(ConditionRequest conditionRequest){
        try {
            int operator = ConditionConfig.OperatorConfig.mapNameToOperatorId.get(conditionRequest.getOperator());
            int type = ConditionConfig.TypeConfig.mapNameToTypeId.get(conditionRequest.getType());


            return new ConditionInfo(conditionRequest.getField(), operator, conditionRequest.getValue(), type);
        } catch (Exception e){
            throw new RequestInvalid("operator or type not support");
        }
    }

    public List<ConditionInfo> conditionRequestListToInfo(List<ConditionRequest> conditions){
        List<ConditionInfo> conditionInfoList = new ArrayList<>();
        conditions.forEach(condition -> {
            conditionInfoList.add(conditionRequestToInfo(condition));
        });
        return conditionInfoList;
    }
}
