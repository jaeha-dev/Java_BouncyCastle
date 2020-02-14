package service;

import org.springframework.stereotype.Service;

import java.util.Scanner;

import static main.Init.filePBESEncoder;
import static main.Init.fileRSAEncoder;

/**
 * @title  : 파일 서비스 클래스
 * @author : jaeha-dev (Git)
 */
@Service
public class FileService {

    /**
     * 파일 암호화
     */
    public boolean encryptFile(Scanner scanner) throws Exception {
        System.out.println("* 파일 암호화 *");
        System.out.println("+ 암호화 할 파일의 이름 입력하세요. (예시: pbes.txt) +");
        String filename = scanner.next();
        System.out.println("+ 암호화에 사용할 키를 입력하세요. +");
        String key = scanner.next();

        return filePBESEncoder.encryptFile(filename, "", key);
    }

    /**
     * 파일 복호화
     */
    public boolean decryptFile(Scanner scanner) throws Exception {
        System.out.println("* 파일 복호화 *");
        System.out.println("+ 복호화 할 파일의 이름을 입력하세요. (예시: pbes.enc) +");
        String filename = scanner.next();
        System.out.println("+ 복호화 될 파일의 확장자를 입력하세요 (예시: txt). +");
        String extName = scanner.next();
        System.out.println("+ 암호화에 사용한 키를 입력하세요. +");
        String key = scanner.next();

        return filePBESEncoder.decryptFile(filename, extName, key);
    }

    /**
     * 파일 암호화 (B가 A의 공개키로 암호화)
     */
    public boolean encryptRSAFile(Scanner scanner) throws Exception {
        System.out.println("* 파일 암호화 *");
        System.out.println("+ 수신자의 공개키 파일 이름을 입력하세요. (예시: test@test.com-public.pem) +");
        String keyName = scanner.next();
        System.out.println("+ 암호화 할 파일의 이름을 입력하세요. (예시: rsa.txt) +");
        String filename = scanner.next();

        return fileRSAEncoder.encryptFile(filename, "", keyName);
    }

    /**
     * 파일 복호화 (B가 B의 개인키로 복호화하여 원본 복구)
     */
    public boolean decryptRSAFile(Scanner scanner) throws Exception {
        System.out.println("* 파일 복호화 *");
        System.out.println("+ 수신자의 개인키 파일 이름을 입력하세요. (예시: test@test.com-private.pem) +");
        String keyName = scanner.next();
        System.out.println("+ 복호화 할 파일의 확장자를 입력하세요 (예시: txt). +");
        String extName = scanner.next();
        System.out.println("+ 복호화 할 파일의 이름을 입력하세요. (예시: rsa.enc) +");
        String filename = scanner.next();

        return fileRSAEncoder.decryptFile(filename, extName, keyName);
    }
}