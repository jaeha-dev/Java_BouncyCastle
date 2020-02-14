package service;

import dto.Pem;
import dto.User;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.FileOutputStream;
import java.security.*;

import static global.Constant.*;

/**
 * @title  : 키 서비스 클래스
 * @author : jaeha-dev (Git)
 */
@Service
public class KeyService {

    /**
     * 대칭(비밀)키 생성 및 발급
     */
    public boolean generateSecretKey(User user) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        SecretKey secretKey = keyGenerator.generateKey();

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] key = cipher.doFinal(user.getPassword().getBytes(CHARSET)); // 키 생성에 계정 비밀번호를 활용한다.

        FileOutputStream outputFile;
        try {
            outputFile = new FileOutputStream(user.getEmail() + "-" + SECRET_KEY);
            outputFile.write(key);
            outputFile.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 키페어 생성 (테스트 통과)
     */
    public boolean generateRSAKeyPair(User user) throws Exception {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(1024);
            KeyPair keyPair = generator.generateKeyPair();

            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            writePemFile(publicKey, "RSA PUBLIC KEY", user.getEmail() + "-" + RSA_PUBLIC_KEY);
            writePemFile(privateKey, "RSA PRIVATE KEY", user.getEmail() + "-" + RSA_PRIVATE_KEY);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 키페어 발급 (테스트 통과)
     */
    private void writePemFile(Key key, String description, String filename) throws Exception {
        Pem pemFile = new Pem(key, description);
        pemFile.write(filename);
    }
}