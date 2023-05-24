package bk.edu.utils;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SparkSession;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SparkUtils implements Serializable {

    public SparkSession session;
    public SQLContext sqlContext;

    private String user = "book_shop";

    private String password = "package1107N";

    private String host = "172.25.0.7:3306";

    private String dbName = "customer-data-platform";

    public SparkUtils(String nameJob, boolean log, boolean master) {
        System.out.println("- Create spark");
        session = createSparkConfig(nameJob, log, master);
        sqlContext = session.sqlContext();
        System.out.println("- Create Done!!");
    }

    public SparkSession createSparkConfig(String nameJob, boolean log, boolean master) {
        if (!log) {
            System.out.println("run with disable log");
            Logger.getLogger("org").setLevel(Level.OFF);
            Logger.getLogger("akka").setLevel(Level.OFF);
        }
        if (master) {
            return SparkSession
                    .builder()
                    .appName(nameJob)
                    .config("spark.speculation", "true")
                    .config("spark.sql.parquet.binaryAsString", "true")
                    .config("spark.hadoop.validateOutputSpecs", "false")
                    .config("spark.driver.memory", "1g")
                    .config("spark.speculation","false")    // đè hết config set khi chạy
                    .config("spark.yarn.access.hadoopFileSystems","/data/raw/day/")

                    .getOrCreate();
        } else {
            return null;
        }
    }

    public Dataset<Row> getTableDataframe(String tableName){
        Dataset<Row> df = session.read().format("jdbc")
                .option("driver","com.mysql.cj.jdbc.Driver")
                .option("url", "jdbc:mysql://" + host + "/" + dbName)
                .option("dbtable", tableName)
                .option("user", user)
                .option("password", password)
                .load();
        return df;
    }

    public void close() {
        session.close();
    }

}
