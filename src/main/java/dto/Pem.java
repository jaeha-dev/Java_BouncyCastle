package dto;

import lombok.NoArgsConstructor;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.security.Key;

/**
 * @title  : PEM 클래스
 * @author : jaeha-dev (Git)
 */
@NoArgsConstructor
public class Pem {
    private PemObject pemObject;

    public Pem(Key key, String description) {
        this.pemObject = new PemObject(description, key.getEncoded());
    }

    public void write(String filename) throws Exception {
        try (PemWriter pemWriter = new PemWriter(new OutputStreamWriter(new FileOutputStream(filename)))) {
            pemWriter.writeObject(this.pemObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}