package dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;

/**
 * @title  : 계정 모델 클래스
 * @author : jaeha-dev (Git)
 */
@Getter
@Setter
@NoArgsConstructor
public class User implements Serializable {
    private Long id;
    private String email;
    private String password;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}