package bk.edu.job;

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

import java.sql.*;
import java.util.Iterator;
import java.util.List;
import java.util.Date;

public class SegmentAllUser {
    protected SparkUtils sparkUtil;

    public SegmentAllUser(){
        sparkUtil = new SparkUtils("segment all user", true, true);
    }
    public static void main(String args[]){
        SegmentAllUser segmentAllUser = new SegmentAllUser();
        segmentAllUser.process();
    }

    private void process() {
        MySqlUtils mySqlUtils = new MySqlUtils();
        List<SegmentInfo> segments = mySqlUtils.getAllSegment();
        mySqlUtils.close();

        Dataset<Row> df = sparkUtil.getTableDataframe("bookshop_customer");
        df.persist(StorageLevel.MEMORY_ONLY());
        df.show();
        segments.forEach(segmentInfo -> {
            System.out.println(segmentInfo.getSegmentId());
            linkSegmentAndUser(segmentInfo, df);
        });
        df.unpersist();
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
        filterDf.show();
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
                Long time = System.currentTimeMillis() + TimeUtils.A_HOUR_IN_MILLISECOND * 7;
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
