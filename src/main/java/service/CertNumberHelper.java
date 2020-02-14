package service;

import org.springframework.stereotype.Component;
import java.security.SecureRandom;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @title  : 인증 번호 컴포넌트 클래스
 * @author : jaeha-dev (Git)
 */
@Component
public class CertNumberHelper {
    private String tempCertNumber = "";

    /**
     * 인증 번호 생성
     */
    String generateCertNumber() {
        final int numberSize = 5;
        SecureRandom random = new SecureRandom();
        StringBuffer number = new StringBuffer();

        for (int i = 0; i < numberSize; i++) {
            number.append(random.nextInt(10));
        }
        return number.toString();
    }

    /**
     * 인증 번호 입력
     */
    boolean inputCertNumber(Scanner scanner, String certNumber, String message) {
        // 인증 번호 입력 및 타이머
        Timer timer = new Timer(); // 타이머 생성 (1)
        TimerTask task = new TimerTask() { // 30초 후, 실행 (3)
            @Override
            public void run() {
                System.out.println(message); // 시간 초과
                tempCertNumber = generateCertNumber(); // 인증 번호 초기화
            }
        };
        timer.schedule(task, 30*1000); // 30초 타이머 시작 (2)

        tempCertNumber = certNumber; // 생성된 인증 번호를 임시 인증 번호에 저장한다.
        String inputNumber = scanner.next(); // 사용자 입력 번호

        if (inputNumber.isEmpty()) {
            return false;
        } else {
            timer.cancel(); // 타이머 취소
            return tempCertNumber.equals(inputNumber); // 일치 여부 확인
        }
    }
}