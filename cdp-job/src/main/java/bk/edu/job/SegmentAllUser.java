package bk.edu.job;

import bk.edu.config.Config;
import bk.edu.data.model.ConditionInfo;
import bk.edu.data.model.SegmentInfo;
import bk.edu.utils.MySqlUtils;
import bk.edu.utils.SparkUtils;
import bk.edu.utils.TimeUtils;
import bk.edu.utils.TransformUtils;
import org.apache.ivy.ant.IvyExtractFromSources;
import org.apache.spark.api.java.function.ForeachPartitionFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.storage.StorageLevel;

import java.io.Serializable;
import java.sql.*;
import java.util.Iterator;
import java.util.List;

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
            MySqlUtils mySqlUtils = new MySqlUtils();
            List<SegmentInfo> segments = mySqlUtils.getNewSegment(Integer.parseInt(args[2]));
            mySqlUtils.close();
            if(segments.isEmpty()){
                System.out.println("No segment updated");
                return;
            }
            SparkUtils sparkUtil = new SparkUtils("segment new, update segments", log, true);
            segmentAllUser.processNewSegment(sparkUtil, segments);
        } else {
            SparkUtils sparkUtil = new SparkUtils("segment user updated", log, true);
            int duration = Integer.parseInt(args[2]);
            segmentAllUser.processUserUpdated(sparkUtil, duration);
        }
    }

    private void processNewSegment(SparkUtils sparkUtil, List<SegmentInfo> segments){

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
        System.out.println("Total time process: " + (System.currentTimeMillis() - timeStart) / 1000 + "s");
    }

    private void processUserUpdated(SparkUtils sparkUtils, Integer duration){
        long timeStart = System.currentTimeMillis();
        while (true){
            MySqlUtils mySqlUtils = new MySqlUtils();
            List<SegmentInfo> segments = mySqlUtils.getAllSegment();
            long timeEnd = System.currentTimeMillis();
            Dataset<Row> df = sparkUtils.getTableDataframe("bookshop_customer").repartition(8);
            System.out.println(timeStart);
            df = df.filter(col("updated_at").$greater$eq(new Timestamp(timeStart)));
            df.show();

            System.out.println("Number user process: " + df.count());
            Long timeStart1 = System.currentTimeMillis();
            Dataset<Row> finalDf = df;
            finalDf.persist(StorageLevel.MEMORY_ONLY());
            segments.forEach(segmentInfo -> {
                System.out.println(segmentInfo.getSegmentId());
                linkSegmentAndUser(segmentInfo, finalDf);
            });
            deleteAssociation(finalDf, timeStart);
            finalDf.unpersist();

            System.out.println("Time process: " + (System.currentTimeMillis() - timeStart1) / 1000 + "s");
            System.out.println("Total time process: " + (System.currentTimeMillis() - timeEnd) / 1000 + "S");
            timeStart = timeEnd;
            try {
                Thread.sleep(duration * 1000);
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

        deleteAssociation(df, timeNow);
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

    public void deleteAssociation(Dataset<Row> df, long timeEnd){
        df.foreachPartition((ForeachPartitionFunction<Row>) rows -> {
            String selectSql = "SELECT * FROM cdp_segment_customer_association WHERE user_id = ? ;";
            String deleteSql = "DELETE FROM cdp_segment_customer_association WHERE (`user_id` = ? and `segment_id` = ? );";
            MySqlUtils mySqlUtil = new MySqlUtils();
            Connection mysqlConnection = mySqlUtil.getConnection();
            PreparedStatement selectP = mysqlConnection.prepareStatement(selectSql);
            PreparedStatement deleteP = mysqlConnection.prepareStatement(deleteSql);
            while (rows.hasNext()){
                Row row = rows.next();
                int user_id = row.getInt(0);

                deleteP.setInt(1, user_id);
                selectP.setInt(1, user_id);
                ResultSet rs = selectP.executeQuery();
                while (rs.next()){
                    Timestamp timestamp = rs.getTimestamp("updated_at");
                    if (timestamp.getTime() < timeEnd){
                        int segment_id = rs.getInt("segment_id");
                        deleteP.setInt(2, segment_id);
                        deleteP.executeUpdate();
                    }
                }

            }
            mysqlConnection.close();
        });
    }

    private void linkSegmentAndUser(SegmentInfo segmentInfo, Dataset<Row> df){
        Dataset<Row> filterDf = df;
        List<ConditionInfo> conditions = segmentInfo.getConditions();
//        if(conditions.size() == 0){
//        } else {
//            filterDf = TransformUtils.filterCondition(conditions.get(0), df);
            for(int i = 0; i < conditions.size(); i++){
                try {
                    filterDf = TransformUtils.filterCondition(conditions.get(i), filterDf);
                } catch (Exception ignore){

                }
            }
//        }
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
