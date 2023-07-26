package bk.edu.utils;

import bk.edu.config.Config;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import java.io.Serializable;


public class SparkUtils implements Serializable{

    public SparkSession session;
    public SQLContext sqlContext;
    public JavaStreamingContext javaStreamingContext;
    public JavaSparkContext javaSparkContext;

    private String user = "book_shop";

    private String password = "package1107N";

    private String host = "172.25.0.1:3306";

    private String dbName = "customer-data-platform";

    public SparkUtils(String nameJob, boolean log, boolean master) {
//        Logger.getLogger("org").setLevel(Level.OFF);
//        Logger.getLogger("akka").setLevel(Level.OFF);
        System.out.println("- Create spark");
        session = createSparkConfig(nameJob, log, master);
        sqlContext = session.sqlContext();
        javaSparkContext = new JavaSparkContext(session.sparkContext());
        javaStreamingContext = new JavaStreamingContext(javaSparkContext, Durations.seconds(60));

        System.out.println("- Create Done!!");
    }

    public SparkSession createSparkConfig(String nameJob, boolean log, boolean master) {
        Logger.getLogger("akka").setLevel(Level.OFF);
        if (!log) {
            System.out.println("run with disable log");
            Logger.getLogger("org").setLevel(Level.OFF);
        }
        if (master) {
            return SparkSession
                    .builder()
                    .appName(nameJob)
                    .config("spark.speculation", "true")
                    .config("spark.sql.parquet.binaryAsString", "true")
                    .config("spark.hadoop.validateOutputSpecs", "false")
                    .config("spark.driver.memory", "1g")
                    .config("spark.sql.shuffle.partitions", "8")
                    .config("spark.speculation","false")    // đè hết config set khi chạy
                    .config("spark.yarn.access.hadoopFileSystems","/data/raw/day/")

                    .getOrCreate();
        } else {
            return null;
        }
    }

    public Dataset<Row> getTableDataframe(String tableName){
        Dataset<Row> df = session.read().format("jdbc")
                .option("numPartitions", "8")
                .option("driver","com.mysql.cj.jdbc.Driver")
                .option("url", "jdbc:mysql://" + Config.MYSQL.HOST + "/" + Config.MYSQL.DBNAME)
                .option("dbtable", tableName)
                .option("user", Config.MYSQL.USER)
                .option("password", Config.MYSQL.PASSWORD)
                .load();
        return df;
    }

    public void close() {
        session.close();
    }

}
