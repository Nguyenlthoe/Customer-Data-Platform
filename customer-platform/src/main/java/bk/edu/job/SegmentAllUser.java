package bk.edu.job;

import bk.edu.config.Config;
import bk.edu.data.model.ConditionInfo;
import bk.edu.data.model.SegmentInfo;
import bk.edu.utils.MySqlUtils;
import bk.edu.utils.SparkUtils;
import bk.edu.utils.TimeUtils;
import bk.edu.utils.TransformUtils;
import org.apache.spark.api.java.function.ForeachPartitionFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.storage.StorageLevel;

import java.io.Serializable;
import java.sql.*;
import java.util.Iterator;
import java.util.List;
import java.util.Date;

import static org.apache.spark.sql.functions.col;

public class SegmentAllUser implements Serializable {


    protected Long timeNow;

    public SegmentAllUser(){
        timeNow = System.currentTimeMillis();
    }
    public static void main(String args[]){
        SegmentAllUser segmentAllUser = new SegmentAllUser();
        boolean log = true;
        if(args[1].equals("false")){
            log = false;
        }
        if(args[0].equals("all")){
            SparkUtils sparkUtil = new SparkUtils("segment all user", log, true);
            segmentAllUser.process(sparkUtil, true);
        } else if (args[0].equals("new")) {
            SparkUtils sparkUtil = new SparkUtils("segment new, update segments", log, true);
            segmentAllUser.processNewSegment(sparkUtil);
        } else {
            SparkUtils sparkUtil = new SparkUtils("segment user updated", log, true);
            segmentAllUser.processUserUpdated(sparkUtil);
        }
    }

    private void processNewSegment(SparkUtils sparkUtil){
        MySqlUtils mySqlUtils = new MySqlUtils();
        List<SegmentInfo> segments = mySqlUtils.getNewSegment();


        Long timeStart = System.currentTimeMillis();
        Dataset<Row> df = sparkUtil.getTableDataframe("bookshop_customer");

        System.out.println("Number user process: " + df.count());
        df.show();
        Dataset<Row> finalDf = df.repartition(8);
        Long timeStart2 = System.currentTimeMillis();
        finalDf.persist(StorageLevel.MEMORY_ONLY());
        segments.forEach(segmentInfo -> {
            System.out.println("Process segment id : " + segmentInfo.getSegmentId());
            linkSegmentAndUser(segmentInfo, finalDf);
        });
        finalDf.unpersist();

        deletedAssociationBySegmentId(segments);

        System.out.println("Time process: " + (System.currentTimeMillis() - timeStart2) / 1000 + "s");
        System.out.println("Total time process: " + (System.currentTimeMillis() - timeStart) / 1000 + "S");

        mySqlUtils.close();
    }

    private void processUserUpdated(SparkUtils sparkUtils){
        long timeStart = System.currentTimeMillis();
        while (true){
            MySqlUtils mySqlUtils = new MySqlUtils();
            List<SegmentInfo> segments = mySqlUtils.getAllSegment();
            long timeEnd = System.currentTimeMillis();
            Dataset<Row> df = sparkUtils.getTableDataframe("bookshop_customer");
            df = df.filter(col("updated_at").$greater$eq(new Date(timeStart)))
                    .filter(col("updated_at").$less(new Date(timeEnd)));
            df.show();

            System.out.println("Number user process: " + df.count());
            Long timeStart1 = System.currentTimeMillis();
            Dataset<Row> finalDf = df.repartition(8);
            finalDf.persist(StorageLevel.MEMORY_ONLY());
            segments.forEach(segmentInfo -> {
                System.out.println(segmentInfo.getSegmentId());
                linkSegmentAndUser(segmentInfo, finalDf);
            });
            deleteAssociation(finalDf);
            finalDf.unpersist();

            System.out.println("Time process: " + (System.currentTimeMillis() - timeStart1) / 1000 + "s");
            System.out.println("Total time process: " + (System.currentTimeMillis() - timeEnd) / 1000 + "S");
            timeStart = timeEnd;
            try {
                Thread.sleep(60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("Interrupt by sleep");
            }
        }
    }
    private void process(SparkUtils sparkUtil, boolean processAll) {
        MySqlUtils mySqlUtils = new MySqlUtils();
        List<SegmentInfo> segments = mySqlUtils.getAllSegment();

        Dataset<Row> df = sparkUtil.getTableDataframe("bookshop_customer");
        if(!processAll) {
            Long time = System.currentTimeMillis() - TimeUtils.A_MINUTE_IN_MILLISECOND * 10;
            String filterEpr = "updated_at > '" + Config.FORMAT_DATETIME_SQL.format(new Date(time)) + "'";
            df = df.filter(filterEpr);
        }
        System.out.println("Number user process: " + df.count());
        df.show();
        Dataset<Row> finalDf = df.repartition(8);

        finalDf.persist(StorageLevel.MEMORY_ONLY());
        segments.forEach(segmentInfo -> {
            System.out.println(segmentInfo.getSegmentId());
            linkSegmentAndUser(segmentInfo, finalDf);
        });

        deleteAssociation(df);
        System.out.println(df.count());
        finalDf.unpersist();

        mySqlUtils.close();
    }

    public void deletedAssociationBySegmentId(List<SegmentInfo> segments){
        segments.forEach(segmentInfo -> {
            String selectSql = "SELECT * FROM cdp_segment_customer_association WHERE segment_id = ? ;";
            String deleteSql = "DELETE FROM cdp_segment_customer_association WHERE (`segment_id` = ? and `user_id` = ? );";
            MySqlUtils mySqlUtil = new MySqlUtils();
            Connection mysqlConnection = mySqlUtil.getConnection();
            try {
                PreparedStatement selectP = mysqlConnection.prepareStatement(selectSql);
                PreparedStatement deleteP = mysqlConnection.prepareStatement(deleteSql);

                deleteP.setInt(1, segmentInfo.getSegmentId());
                selectP.setInt(1, segmentInfo.getSegmentId());
                ResultSet rs = selectP.executeQuery();
                while (rs.next()){
                    Timestamp timestamp = rs.getTimestamp("updated_at");
                    if (timestamp.getTime() < timeNow){
                        int user_id = rs.getInt("user_id");
                        deleteP.setInt(2, user_id);
                        deleteP.executeUpdate();
                    }
                }
            } catch (Exception ignore) {
                ignore.printStackTrace();
            } finally {
                mySqlUtil.close();
            }
        });
    }

    public void deleteAssociation(Dataset<Row> df){
        df.foreachPartition(new ForeachPartitionFunction<Row>() {
            @Override
            public void call(Iterator<Row> rows) throws Exception {


            }
        });
    }

    private void linkSegmentAndUser(SegmentInfo segmentInfo, Dataset<Row> df){
        Dataset<Row> filterDf = df;
        List<ConditionInfo> conditions = segmentInfo.getConditions();
        if(conditions.size() == 0){
        } else {
            filterDf = TransformUtils.filterCondition(conditions.get(0), df);
            for(int i = 1; i < conditions.size(); i++){
                filterDf = TransformUtils.filterCondition(conditions.get(i), filterDf);
            }
        }
        filterDf.select("user_id", "gender", "short_hobbies", "long_hobbies", "birthday", "email").show();
        updateAssociation(segmentInfo.getSegmentId(), filterDf);
    }

    public void updateAssociation(int segmentId, Dataset<Row> df) {
        df.foreachPartition((ForeachPartitionFunction<Row>) rows -> {
            MySqlUtils mySqlUtils = new MySqlUtils();
            Connection mysqlConnection = mySqlUtils.getConnection();
            String selectSql = "SELECT * FROM cdp_segment_customer_association where segment_id = ? and user_id = ? ;";
            String insertSql = "INSERT INTO cdp_segment_customer_association (`segment_id`, `user_id`, `updated_at`) VALUES (?, ?, ?);";
            String updateSql = "UPDATE cdp_segment_customer_association SET updated_at = ? WHERE (`segment_id` = ?) and (`user_id` = ?);";
            try {
                PreparedStatement selectP = mysqlConnection.prepareStatement(selectSql);
                PreparedStatement insertP = mysqlConnection.prepareStatement(insertSql);
                PreparedStatement updateP = mysqlConnection.prepareStatement(updateSql);
                Long time = System.currentTimeMillis();
                Timestamp timestamp = new Timestamp(time);
                ResultSet selectRs;
                while (rows.hasNext()){
                    Row row = rows.next();
                    int userId = row.getInt(0);
                    selectP.setInt(1, segmentId);
                    selectP.setInt(2, userId);
                    selectRs = selectP.executeQuery();
                    if (selectRs.next()){
                        updateP.setTimestamp(1, timestamp);
                        updateP.setInt(2, segmentId);
                        updateP.setInt(3, userId);
                        if(!updateP.execute()){
                            System.out.println("False");
                        }
                    } else {
                        insertP.setInt(1, segmentId);
                        insertP.setInt(2, userId);
                        insertP.setTimestamp(3, timestamp);
                        if(!insertP.execute()){
                            System.out.println("False");
                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                mysqlConnection.close();
            }
        });
    }
}
