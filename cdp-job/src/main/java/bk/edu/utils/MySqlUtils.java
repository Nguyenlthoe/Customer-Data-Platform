package bk.edu.utils;

import bk.edu.config.Config;
import bk.edu.data.model.ConditionInfo;
import bk.edu.data.model.SegmentInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mifmif.common.regex.Generex;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MySqlUtils {

    private Connection mysqlConnection;

    public MySqlUtils() {
        try {
            mysqlConnection = DriverManager.getConnection("jdbc:mysql://" + Config.MYSQL.HOST + "/" +
                    Config.MYSQL.DBNAME,
                    Config.MYSQL.USER, Config.MYSQL.PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection(){
        if(mysqlConnection == null){
            try {
                mysqlConnection = DriverManager.getConnection("jdbc:mysql://" + Config.MYSQL.HOST + "/" +
                        Config.MYSQL.DBNAME,
                    Config.MYSQL.USER, Config.MYSQL.PASSWORD);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return this.mysqlConnection;
    }

    public Set<Integer> getListUserUpdate(){
        Set<Integer> userIds = new HashSet<>();
        String sql = "SELECT * FROM bookshop_customer_category_association where updated_at <= ?";
        try {
            PreparedStatement preparedStatement = mysqlConnection.prepareStatement(sql);
            preparedStatement.setTimestamp(1, new Timestamp(TimeUtils.getDayBefore(Config.DATE_SHORT_HOBBY)));
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                int userId = rs.getInt("user_id");
                userIds.add(userId);
            }
        } catch (SQLException e) {

        }
        return userIds;
    }

    public Set<Integer> getListUser(){
        Set<Integer> userIds = new HashSet<>();
        String sql = "SELECT * FROM bookshop_customer";
        try {
            PreparedStatement preparedStatement = mysqlConnection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                int userId = rs.getInt("user_id");
                userIds.add(userId);
            }
        } catch (SQLException e) {

        }
        return userIds;
    }

    public void updateEmail(){
        String sql = "SELECT * FROM bookshop_customer where email like '%ỳ%'";
        String updateSql = "UPDATE `bookshop_customer` SET `email` = ? WHERE (`user_id` = ? );";
        try {
            PreparedStatement preparedStatement = mysqlConnection.prepareStatement(sql);
            PreparedStatement prepareStatement2 = mysqlConnection.prepareStatement(updateSql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                int userId = rs.getInt("user_id");
                String email = rs.getString("email");
                email = email.replaceAll("ỳ", "y");
                prepareStatement2.setString(1, email);
                prepareStatement2.setInt(2, userId);
                prepareStatement2.executeUpdate();
            }
        } catch (SQLException e) {

        }
    }

    public void deleteOldHobby(){
        String sql = "DELETE FROM cdp_customer_category_association where updated_at <= ?";
        try {
            PreparedStatement preparedStatement = mysqlConnection.prepareStatement(sql);
            preparedStatement.setTimestamp(1, new Timestamp(TimeUtils.getDayBefore(Config.DATE_SHORT_HOBBY)));
            preparedStatement.executeUpdate();

            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
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
                int count = rs.getInt("count");
                int sum = rs.getInt("sum");
                insertCustomerBookAvg(userId, count, sum);
            }
        } catch (SQLException e) {

        }
    }

    public void updateUpdatedTime(int userId){
        String sql = "UPDATE `customer-data-platform`.`bookshop_customer` SET `updated_at` = ? WHERE (`user_id` = ? );\n";
        try {
            PreparedStatement preparedStatement = mysqlConnection.prepareStatement(sql);
            preparedStatement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setInt(2, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException ignore) {

        }
    }

    public void insertCustomerBookAvg(int userId, int count, int sum){
        String sql = "UPDATE `customer-data-platform`.`bookshop_customer` SET `total_book_view` = ? , `avg_book_value_view` = ? , `total_book_value_view` = ? WHERE (`user_id` = ? );\n";
        try {
            PreparedStatement preparedStatement = mysqlConnection.prepareStatement(sql);
            preparedStatement.setInt(1, count);
            double avg = sum * 1.0 / count;
            preparedStatement.setDouble(2, avg);
            preparedStatement.setInt(3, sum);
            preparedStatement.setInt(4, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException ignore) {

        }
    }

    public void updateCustomerCategory(int userId, int categoryId, PreparedStatement preparedStatement){
        try {
            preparedStatement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setInt(2, userId);
            preparedStatement.setInt(3, categoryId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Update failed");
        }
    }
    public boolean checkExistCustomerCategory(int userId, int categoryId, PreparedStatement preparedStatement){
        try {
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
    public void insertCustomerCategory(int userId, int categoryId, PreparedStatement preparedStatement){
        try {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, categoryId);
            preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Insert failed");
        }
    }

    public List<SegmentInfo> getNewSegment() {
        List<SegmentInfo> segments = new ArrayList<>();
        String sql = "SELECT segment_id, rule FROM cdp_segment where is_deleted = 0 and `status` = 0;";
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
            mysqlConnection = null;
            System.out.println("close mysql");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateShortHobbies(List<Integer> userIds) {
        String selectSql = "SELECT * FROM cdp_customer_category_association where user_id = ? ;\n";
        String updateSql = "UPDATE `bookshop_customer` SET `short_hobbies` = ? WHERE (`user_id` = ? );\n";
        try {
            PreparedStatement preparedStatementUpdate = mysqlConnection.prepareStatement(updateSql);
            PreparedStatement preparedStatementSelect = mysqlConnection.prepareStatement(selectSql);

            userIds.forEach(userId -> {
                try {
                    preparedStatementSelect.setInt(1, userId);
                    ResultSet rs = preparedStatementSelect.executeQuery();
                    List<Integer> categoryIds = new ArrayList<>();
                    while (rs.next()){
                        categoryIds.add(rs.getInt("category_id"));
                    }
                    String categoryShort =" " + StringUtils.join(categoryIds, " , ") + " ";
                    preparedStatementUpdate.setString(1, categoryShort);
                    preparedStatementUpdate.setInt(2, userId);
                    preparedStatementUpdate.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (SQLException e) {
            System.err.println("Insert failed");
        }
    }

    public void updateShortHobbies(int userId) {
        String selectSql = "SELECT * FROM cdp_customer_category_association where user_id = ? ;\n";
        String updateSql = "UPDATE `bookshop_customer` SET `short_hobbies` = ? WHERE (`user_id` = ? );\n";
        try {
            PreparedStatement preparedStatementUpdate = mysqlConnection.prepareStatement(updateSql);
            PreparedStatement preparedStatementSelect = mysqlConnection.prepareStatement(selectSql);

            preparedStatementSelect.setInt(1, userId);
            ResultSet rs = preparedStatementSelect.executeQuery();
            List<Integer> categoryIds = new ArrayList<>();
            while (rs.next()){
                categoryIds.add(rs.getInt("category_id"));
            }
            String categoryShort =" " + StringUtils.join(categoryIds, " , ") + " ";
            preparedStatementUpdate.setString(1, categoryShort);
            preparedStatementUpdate.setInt(2, userId);
            preparedStatementUpdate.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Insert failed");
        }
    }

    public void insertLongHobbies(Integer userId, String longHobbies) {
        String updateSql = "UPDATE `bookshop_customer` SET `long_hobbies` = ? WHERE (`user_id` = ? );\n";
        try {
            PreparedStatement preparedStatementUpdate = mysqlConnection.prepareStatement(updateSql);

            preparedStatementUpdate.setString(1, longHobbies);
            preparedStatementUpdate.setInt(2, userId);
            preparedStatementUpdate.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Insert failed");
        }
    }

    public void updateNumberPhone(Set<Integer> users) {
        String updateSql = "UPDATE `bookshop_customer` SET `phone_number` = ? WHERE (`user_id` = ? );\n";
        try {
            PreparedStatement preparedStatementUpdate = mysqlConnection.prepareStatement(updateSql);
            Generex generex = new Generex("(0)(3[2-9]|5[6|8|9]|7[0|6-9]|8[0-6|8|9]|9[0-4|6-9])[0-9]{7}");

            generex.getMatchedString(2);
            users.forEach(userId -> {
                try {

                    String phoneNumber = generex.random();
                    while (phoneNumber.contains("|")){
                        phoneNumber = generex.random();
                    }
                    preparedStatementUpdate.setString(1, phoneNumber);
                    preparedStatementUpdate.setInt(2, userId);
                    preparedStatementUpdate.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (SQLException e) {
            System.err.println("Insert failed");
        }
    }

    public void updateStatusSegment(List<SegmentInfo> segments) {
        String updateSql = "UPDATE `cdp_segment` SET `status` = 1 WHERE (`segment_id` = ? );\n";
        try {
            PreparedStatement preparedStatementUpdate = mysqlConnection.prepareStatement(updateSql);
            segments.forEach(segmentInfo -> {
                try {
                    preparedStatementUpdate.setInt(1, segmentInfo.getSegmentId());
                    preparedStatementUpdate.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.err.println("update failed");
                }
            });

        } catch (SQLException e) {
        }
    }
}
