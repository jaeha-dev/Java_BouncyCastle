package service;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

/**
 * @title  : 비밀번호 해싱 컴포넌트 클래스
 * @author : jaeha-dev (Git)
 */
@Component
public class PasswordEncoder {

    /**
     * 비밀번호 해싱
     */
    static String encodePassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(10));
    }

    /**
     * 평문, 암호문 비교
     */
    static boolean isPasswordMatch(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}