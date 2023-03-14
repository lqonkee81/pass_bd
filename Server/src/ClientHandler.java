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

//    private ObjectOutputStream writer;
//    private ObjectInputStream reader;

    public ClientHandler(Socket socket, Server server) {
        System.out.println("Client has been connected!");

        enc = new Encryption();

        this.socket = socket;
        this.server = server;

        this.selfPublickKey = enc.getPublicKey();
        this.selfPrivateKey = enc.getPrivateKey();
    }

    private void listener() {
//        Package pack = packageHandler();

        Package pack = new Package(PackageType.SERVICE);

//        try {
//            pack = (Package) reader.readObject();
//
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }

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
        Package pac = null;
//        try (
//                BufferedInputStream reader = new BufferedInputStream(socket.getInputStream());
//                BufferedOutputStream writer = new BufferedOutputStream(socket.getOutputStream())
//        ) {
//            byte[] pac_bytes = reader.readAllBytes();
//            pac = enc.decrypt(pac_bytes, selfPrivateKey);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        return pac;
    }

    private void exchangeKeys() {
        /*
         * Обмен ключами с клиентом
         *
         * Сначала принимает сервер клиентский ключ,
         * затем отправляет клиенту свой
         * */

        try (
                ObjectOutputStream writer = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream reader = new ObjectInputStream(socket.getInputStream());
        ) {
            userPublicKey = (PublicKey) reader.readObject();
            System.out.println("Client public key: " + userPublicKey);

            writer.writeObject(selfPublickKey);
            writer.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        exchangeKeys();
//        listener();
    }
}
