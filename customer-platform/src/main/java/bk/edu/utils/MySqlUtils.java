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

    private String host = "172.25.0.1:3306";

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
    public void updateBookRead(int userId){
        String sql = "SELECT count(cb.book_id) as count, sum(b.price) as sum FROM cdp_customer_book_association cb inner join bookshop_book b on cb.book_id = b.book_id where cb.user_id = ? ;";
        try {
            PreparedStatement preparedStatement = mysqlConnection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
            }
        } catch (SQLException e) {

        }
    }

    public void updateCustomerCategory(int userId, int categoryId){
        String sql = "UPDATE `cdp_customer_category_association` SET `updated_at` = ? WHERE (`user_id` = ? ) and (`category_id` = ? );\n";
        try {
            PreparedStatement preparedStatement = mysqlConnection.prepareStatement(sql);
            preparedStatement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setInt(2, userId);
            preparedStatement.setInt(3, categoryId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Update failed");
        }
    }
    public boolean checkExistCustomerCategory(int userId, int categoryId){
        String sql = "SELECT * FROM cdp_customer_category_association WHERE user_id = ? and category_id = ?";
        try {
            PreparedStatement preparedStatement = mysqlConnection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, categoryId);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                return true;
            }
        } catch (SQLException e) {
            return true;
        }
        return false;
    }
    public boolean checkExistCustomerBook(int userId, int bookId){
        String sql = "SELECT * FROM cdp_customer_book_association WHERE user_id = ? and book_id = ?";
        try {
            PreparedStatement preparedStatement = mysqlConnection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, bookId);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                return true;
            }
        } catch (SQLException e) {
            return true;
        }
        return false;
    }

    public void insertCustomerBook(int userId, int bookId){
        String sql = "INSERT INTO `cdp_customer_book_association` (`user_id`, `book_id`) VALUES ( ? , ? );\n";
        try {
            PreparedStatement preparedStatement = mysqlConnection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, bookId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Insert failed");
        }
    }
    public void insertCustomerCategory(int userId, int categoryId){
        String sql = "INSERT INTO `cdp_customer_category_association` (`user_id`, `category_id`, `updated_at`) VALUES ( ? , ? , ? );\n";
        try {
            PreparedStatement preparedStatement = mysqlConnection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, categoryId);
            preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Insert failed");
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
}
