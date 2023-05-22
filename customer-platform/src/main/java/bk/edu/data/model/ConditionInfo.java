package bk.edu.data.model;public class ConditionInfo {
    private String field;

    private int operator;

    private String value;

    private int type;

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
