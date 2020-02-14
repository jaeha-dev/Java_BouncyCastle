package service;

import dto.User;
import org.springframework.stereotype.Service;
import java.util.Scanner;
import static main.Init.certNumberHelper;
import static main.Init.userDao;
import static service.PasswordEncoder.encodePassword;
import static service.PasswordEncoder.isPasswordMatch;

/**
 * @title  : 계정 서비스 클래스
 * @author : jaeha-dev (Git)
 */
@Service
public class UserService {

    /**
     * 계정 등록
     */
    public boolean register(Scanner scanner) {
        System.out.println("* 계정 등록 *");
        System.out.println("+ 로그인에 사용할 이메일을 입력하세요. (최대 30자) +");
        String email = scanner.next();

        if (userDao.countUserEmail(email) != 0) {
            System.out.println("+ 이미 등록된 이메일입니다. +");
            return false;
        }

        System.out.println("+ 로그인에 비밀번호를 입력하세요. (최대 20자) +");
        String password = scanner.next();

        // 비밀번호 해싱
        return userDao.insertUser(email, encodePassword(password)) != 0;
    }

    /**
     * 계정 로그인
     */
    public User login(Scanner scanner) {
        System.out.println("* 계정 로그인 *");
        System.out.println("+ 이메일과 비밀번호를 공백 문자를 구분하여 입력하세요. (root 12345) +");
        String email = scanner.next();
        String password = scanner.next();

        User user = userDao.selectUser(email);

        // 평문, 암호문 비교
        if (isPasswordMatch(password, user.getPassword())) return user;
        else return null;
    }

    /**
     * 인증 번호 입력 및 확인
     */
    public boolean certificate(Scanner scanner) {
        System.out.println("* 인증 번호 확인 *");

        // 인증 번호 생성
        String certNumber = certNumberHelper.generateCertNumber();
        System.out.println("+ 인증 번호가 생성되었습니다. +");
        System.out.println("+ 생성된 5자리 인증 번호를 30초 이내에 입력해주세요. (" + certNumber + ")");

        // 인증 번호 입력 및 타이머
        return certNumberHelper.inputCertNumber(scanner, certNumber, "+ 입력 시간을 초과하여 인증 번호가 초기화되었습니다. +");
    }

    /**
     * 비밀번호 수정
     */
    public boolean editPassword(Scanner scanner) {
        System.out.println("* 비밀번호 수정 *");
        System.out.println("+ 이메일을 입력하세요. (최대 30자) +");
        String email = scanner.next();

        if (userDao.countUserEmail(email) != 0) {
            System.out.println("+ 새로운 비밀번호를 입력하세요. (최대 20자) +");
            String password = scanner.next();

            // 비밀번호 해싱
            return userDao.updateUserPassword(email, encodePassword(password)) != 0;
        } else {
            System.out.println("+ 해당 이메일로 등록된 계정이 없습니다. +");
            return false;
        }
    }

    /**
     * 계정 탈퇴
     */
    public boolean unregister(Scanner scanner) {
        System.out.println("* 계정 탈퇴 *");
        System.out.println("+ 이메일과 비밀번호를 공백 문자를 구분하여 입력하세요. (root 12345) +");
        String email = scanner.next();
        String password = scanner.next();

        User user = userDao.selectUser(email);

        // 평문, 암호문 비교
        if (isPasswordMatch(password, user.getPassword())) {
            return userDao.deleteUser(email, user.getPassword()) != 0;
        } else {
            return false;
        }
    }
}