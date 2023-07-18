package bk.edu.service;

import bk.edu.data.entity.AdminEntity;
import bk.edu.data.entity.CustomerEntity;
import bk.edu.data.entity.SegmentEntity;
import bk.edu.data.mapper.SegmentMapper;
import bk.edu.data.model.ConditionInfo;
import bk.edu.data.request.ConditionRequest;
import bk.edu.data.request.SegmentRequest;
import bk.edu.exception.RequestInvalid;
import bk.edu.repository.AdminRepository;
import bk.edu.repository.CustomerRepository;
import bk.edu.repository.SegmentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SegmentService {
    @Autowired
    SegmentRepository segmentRepository;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    SegmentMapper segmentMapper;
    public SegmentEntity getSegment(Integer segmentId) {
        return segmentRepository.findBySegmentId(segmentId);
    }

    public SegmentEntity createSegment(SegmentRequest segmentRequest) {
        Optional<AdminEntity> adminEntity = adminRepository.findById(1);
        if(!adminEntity.isPresent()) {
            throw new RequestInvalid("Admin not found");
        }
        SegmentEntity segment = segmentRepository.findByAdminAndNameAndIsDeleted(adminEntity.get(), segmentRequest.getName(), 0);
        if(segment != null){
            throw new RequestInvalid("Title of segment has exited");
        }

        SegmentEntity segmentEntity = segmentMapper.segmentRequestToEntity(segmentRequest);
        segmentEntity.setAdmin(adminEntity.get());
        segmentRepository.saveAndFlush(segmentEntity);

        return segmentEntity;
    }

    public Page<CustomerEntity> getCustomersBySegment(int segmentId, Pageable pageable){
        SegmentEntity segmentEntity = segmentRepository.findBySegmentIdAndIsDeleted(segmentId, 0);
        if(segmentEntity == null){
            throw new RequestInvalid("SegmentId not found");
        }
        return customerRepository.findAllBySegmentId(segmentId, pageable);
    }

    public SegmentEntity updateSegment(int segmentId, SegmentRequest segmentRequest) {
        SegmentEntity segmentEntity = segmentRepository.findBySegmentIdAndIsDeleted(segmentId, 0);
        if(segmentEntity == null){
            throw new RequestInvalid("Segment not found");
        }
        segmentEntity.setName(segmentRequest.getName());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<ConditionInfo> conditions = segmentMapper.conditionRequestListToInfo(segmentRequest.getConditions());
            segmentEntity.setRule(objectMapper.writeValueAsString(conditions));
        } catch (Exception e) {
            throw new RequestInvalid("Invalid format Condition");
        }
        segmentEntity.setUpdatedAt(new Date());
        segmentRepository.saveAndFlush(segmentEntity);
        return segmentEntity;
    }


    public Page<SegmentEntity> getListSegment(Pageable pageable) {
        Page<SegmentEntity> segmentEntities = segmentRepository.findAllByIsDeleted(0, pageable);
        return segmentEntities;
    }
}
