package bk.edu.utils;

import bk.edu.data.model.ConditionInfo;
import bk.edu.data.model.SegmentInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.spark.api.java.function.ForeachPartitionFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class MySqlUtils {
    private String user = "book_shop";

    private String password = "package1107N";

    private String host = "172.25.0.7:3306";

    private String dbName = "customer-data-platform";

    private Connection mysqlConnection;

    public MySqlUtils() {
        try {
            mysqlConnection = DriverManager.getConnection("jdbc:mysql://" + host + "/" + dbName, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection(){
        return this.mysqlConnection;
    }

    public void getTime(){
        String sql = "SELECT * FROM bookshop_customer where user_id = 1029 ;";
        try {
            PreparedStatement preparedStatement = mysqlConnection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            Timestamp timestamp = rs.getTimestamp("updated_at");
            System.out.println(timestamp.getTime());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public List<SegmentInfo> getAllSegment() {
        List<SegmentInfo> segments = new ArrayList<>();
        String sql = "SELECT segment_id, rule FROM cdp_segment where is_deleted = 0;";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            PreparedStatement preparedStatement = mysqlConnection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Integer segmentId = rs.getInt("segment_id");
                String rule = rs.getString("rule");
                List<ConditionInfo> conditions = objectMapper.readValue(rule, new TypeReference<List<ConditionInfo>>() {
                });
                segments.add(new SegmentInfo(segmentId, conditions));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return segments;
    }

    public void close() {
        try {
            mysqlConnection.close();
            System.out.println("close mysql");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<SegmentInfo> getNewSegment() {
        Long time = System.currentTimeMillis() - TimeUtils.A_MINUTE_IN_MILLISECOND * 11;
        Timestamp timestamp = new Timestamp(time);
        List<SegmentInfo> segments = new ArrayList<>();
        String sql = "SELECT segment_id, rule FROM cdp_segment where is_deleted = 0 and updated_at > ?;";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            PreparedStatement preparedStatement = mysqlConnection.prepareStatement(sql);
            preparedStatement.setTimestamp(1, timestamp);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Integer segmentId = rs.getInt("segment_id");
                String rule = rs.getString("rule");
                List<ConditionInfo> conditions = objectMapper.readValue(rule, new TypeReference<List<ConditionInfo>>() {
                });
                segments.add(new SegmentInfo(segmentId, conditions));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return segments;
    }
}
