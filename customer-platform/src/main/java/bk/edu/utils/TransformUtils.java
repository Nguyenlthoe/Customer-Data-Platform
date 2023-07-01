
package bk.edu.utils;

import bk.edu.data.model.ConditionInfo;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import bk.edu.config.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.apache.spark.sql.functions.*;

public class TransformUtils {
    public static Dataset<Row> filterCondition(ConditionInfo condition, Dataset<Row> df){
        if(condition.getOperator() == ConditionConfig.OperatorConfig.EQUAL
                && condition.getType() == ConditionConfig.TypeConfig.DATETIME){
            int numYear = Integer.parseInt(condition.getValue());
            Long timeMax = TimeUtils.getYearBefore(numYear);
            Long timeMin = TimeUtils.getYearBefore(numYear + 1);
            String dateMax =  Config.FORMAT_DATE_SQL.format(new Date(timeMax));
            String dateMin = Config.FORMAT_DATE_SQL.format(new Date(timeMin));
            System.out.println(condition.getField() + " < " + dateMax);
            System.out.println(condition.getField() + " < " + dateMin);
            Dataset<Row> finalDf = df
                    .filter(col(condition.getField()).$less(dateMax))
                    .filter(col(condition.getField()).$greater(dateMin));
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
                value =Config.FORMAT_DATE_SQL.format(new Date(datetime));
                break;
            case ConditionConfig.TypeConfig.STRING:
                value =  condition.getValue();
                break;
            default:
                break;
        }
        System.out.println("Value: " + value);
        String field = condition.getField();
        switch (condition.getOperator()){
            case ConditionConfig.OperatorConfig.EQUAL:
                return df.filter(col(field).$eq$eq$eq(value));

            case ConditionConfig.OperatorConfig.GREATER:
                if(condition.getType() == ConditionConfig.TypeConfig.DATETIME){
                    return df.filter(col(field).$less(value));
                } else {
                    return  df.filter(col(field).$greater(value));
                }

            case ConditionConfig.OperatorConfig.LESS:
                if(condition.getType() == ConditionConfig.TypeConfig.DATETIME){
                    return df.filter(col(field).$greater(value));

                } else {
                    return df.filter(col(field).$less(value));
                }

            case ConditionConfig.OperatorConfig.GREATER_AND_EQUAL:
                if(condition.getType() == ConditionConfig.TypeConfig.DATETIME){
                    return df.filter(col(field).$less$eq(value));
                } else {
                    return df.filter(col(field).$greater$eq(value));
                }

            case ConditionConfig.OperatorConfig.LESS_AND_EQUAL:
                if(condition.getType() == ConditionConfig.TypeConfig.DATETIME){
                    return df.filter(col(field).$greater$eq(value));
                } else {
                    return df.filter(col(field).$less$eq(value));
                }

            case ConditionConfig.OperatorConfig.CONTAIN:

                    return df.filter(col(field).contains(" " + value + " "));
                

            case  ConditionConfig.OperatorConfig.NOT_EQUAL:
                if(condition.getType() == ConditionConfig.TypeConfig.DATETIME){
                    int numYear = Integer.parseInt(condition.getValue());
                    Long timeMax = TimeUtils.getYearBefore(numYear);
                    Long timeMin = TimeUtils.getYearBefore(numYear + 1);
                    String dateMax =  Config.FORMAT_DATE_SQL.format(new Date(timeMax));
                    String dateMin = Config.FORMAT_DATE_SQL.format(new Date(timeMin));
                    return df.filter(col(field).$greater$eq(dateMax)).union(df.filter(col(field).$less$eq(dateMin)));
                }else {
                    return df.filter(col(field).notEqual(value));
                }


            case ConditionConfig.OperatorConfig.NOT_CONTAIN:
                    return df.withColumn("is_contain", when(col(field).contains(" " + value + " "), lit (1)).otherwise(lit(0)))
                            .filter(col("is_contain").$eq$eq$eq(0))
                            .drop("is_contain");

        }
        return df.limit(0);
    }
}
