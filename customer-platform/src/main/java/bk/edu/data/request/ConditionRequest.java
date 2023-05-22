package bk.edu.data.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConditionRequest {
    private String field;

    private String operator;

    private String value;

    private String type;

    @Override
    public String toString() {
        return "Condition{" +
                "field='" + field + '\'' +
                ", operator='" + operator + '\'' +
                ", value='" + value + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
