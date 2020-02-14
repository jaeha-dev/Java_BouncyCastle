package main;

import dto.User;
import java.util.Scanner;
import static main.Init.*;

/**
 * @title  : 메인 클래스
 * @author : jaeha-dev (Git)
 */
public class Main {
    private static Scanner scanner = new Scanner(System.in);

    /**
     * 메인 스레드
     */
    public static void main(String[] args) throws Exception {
        while (true) {
            // 서비스 및 DB 초기화
            initVariable();
            // 로그인 메뉴 출력
            printLoginMenu();
            String chooseNum = scanner.next();
            if (chooseNum.equals("0")) break;
        }
    }

    /**
     * 메뉴 입력 오류
     */
    private static void printChooseNumError(String chooseNum) {
        System.out.println("잘못된 코드(" + chooseNum + ") 입력입니다. 다시 입력하세요.");
    }

    /**
     * 로그인 메뉴
     */
    private static void printLoginMenu() throws Exception {
        System.out.println("\n*------------------------------* [로그인 메뉴] *------------------------------*");
        System.out.println("1. 계정 등록");
        System.out.println("2. 계정 로그인");
        System.out.println("3. 계정 비밀번호 초기화");
        System.out.println("4. 계정 탈퇴");
        System.out.println("*-------------------------------* [종료 메뉴] *-------------------------------*");
        System.out.println("0. 프로그램 종료");
        System.out.print("-> 메뉴번호 입력: ");
        String chooseNum = scanner.next();

        switch (chooseNum) {
            case "0":
                System.exit(0);
                break;
            case "1":
                if (userService.register(scanner)) {
                    System.out.println("+ 계정 등록에 성공하였습니다. +");
                } else {
                    System.out.println("+ 계정 등록에 실패하였습니다. +");
                }
                printLoginMenu(); // 로그인 메뉴 호출
                break;
            case "2":
                User user = userService.login(scanner);
                if (user != null) {
                    System.out.println("+ 로그인에 성공하였습니다. +");
                    printMainMenu(user); // 메인 메뉴 호출
                } else {
                    System.out.println("+ 로그인에 실패하였습니다. +");
                    printLoginMenu(); // 로그인 메뉴 호출
                }
                break;
            case "3":
                if (userService.certificate(scanner)) {
                    System.out.println("+ 인증 번호 확인에 성공하였습니다. +");
                    if (userService.editPassword(scanner)) {
                        System.out.println("+ 비밀번호가 수정되었습니다. 수정된 비밀번호로 로그인하세요. +");
                    } else {
                        System.out.println("+ 비밀번호 수정에 실패하였습니다. +");
                    }
                } else {
                    System.out.println("+ 인증 번호 확인에 실패하였습니다. +");
                }
                printLoginMenu(); // 로그인 메뉴 호출
                break;
            case "4":
                if (userService.unregister(scanner)) {
                    System.out.println("+ 계정 탈퇴에 성공하였습니다. +");
                } else {
                    System.out.println("+ 계정 탈퇴에 실패하였습니다. +");
                }
                printLoginMenu(); // 로그인 메뉴 호출
                break;
            default:
                printChooseNumError(chooseNum); // 에러 코드
                break;
        }
    }

    /**
     * 메인 메뉴
     */
    private static void printMainMenu(User user) throws Exception {
        System.out.println("\n*------------------------------* [로그인 계정] *------------------------------*");
        System.out.println("+ 이메일: " + user.getEmail());
        System.out.println("*-------------------------------* [메인 메뉴] *-------------------------------*");
        System.out.println("1. PBES 파일 암/복호화 메뉴"); //
        System.out.println("2. RSA 파일 암/복호화 메뉴"); //
        System.out.println("*-------------------------------* [종료 메뉴] *-------------------------------*");
        System.out.println("3. 계정 로그아웃");
        System.out.println("0. 프로그램 종료");
        System.out.print("-> 메뉴번호 입력: ");
        String chooseNum = scanner.next();

        switch (chooseNum) {
            case "0":
                System.exit(0);
                break;
            case "1":
                printPBESFileMenu(user);
                break; // PBES 파일 암/복호화 메뉴 호출
            case "2":
                printRSAFileMenu(user);
                break; // RSA 파일 암/복호화 메뉴 호출
            case "3":
                printLoginMenu();
                break; // 로그인 메뉴 호출
            default:
                printChooseNumError(chooseNum); // 에러 코드
                break;
        }
    }

    /**
     * PBES 파일 암/복호화 메뉴
     */
    private static void printPBESFileMenu(User user) throws Exception {
        System.out.println("\n*------------------------------* [로그인 계정] *------------------------------*");
        System.out.println("+ 이메일: " + user.getEmail());
        System.out.println("*--------------------------* [PBES 파일 암/복호화] *--------------------------*");
        System.out.println("1. 파일 암호화"); //
        System.out.println("2. 파일 복호화"); //
        System.out.println("*-------------------------------* [종료 메뉴] *-------------------------------*");
        System.out.println("3. 메인 메뉴");
        System.out.println("0. 프로그램 종료");
        System.out.print("-> 메뉴번호 입력: ");
        String chooseNum = scanner.next();

        switch (chooseNum) {
            case "0":
                System.exit(0);
                break;
            case "1":
                if (fileService.encryptFile(scanner)) {
                    System.out.println("+ 파일 암호화에 성공하였습니다. +");
                } else {
                    System.out.println("+ 파일 암호화에 실패하였습니다. +");
                }
                printPBESFileMenu(user); // PBES 파일 암/복호화 메뉴 호출
                break;
            case "2":
                if (fileService.decryptFile(scanner)) {
                    System.out.println("+ 파일 복호화에 성공하였습니다. +");
                } else {
                    System.out.println("+ 파일 복호화에 실패하였습니다. +");
                }
                printPBESFileMenu(user); // PBES 파일 암/복호화 메뉴 호출
                break;
            case "3":
                printMainMenu(user); // 메인 메뉴 호출
                break;
            default:
                printChooseNumError(chooseNum); // 에러 코드
                break;
        }
    }

    /**
     * RSA 파일 암/복호화 메뉴
     */
    private static void printRSAFileMenu(User user) throws Exception {
        System.out.println("\n*------------------------------* [로그인 계정] *------------------------------*");
        System.out.println("+ 이메일: " + user.getEmail());
        System.out.println("*--------------------------* [RSA 파일 암/복호화] *--------------------------*");
        System.out.println("1. 키페어 발급"); //
        System.out.println("2. 파일 암호화"); //
        System.out.println("3. 파일 복호화"); //
        System.out.println("*-------------------------------* [종료 메뉴] *-------------------------------*");
        System.out.println("4. 메인 메뉴");
        System.out.println("0. 프로그램 종료");
        System.out.print("-> 메뉴번호 입력: ");
        String chooseNum = scanner.next();

        switch (chooseNum) {
            case "0":
                System.exit(0);
                break;
            case "1":
                if (keyService.generateRSAKeyPair(user)) {
                    System.out.println("+ 키페어 발급에 성공하였습니다. +");
                } else {
                    System.out.println("+ 키페어 발급에 실패하였습니다. +");
                }
                printRSAFileMenu(user); // RSA 파일 암/복호화 메뉴 호출
                break;
            case "2":
                if (fileService.encryptRSAFile(scanner)) {
                    System.out.println("+ 파일 암호화에 성공하였습니다. +");
                } else {
                    System.out.println("+ 파일 암호화에 실패하였습니다. +");
                }
                printRSAFileMenu(user); // RSA 파일 암/복호화 메뉴 호출
                break;
            case "3":
                if (fileService.decryptRSAFile(scanner)) {
                    System.out.println("+ 파일 복호화에 성공하였습니다. +");
                } else {
                    System.out.println("+ 파일 복호화에 실패하였습니다. +");
                }
                printRSAFileMenu(user); // RSA 파일 암/복호화 메뉴 호출
                break;
            case "4":
                printMainMenu(user); // 메인 메뉴 호출
                break;
            default:
                printChooseNumError(chooseNum); // 에러 코드
                break;
        }
    }
}