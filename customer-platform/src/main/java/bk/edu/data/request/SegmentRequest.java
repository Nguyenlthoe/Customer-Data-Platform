package bk.edu.data.request;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class SegmentRequest {
    private String name;

    private List<ConditionRequest> conditions;

}
