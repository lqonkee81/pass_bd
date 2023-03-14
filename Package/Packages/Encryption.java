/*
 * Шифрование пакетов
 *
 * Пока что не работает и лежит мертвым грузом
 * */

import javax.crypto.Cipher;
import java.io.*;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

public class Encryption {
    private PublicKey publicKey;
    private PrivateKey privateKey;

    public Encryption() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(1024);
            KeyPair pair = generator.generateKeyPair();
            privateKey = pair.getPrivate();
            publicKey = pair.getPublic();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] encrypt(Package p, PublicKey publicKey) throws Exception {
        byte[] bytes = obj2Byte(p);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(bytes);
        return encryptedBytes;
    }

    public Package decrypt(byte[] encryptedBytes, PrivateKey privateKey) throws Exception {
//        byte[] encryptedBytes = decode(encryptedLetter);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedLetter = cipher.doFinal(encryptedBytes);
        return (Package) byte2Obj(decryptedLetter);

        //        return new String(decryptedLetter, "UTF8");
    }

    private byte[] obj2Byte(Object myObject) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
        objStream.writeObject(myObject);
        return byteStream.toByteArray();
    }

    public Object byte2Obj(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objStream = new ObjectInputStream(byteStream);
        return objStream.readObject();
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }
}

