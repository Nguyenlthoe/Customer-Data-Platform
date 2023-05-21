package bk.edu.data.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Condition {
    private String field;

    private String operator;

    private String value;

    private String type;
}
