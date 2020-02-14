package dao;

import dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @title  : 계정 데이터 액세스 클래스
 * @author : jaeha-dev (Git)
 */
@Repository
public class UserDao {

    // Spring JDBC DML template method
    @Autowired private JdbcTemplate jdbcTemplate;

    /**
     * 계정 등록
     */
    public int insertUser(String email, String password) {
        final String query = "INSERT INTO user(email, password) VALUES(?, ?)";
        return jdbcTemplate.update(query, preparedStatement -> {
            preparedStatement.setObject(1, email);
            preparedStatement.setObject(2, password);
        });
    }

    /**
     * 계정 이메일 중복 검사
     */
    public int countUserEmail(String email) {
        final String query = "SELECT COUNT(*) FROM user WHERE email = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{email}, Integer.class);
    }

    /**
     * 계정 로그인
     */
    public User selectUser(String email) {
        final String query = "SELECT * FROM user WHERE email = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{email}, new UserMapper());
    }

    /**
     * 비밀번호 수정
     */
    public int updateUserPassword(String email, String password) {
        final String query = "UPDATE user SET password = ? WHERE email = ?";
        return jdbcTemplate.update(query, password, email);
    }

    /**
     * 계정 삭제
     */
    public int deleteUser(String email, String password) {
        final String query = "DELETE FROM user WHERE email = ? AND password = ?";
        return jdbcTemplate.update(query, email, password);
    }

    /**
     * Object-Row Mapper
     */
    private static final class UserMapper implements RowMapper<User> {
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            return user;
        }
    }
}