package bk.edu.utils;

import bk.edu.data.model.ConditionInfo;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import bk.edu.config.*;

import java.util.Date;

import static org.apache.spark.sql.functions.col;

public class TransformUtils {
    public static Dataset<Row> filterCondition(ConditionInfo condition, Dataset<Row> df){
        if(condition.getOperator() == ConditionConfig.OperatorConfig.EQUAL
            && condition.getType() == ConditionConfig.TypeConfig.DATETIME){
            int numYear = Integer.parseInt(condition.getValue());
            Long timeMax = TimeUtils.getYearBefore(numYear);
            Long timeMin = TimeUtils.getYearBefore(numYear + 1);
            String dateMax ="'" + Config.FORMAT_DATE_SQL.format(new Date(timeMax)) + "'";
            String dateMin = "'" + Config.FORMAT_DATE_SQL.format(new Date(timeMin)) + "'";
            Dataset<Row> finalDf = df
                .filter(condition.getField() + " < " + dateMax)
                .filter(condition.getField() + " > " + dateMin);
            return finalDf;
        }

        String value = condition.getValue();
        switch (condition.getType()){
            case ConditionConfig.TypeConfig.DATETIME:
                int numYear = Integer.parseInt(condition.getValue());
                Long datetime = TimeUtils.getYearBefore(numYear);
                if(condition.getOperator() == ConditionConfig.OperatorConfig.GREATER
                    || condition.getOperator() == ConditionConfig.OperatorConfig.LESS_AND_EQUAL){
                    datetime = TimeUtils.getYearBefore(numYear + 1);
                }
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
        Dataset<Row> filterDf = df.filter(col("cc").equalTo(7));
        return filterDf;
    }
}
