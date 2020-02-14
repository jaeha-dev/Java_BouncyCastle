package global;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @title  : 공통 상수 클래스
 * @author : jaeha-dev (Git)
 */
public class Constant {
    public static final Charset CHARSET = StandardCharsets.UTF_8;
    public static final String SECRET_KEY = "secret.key";
    public static final String RSA_PUBLIC_KEY = "public.pem";
    public static final String RSA_PRIVATE_KEY = "private.pem";
}