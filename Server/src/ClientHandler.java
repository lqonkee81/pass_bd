import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
    private Encryption enc;

    private PublicKey userPublicKey;
    private final PublicKey selfPublickKey;
    private final PrivateKey selfPrivateKey;

    private ObjectOutputStream objectWriter;
    private ObjectInputStream objectReader;

    private DataBaseHandler dbHandler;
    private PasswordDBHandler passwordsDB;

    private String login;

    private boolean isAuthorized;

    public ClientHandler(Socket socket, Server server) {
        System.out.println("Client has been connected!");

        isAuthorized = false;

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
        controller();
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

    private void controller() {
        /*
         * Обрабатывает принимаемые пакеты
         * */

        while (true) {
            Package pack = recievePackage();
            PackageType type = pack.getType();

            switch (type) {
                case REGISTRATION:
                    RegistrationPackage rgp = (RegistrationPackage) pack;
                    System.out.println(rgp);
                    registration(rgp);
                    break;

                case AUTHORIZATION:
                    AuthorizationPackage autPack = (AuthorizationPackage) pack;
                    System.out.println(pack);
                    autorization(autPack);
                    break;

                case ADD_AUTHORIZE_DATA:
                    if (isAuthorized) {
                        DataPackage aadp = (DataPackage) pack;
                        System.out.println(aadp);

                        addAutorizationData(
                                aadp.getUrl(),
                                aadp.getLogin(),
                                aadp.getPassword()
                        );
                    }
                    break;

                case DELETE_AUTHORIZE_DATA:
                    if (isAuthorized) {
                        DataPackage dadp = (DataPackage) pack;
                        System.out.println(dadp);

                        delAuthorizeData(dadp.getUrl());
                    }
                    break;

                case MODIFY_AUTHORIZE_DATA:
                    if (isAuthorized) {
                        DataPackage dadp = (DataPackage) pack;
                        System.out.println(dadp);

                        updatePassword(dadp.getUrl(), dadp.getPassword());
                    }
                    break;

                case GET_AUTHORIZE_DATA:
                    if (isAuthorized) {
                        DataPackage dadp = (DataPackage) pack;
                        System.out.println(dadp);

                        sendAuthorizeData(dadp.getUrl());
                    }
                    break;

                case GET_FULL_DATA_BASE:
                    if (isAuthorized) {
                        sendAllDataBase();
                    }

                default:
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }
        }
    }

    private Package recievePackage() {
        /*
         * Принимает и расшифровывает пакет
         * */

        try {
            SendingPackage p = (SendingPackage) objectReader.readObject();
            Package pac = enc.decrypt(p.getData(), selfPrivateKey);

            return pac;

        } catch (SocketException | EOFException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Package(PackageType.EMPTY);
    }


    private void sendPackage(Package pac) {
        /*
         * Шифрует пакет и потправляет пользователю
         * */

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
        /*
         * Регистрирует пользователя
         *
         * По сути просто записывает логин и хеш пароля
         * в базу со всеми пользователями
         * */

        try {
            dbHandler.addUser(
                    rgp.getLoggin(),
                    enc.makeHash(rgp.getPassword()),
                    rgp.getPhoneNumber(),
                    rgp.getEmail()
            );
            this.login = rgp.getLoggin();

            sendPackage(new PackageAccept());
            isAuthorized = true;

        } catch (SQLException | NoSuchAlgorithmException e) {
            sendPackage(new PackageError());
            e.printStackTrace();
        }
    }


    private void autorization(AuthorizationPackage pack) {
        /*
         * Проверяет наличие пользователя в базе
         *
         * Если пользователь есть, то сравнивает хеши паролей
         * и принимает решение давать доступ или нет
         * */

        try {
            String hashedPassword = enc.makeHash(pack.getPassword());

            if (dbHandler.checkUser(pack.getLogin(), hashedPassword)) {
                sendPackage(new PackageAccept());
                this.login = pack.getLogin();
                isAuthorized = true;
            } else {
                sendPackage(new PackageError());
            }
        } catch (SQLException | NoSuchAlgorithmException | NullPointerException e) {
            sendPackage(new PackageError());
            e.printStackTrace();
        }
    }


    private void addAutorizationData(String url, String login, String password) {
        /*
         * Добавдение записи в базу данных с паролями пользователя
         * */

        PasswordDBHandler dbHand = new PasswordDBHandler(this.login);

        try {
            dbHand.addRow(
                    url,
                    login,
                    password
            );
            sendPackage(new PackageAccept());

        } catch (SQLException e) {
            sendPackage(new PackageError());
            e.printStackTrace();
        }
    }


    private void delAuthorizeData(String url) {
        /*
         * Удаление записи из базы данных с паролями пользователя
         * */

        PasswordDBHandler dbHand = new PasswordDBHandler(this.login);

        try {
            dbHand.deleteRow(url);
            sendPackage(new PackageAccept());
        } catch (SQLException e) {
            sendPackage(new PackageError());
            e.printStackTrace();
        }
    }


    private void updatePassword(String url, String password) {
        PasswordDBHandler dbHand = new PasswordDBHandler(this.login);

        try {
            dbHand.updatePassword(url, password);
            sendPackage(new PackageAccept());
        } catch (SQLException e) {
            sendPackage(new PackageError());
            e.printStackTrace();
        }
    }

    private void sendAuthorizeData(String url) {
        PasswordDBHandler dbHand = new PasswordDBHandler(this.login);

        try {
            ArrayList<String> data = dbHand.getAuthorizeData(url);

            sendPackage(new DataPackage(data, PackageType.GET_AUTHORIZE_DATA));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void sendAllDataBase() {
        /*
         * Отпроавляет всю базу данных пользователю
         * */

        PasswordDBHandler dbHand = new PasswordDBHandler(this.login);

        try {
            ArrayList<String> urls = dbHand.getFullDataBase();

            DataPackage pack = new DataPackage(urls, PackageType.GET_FULL_DATA_BASE);

            sendPackage(pack);
        } catch (SQLException e) {
            sendPackage(new PackageError());
            e.printStackTrace();
        }
    }
}
