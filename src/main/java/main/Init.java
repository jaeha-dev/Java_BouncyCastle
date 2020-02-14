package main;

import dao.UserDao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import service.*;

/**
 * @title  : 초기화 클래스
 * @author : jaeha-dev (Git)
 */
public class Init {
    // 계정 서비스
    public static UserDao userDao;
    public static UserService userService;
    public static PasswordEncoder passwordEncoder;
    public static CertNumberHelper certNumberHelper;
    // 파일 서비스
    public static FileService fileService;
    public static FilePBESEncoder filePBESEncoder;
    public static FileRSAEncoder fileRSAEncoder;
    // 키 서비스
    public static KeyService keyService;

    /**
     * 서비스 및 DAO 초기화
     */
    static void initVariable() {
        @SuppressWarnings("resource")
        ApplicationContext context = new ClassPathXmlApplicationContext("Spring.xml"); // Spring, Spring JDBC

        // 계정 서비스
        userDao = context.getBean("userDao", UserDao.class);
        userService = context.getBean("userService", UserService.class);
        passwordEncoder = context.getBean("passwordEncoder", PasswordEncoder.class);
        certNumberHelper = context.getBean("certNumberHelper", CertNumberHelper.class);
        // 파일 서비스
        fileService = context.getBean("fileService", FileService.class);
        filePBESEncoder = context.getBean("filePBESEncoder", FilePBESEncoder.class);
        fileRSAEncoder = context.getBean("fileRSAEncoder", FileRSAEncoder.class);
        // 키 서비스
        keyService = context.getBean("keyService", KeyService.class);
    }
}