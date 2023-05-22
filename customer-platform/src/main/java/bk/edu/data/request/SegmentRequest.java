package bk.edu.data.request;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SegmentRequest {
    private String name;

    private List<ConditionRequest> conditions;

}
