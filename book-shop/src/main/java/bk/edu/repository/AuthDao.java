package bk.edu.repository;

import bk.edu.data.entity.UserEntity;
import bk.edu.data.req.LoginRequest;
import bk.edu.exception.RequestInvalid;
import bk.edu.utils.FunctionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AuthDao {
    @Autowired
    JdbcTemplate jdbcTemplate;
    public UserEntity getUser(LoginRequest loginRequest, boolean isAdmin) {
        UserEntity userEntity = new UserEntity();
        String passwordHash = FunctionUtils.hashPassword(loginRequest.getPassword());
        String tableName = isAdmin ? "cdp_admin" : "bookshop_customer";
        String sql = String.format("SELECT * FROM `%s` where email = ? and password = ? ;", tableName);

        List<Map<String, Object>> rs = jdbcTemplate.queryForList(sql, loginRequest.getEmail(), passwordHash);
        if(rs.size() != 0){
            Map<String, Object> map = rs.get(0);
            userEntity.setUserId((Integer) map.get("user_id"));
            userEntity.setEmail((String) map.get("email"));
            userEntity.setPhoneNumber((String)map.get("phone_number"));
            return userEntity;
        } else {
            throw new RequestInvalid("Email hoặc mật khẩu không đúng");
        }
    }
}
