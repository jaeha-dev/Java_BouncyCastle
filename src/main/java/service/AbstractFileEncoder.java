package service;

import javax.crypto.Cipher;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @title  : 파일 인코더 추상 클래스
 * @author : jaeha-dev (Git)
 */
abstract class AbstractFileEncoder {
    abstract boolean encryptFile(String filename, String extName, String key) throws Exception;
    abstract boolean decryptFile(String filename, String extName, String key) throws Exception;
    abstract void writeAndCloseFile(Cipher cipher, FileInputStream inputFile, FileOutputStream outputFile) throws Exception;
}