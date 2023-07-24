package bk.edu.job;

import bk.edu.config.Config;
import bk.edu.storage.ElasticStorage;
import bk.edu.utils.ElasticUtils;
import bk.edu.utils.MySqlUtils;
import bk.edu.utils.SparkUtils;
import bk.edu.utils.TimeUtils;
import org.apache.spark.api.java.function.ForeachPartitionFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import static org.apache.spark.sql.functions.col;

public class UpdateLongHobby implements Serializable {

    public static void main(String args[]){
        UpdateLongHobby updateLongHobby = new UpdateLongHobby();
        boolean log = true;
        if(args[0].equals("false")){
            log = false;
        }
        updateLongHobby.process(log);
    }

    public void process(boolean log){
        SparkUtils sparkUtil = new SparkUtils("update long hobby", log, true);
        System.out.println("Start updating long hobbies");
        long start = System.currentTimeMillis();
        Dataset<Row> df = sparkUtil.getTableDataframe("bookshop_customer");
        //df.printSchema();
        df = df.filter(col("updated_at").$greater(new Timestamp(TimeUtils.getDayBefore(1))));
        df.select("user_id", "email", "name").show();
        df.foreachPartition((ForeachPartitionFunction<Row>) t -> {
            MySqlUtils mySqlUtils = new MySqlUtils();
            ElasticUtils elasticUtils = new ElasticUtils();
            while (t.hasNext()){
                Row row = t.next();
                Integer userId = row.getInt(0);
                String longHobbies = elasticUtils.getLongHobby(userId);

                mySqlUtils.insertLongHobbies(userId, longHobbies);
            }
            elasticUtils.close();
            mySqlUtils.close();
        });

        System.out.println("Time process: " + (System.currentTimeMillis() - start) / 1000 + "s");
        System.out.println("Number user long hobbies updated: " + df.count());

    }

}
