package bk.edu.data.response.dto;

import bk.edu.data.entity.AdminEntity;
import bk.edu.data.model.ConditionInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor

public class SegmentDto {
    private Integer segmentId;

    private String name;

    private List<ConditionInfo> conditions;

    private String createdAt;

    private String updatedAt;
}
