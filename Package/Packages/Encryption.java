/*
 * Шифрование пакетов
 *
 * Пока что не работает и лежит мертвым грузом
 * */

import javax.crypto.*;
import java.io.*;
import java.security.*;

public class Encryption {
    private PublicKey publicKey;
    private PrivateKey privateKey;

    public Encryption() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(4096);
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
        return cipher.doFinal(bytes);
    }

    public Package decrypt(byte[] encryptedBytes, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedLetter = cipher.doFinal(encryptedBytes);
        return (Package) byte2Obj(decryptedLetter);
    }

    private byte[] obj2Byte(Object myObject) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
        objStream.writeObject(myObject);
        return byteStream.toByteArray();
    }

    private Object byte2Obj(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objStream = new ObjectInputStream(byteStream);
        return objStream.readObject();
    }


    public String makeHash(String pass) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");

        byte[] bytes = md5.digest(pass.getBytes());
        StringBuilder stringBuilder = new StringBuilder();

        for (byte b : bytes) {
            stringBuilder.append(String.format("%02X", b));
        }
        return stringBuilder.toString();
    }


    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }
}

