package bk.edu.utils;

import bk.edu.data.model.ConditionInfo;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import bk.edu.config.*;

import java.util.Date;

public class TransformUtils {
    public static Dataset<Row> filterCondition(ConditionInfo condition, Dataset<Row> df){
        String value = condition.getValue();
        switch (condition.getType()){
            case ConditionConfig.TypeConfig.DATETIME:
                int numYear = Integer.parseInt(condition.getValue());
                Long datetime = TimeUtils.getYearBefore(numYear);
                value ="'" + Config.FORMAT_DATE_SQL.format(new Date(datetime)) + "'";
                break;
            case ConditionConfig.TypeConfig.STRING:
                value = "'" + condition.getValue() + "'";
                break;
            default:
                break;
        }
        String expression = condition.getField();
        switch (condition.getOperator()){
            case ConditionConfig.OperatorConfig.EQUAL:
                expression = expression + " = ";
                break;
            case ConditionConfig.OperatorConfig.GREATER:
                if(condition.getType() == ConditionConfig.TypeConfig.DATETIME){
                    expression = expression + " < ";
                } else {
                    expression = expression + " > ";
                }
                break;
            case ConditionConfig.OperatorConfig.LESS:
                if(condition.getType() == ConditionConfig.TypeConfig.DATETIME){
                    expression = expression + " > ";
                } else {
                    expression = expression + " < ";
                }
                break;
            case ConditionConfig.OperatorConfig.GREATER_AND_EQUAL:
                if(condition.getType() == ConditionConfig.TypeConfig.DATETIME){
                    expression = expression + " <= ";
                } else {
                    expression = expression + " >= ";
                }
                break;
            case ConditionConfig.OperatorConfig.LESS_AND_EQUAL:
                if(condition.getType() == ConditionConfig.TypeConfig.DATETIME){
                    expression = expression + " >= ";
                } else {
                    expression = expression + " <= ";
                }
                break;
            case ConditionConfig.OperatorConfig.CONTAIN:
                value = "'%" + condition.getValue() + "%'";
                expression = expression + " like ";
                break;
        }
        expression = expression + value;

        System.out.println(expression);
        Dataset<Row> filterDf = df.filter(expression);
        return filterDf;
    }
}
