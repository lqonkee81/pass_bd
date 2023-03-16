import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AES {
    Cipher cipher;
    SecretKey key;

    AES() throws NoSuchPaddingException, NoSuchAlgorithmException {
        cipher = Cipher.getInstance("AES");
        key = new SecretKeySpec("+)9N2/5iT(Jmn|M?".getBytes(), "AES");
    }

    private String encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    private byte[] decode(String data) {
        return Base64.getDecoder().decode(data);
    }

    public String encrypt(String value) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] bytes = cipher.doFinal(value.getBytes());

        return encode(bytes);
    }

    public String decrypt(String value) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] encryptedBytes = decode(value);
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] dec = cipher.doFinal(encryptedBytes);
        return dec.toString();
//        return new String(cipher.doFinal(dec));
    }
}
