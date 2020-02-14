package service;

import org.bouncycastle.util.encoders.Base64;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @title  : RSA 파일 암호화 컴포넌트 클래스
 * @author : jaeha-dev (Git)
 */
@Component
public class FileRSAEncoder extends AbstractFileEncoder {
    private final String RSA = "RSA/ECB/PKCS1Padding";
    private final String RSA_PUBLIC_BEGIN = "-----BEGIN RSA PUBLIC KEY-----";
    private final String RSA_PUBLIC_END = "-----END RSA PUBLIC KEY-----";
    private final String RSA_PRIVATE_BEGIN = "-----BEGIN RSA PRIVATE KEY-----";
    private final String RSA_PRIVATE_END = "-----END RSA PRIVATE KEY-----";

    /**
     * 파일 암호화
     */
    @Override
    boolean encryptFile(String filename, String extName, String key) throws Exception {
        try {
            FileInputStream inputFile = new FileInputStream(filename); // 원본 파일 이름
            FileOutputStream outputFile = new FileOutputStream(filename.substring(0, filename.lastIndexOf('.')) + ".enc"); // 확장자를 제외한 파일 이름 + .enc
            PublicKey publicKey = getPemPublicKey(key, "RSA"); // 공개키 파일 이름

            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            writeAndCloseFile(cipher, inputFile, outputFile);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 파일 복호화
     */
    @Override
    boolean decryptFile(String filename, String extName, String key) throws Exception {
        try {
            FileInputStream inputFile = new FileInputStream(filename); // 원본 파일 이름
            FileOutputStream outputFile = new FileOutputStream(filename.substring(0, filename.lastIndexOf('.')) + "_dec." + extName); // 확장자를 제외한 파일 이름 + _dec.ext
            PrivateKey privateKey = getPemPrivateKey(key, "RSA"); // 개인키 파일 이름

            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            writeAndCloseFile(cipher, inputFile, outputFile);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 파일 쓰기 & IO 스트림 닫기
     */
    @Override
    void writeAndCloseFile(Cipher cipher, FileInputStream inputFile, FileOutputStream outputFile) throws Exception {
        byte[] input = new byte[1024];
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
     * 키 파일 읽기
     */
    private byte[] getKeyBytes(String filename) throws Exception {
        File file = new File(filename);
        FileInputStream fis = new FileInputStream(file);
        DataInputStream dis = new DataInputStream(fis);
        byte[] keyBytes = new byte[(int) file.length()];
        dis.readFully(keyBytes);
        dis.close();

        return keyBytes;
    }

    /**
     * 공개키 읽기
     */
    private PublicKey getPemPublicKey(String filename, String algorithm) throws Exception {
        String temp = new String(getKeyBytes(filename));
        String publicKeyPEM = temp.replace(RSA_PUBLIC_BEGIN + System.getProperty("line.separator"), "");
        publicKeyPEM = publicKeyPEM.replace(RSA_PUBLIC_END, "");

        byte[] decoded = Base64.decode(publicKeyPEM);

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);

        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 개인키 읽기
     */
    private PrivateKey getPemPrivateKey(String filename, String algorithm) throws Exception {
        String temp = new String(getKeyBytes(filename));
        String privateKeyPEM = temp.replace(RSA_PRIVATE_BEGIN + System.getProperty("line.separator"), "");
        privateKeyPEM = privateKeyPEM.replace(RSA_PRIVATE_END, "");

        byte[] decoded = Base64.decode(privateKeyPEM);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);

        return keyFactory.generatePrivate(keySpec);
    }
}