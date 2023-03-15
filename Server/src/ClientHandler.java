import java.io.*;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.sql.SQLException;

public class ClientHandler implements Runnable {
    private Encryption enc;

    private PublicKey userPublicKey;
    private final PublicKey selfPublickKey;
    private final PrivateKey selfPrivateKey;

    private ObjectOutputStream objectWriter;
    private ObjectInputStream objectReader;

    private DataBaseHandler dbHandler;

    public ClientHandler(Socket socket, Server server) {
        System.out.println("Client has been connected!");

        dbHandler = new DataBaseHandler();

        enc = new Encryption();

        this.selfPublickKey = enc.getPublicKey();
        this.selfPrivateKey = enc.getPrivateKey();

        try {
            this.objectWriter = new ObjectOutputStream(socket.getOutputStream());
            this.objectReader = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        exchangeKeys();
        listener();
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

    private void listener() {
        Package pack = recievePackage();

        PackageType type = pack.getType();
        switch (type) {
            case REGISTRATION:
                RegistrationPackage rgp = (RegistrationPackage) pack;
                registration(rgp);
                break;

            case AUTHORIZATION:
                break;

            case ADD_AUTHORIZE_DATA:
                break;

            case DELETE_AUTHORIZE_DATA:
                break;

            default:
                System.out.println(pack);
        }
    }

    private Package recievePackage() {
        try {
            SendingPackage p = (SendingPackage) objectReader.readObject();
            Package pac = enc.decrypt(p.getData(), selfPrivateKey);

            return pac;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Package(PackageType.SERVICE_ERROR);
    }

    private void sendPackage(Package pac) {
        try {
            byte[] sendPack = enc.encrypt(pac, userPublicKey);
            SendingPackage p = new SendingPackage(sendPack);

            objectWriter.writeObject(p);
            objectWriter.flush();

        } catch (Exception e) {
            System.err.println("Cannot to send package to user");
            e.printStackTrace();
        }
    }

    private void registration(RegistrationPackage rgp) {
        try {
            dbHandler.addUser(
                    rgp.getLoggin(),
                    rgp.getPassword(),
                    rgp.getPhoneNumber(),
                    rgp.getEmail()
            );

            sendPackage(new PackageAccept());

        } catch (SQLException e) {
            sendPackage(new PackageError());
            e.printStackTrace();
        }
    }
}
