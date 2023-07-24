package bk.edu.job;

import bk.edu.utils.ElasticUtils;
import bk.edu.utils.MySqlUtils;
import bk.edu.utils.SparkUtils;
import bk.edu.utils.TimeUtils;
import org.apache.spark.api.java.function.ForeachPartitionFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.sql.Timestamp;
import java.util.Set;

import static org.apache.spark.sql.functions.col;

public class UpdateShortHobby {
    public void process(){
        System.out.println("Start update short hobbies");
        SparkUtils sparkUtil = new SparkUtils("update short hobby", true, true);

        long start = System.currentTimeMillis();
        MySqlUtils mySqlUtils = new MySqlUtils();
        mySqlUtils.deleteOldHobby();
        mySqlUtils.close();

        Dataset<Row> df = sparkUtil.getTableDataframe("bookshop_customer");
        //df.printSchema();
        df = df.filter(col("updated_at").$greater(new Timestamp(TimeUtils.getDayBefore(1))));
        df.select("user_id", "email", "name").show();

        df.foreachPartition((ForeachPartitionFunction<Row>) t -> {
            MySqlUtils mySql = new MySqlUtils();
            while (t.hasNext()){
                Row row = t.next();
                int userId = row.getInt(0);
                mySql.updateShortHobbies(userId);
            }
            mySql.close();
        });

        System.out.println("Time process: " + (System.currentTimeMillis() - start) / 1000 + "s");

        System.out.println("Number user has updated: " + df.count());
    }
    public static void main(String args[]){
        UpdateShortHobby updateShortHobby = new UpdateShortHobby();
        updateShortHobby.process();
    }
}
