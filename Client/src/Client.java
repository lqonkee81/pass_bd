import java.io.*;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;

public class Client {
    static final int PORT = 10521;
    static Encryption enc = new Encryption();
    static PublicKey serverPublicKey;
    static PublicKey selfPublicKey;
    static PrivateKey selfPrivateKey;

    static Socket socket;

    public static void main(String[] args) {
        selfPrivateKey = enc.getPrivateKey();
        selfPublicKey = enc.getPublicKey();

        try {
            socket = new Socket(
                    "localhost",
                    PORT);

            System.out.println("Connected to server");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            exchangeKeys();
            registration();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static void exchangeKeys() throws IOException, ClassNotFoundException {
        ObjectOutputStream writer = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream reader = new ObjectInputStream(socket.getInputStream());
        writer.writeObject(selfPublicKey);
        writer.flush();

        serverPublicKey = (PublicKey) reader.readObject();
        System.out.println("Client public key: " + serverPublicKey);
    }

    static void registration() throws Exception {
        BufferedOutputStream writer = new BufferedOutputStream(socket.getOutputStream());
        BufferedInputStream reader = new BufferedInputStream(socket.getInputStream());

        RegistrationPackage pac = new RegistrationPackage(
                PackageType.REGISTRATION,
                "lqonkee81",
                "kke",
                "484",
                "lqonkee81@yandex.ru"
        );

        byte[] p = enc.encrypt(pac, serverPublicKey);

        writer.write(p);
        writer.flush();
    }
}