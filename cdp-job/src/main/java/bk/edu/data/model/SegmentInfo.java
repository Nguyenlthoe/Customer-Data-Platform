package bk.edu.data.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SegmentInfo {
    private Integer segmentId;

    private List<ConditionInfo> conditions;
}
