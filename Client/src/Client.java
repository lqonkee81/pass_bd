import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
        } catch (IOException | ClassNotFoundException e) {
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
}