package service;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.SecureRandom;

/**
 * @title  : PBES 파일 암호화 컴포넌트 클래스
 * @author : jaeha-dev (Git)
 */
@Component
public class FilePBESEncoder extends AbstractFileEncoder {
    private final String PBES = "PBEWithMD5AndDES";

    /**
     * 파일 암호화
     */
    @Override
    boolean encryptFile(String filename, String extName, String key) throws Exception {
        try {
            // plain.ext -> plain.des (생성)
            FileInputStream inputFile = new FileInputStream(filename); // 원본 파일 이름
            FileOutputStream outputFile = new FileOutputStream(filename.substring(0, filename.lastIndexOf('.')) + ".enc"); // 확장자를 제외한 파일 이름 + .enc

            // Salt & SecretKey 생성, Cipher 초기화
            byte[] salt = generateSalt();
            SecretKey secretKey = generateSecretKey(key);
            Cipher cipher = initCipher(true, salt, secretKey); // Cipher.ENCRYPT_MODE

            // 파일 쓰기 & IO 스트림 닫기
            outputFile.write(salt);
            writeAndCloseFile(cipher, inputFile, outputFile);
            return true; // 파일 잠금 성공

        } catch (Exception e) {
            e.printStackTrace();
            return false; // 파일 잠금 실패
        }
    }

    /**
     * 파일 복호화
     */
    @Override
    boolean decryptFile(String filename, String extName, String key) throws Exception {
        try {
            // plain.des -> plain_dec.ext (생성)
            FileInputStream inputFile = new FileInputStream(filename); // 원본 파일 이름
            FileOutputStream outputFile = new FileOutputStream(filename.substring(0, filename.lastIndexOf('.')) + "_dec." + extName); // 확장자를 제외한 파일 이름 + _dec.ext

            // SecretKey 생성, Salt & Cipher 초기화
            byte[] salt = new byte[8];
            inputFile.read(salt); // Salt 를 생성하지 않고, 이 위치에서 read 하는 것에 주의.
            SecretKey secretKey = generateSecretKey(key);
            Cipher cipher = initCipher(false, salt, secretKey); // Cipher.DECRYPT_MODE

            // 파일 쓰기 & IO 스트림 닫기
            writeAndCloseFile(cipher, inputFile, outputFile);
            return true; // 파일 잠금 해제 성공

        } catch (Exception e) {
            e.printStackTrace();
            return false; // 파일 잠금 해제 실패
        }
    }

    /**
     * 파일 쓰기 & IO 스트림 닫기
     */
    @Override
    void writeAndCloseFile(Cipher cipher, FileInputStream inputFile, FileOutputStream outputFile) throws Exception {
        byte[] input = new byte[64]; // 32비트 || 64비트
        int byteRead;

        while ((byteRead = inputFile.read(input)) != -1) {
            byte[] output = cipher.update(input, 0, byteRead);
            if (output != null) outputFile.write(output);
        }

        byte[] output = cipher.doFinal();
        if (output != null) outputFile.write(output);

        // 파일 IO 스트림 닫기
        inputFile.close();
        outputFile.flush();
        outputFile.close();
    }

    /**
     * Salt 생성 (파일 잠금만)
     */
    private byte[] generateSalt() {
        byte[] salt = new byte[8];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(salt); // Salt (8 byte 랜덤 생성)
        return salt;
    }

    /**
     * SecretKey 생성
     */
    private SecretKey generateSecretKey(String key) throws Exception {
        PBEKeySpec keySpec = new PBEKeySpec(key.toCharArray()); // keySpec 지정 (사용자 입력 키 값)
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(PBES);
        return keyFactory.generateSecret(keySpec);
    }

    /**
     * Cipher 초기화
     */
    private Cipher initCipher(boolean isEncryptMode, byte[] salt, SecretKey secretKey) throws Exception {
        PBEParameterSpec pbeParameterSpec = new PBEParameterSpec(salt, 1000);
        Cipher cipher = Cipher.getInstance(PBES);

        if (isEncryptMode) {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, pbeParameterSpec);
        } else {
            cipher.init(Cipher.DECRYPT_MODE, secretKey, pbeParameterSpec);
        }
        return cipher;
    }
}