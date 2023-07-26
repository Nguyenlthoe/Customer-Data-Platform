package bk.edu.job;

import bk.edu.utils.MySqlUtils;
import bk.edu.utils.SparkUtils;
import bk.edu.utils.TimeUtils;
import org.apache.spark.api.java.function.ForeachPartitionFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.storage.StorageLevel;

import java.sql.Timestamp;

import static org.apache.spark.sql.functions.col;

public class UpdateShortHobby {
    public void process(SparkUtils sparkUtil){
        System.out.println("Start update short hobbies");

        long start = System.currentTimeMillis();
        MySqlUtils mySqlUtils = new MySqlUtils();
        mySqlUtils.deleteOldHobby();
        mySqlUtils.close();

        Dataset<Row> df = sparkUtil.getTableDataframe("bookshop_customer");
        //df.printSchema();
        Timestamp time = new Timestamp(TimeUtils.getDayBefore(1));
        df = df.repartition(8).filter(col("updated_at").$greater(time));
        df.persist(StorageLevel.MEMORY_ONLY());

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

        df.select("user_id", "email", "name").show();
        System.out.println("Number user has updated: " + df.count());
        df.unpersist();
    }
    public static void main(String args[]){
        UpdateShortHobby updateShortHobby = new UpdateShortHobby();
        boolean log = true;
        if(args[0].equals("false")){
            log = false;
        }
        SparkUtils sparkUtil = new SparkUtils("updateShortHobbies", log, true);
        updateShortHobby.process(sparkUtil);
    }
}
