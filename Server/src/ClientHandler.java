import java.io.*;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;

public class ClientHandler implements Runnable {
    private Socket socket;
    private final Server server;

    private Encryption enc;

    private PublicKey userPublicKey;
    private final PublicKey selfPublickKey;
    private final PrivateKey selfPrivateKey;

    private ObjectOutputStream objectWriter;
    private ObjectInputStream objectReader;

    private BufferedOutputStream bytesWriter;
    private BufferedInputStream bytesReader;

    public ClientHandler(Socket socket, Server server) {
        System.out.println("Client has been connected!");

        enc = new Encryption();

        this.socket = socket;
        this.server = server;

        this.selfPublickKey = enc.getPublicKey();
        this.selfPrivateKey = enc.getPrivateKey();

        try {
            this.objectWriter = new ObjectOutputStream(socket.getOutputStream());
            this.objectReader = new ObjectInputStream(socket.getInputStream());

            this.bytesWriter = new BufferedOutputStream(socket.getOutputStream());
            this.bytesReader = new BufferedInputStream(socket.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void listener() {
        Package pack = packageHandler();

        PackageType type = pack.getType();
        switch (type) {
            case REGISTRATION:
                RegistrationPackage rp = (RegistrationPackage) pack;
                System.out.println("kk");
                System.out.println(rp);
                break;

            case AUTHORIZATION:
                break;

            case ADD_AUTHORIZE_DATA:
                break;

            case DELETE_AUTHORIZE_DATA:
                break;

            case SERVICE:
                break;

            default:
                System.out.println(pack);
        }
    }

    private Package packageHandler() {
        try {

            byte[] pac_bytes = bytesReader.readAllBytes();
            return enc.decrypt(pac_bytes, selfPrivateKey);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Package(PackageType.SERVICE);
    }

    private void exchangeKeys() {
        /*
         * Обмен ключами с клиентом
         *
         * Сначала принимает сервер клиентский ключ,
         * затем отправляет клиенту свой
         * */

        try {
            userPublicKey = (PublicKey) objectReader.readObject();
            System.out.println("Client public key: " + userPublicKey);

            objectWriter.writeObject(selfPublickKey);
            objectWriter.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        exchangeKeys();
        listener();
    }
}
