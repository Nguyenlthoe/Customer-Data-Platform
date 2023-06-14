package bk.edu.repository;

import bk.edu.exception.RequestInvalid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class MySqlDao {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public void updateBillInfo(int userId){
        String sql = "SELECT min(b.total) as min, sum(b.total) as sum, count(b.bill_id) as count " +
                "FROM bookshop_bill b inner join bookshop_customer c " +
                "on b.user_id = c.user_id where b.user_id = ?;";
        Map<String, Object> map = jdbcTemplate.queryForMap(sql, userId);
        String sqlUpdate = "UPDATE `bookshop_customer` SET `min_total_bill` = ? , `avg_bill_value` = ? WHERE (`user_id` = ?); ";
        int minTotal = (int) map.get("min");
        long sumTotal = ((BigDecimal) map.get("sum")).longValue();
        long count = (long) map.get("count");
        jdbcTemplate.update(sqlUpdate, minTotal, sumTotal * 1.0/ count, userId);
    }
}
