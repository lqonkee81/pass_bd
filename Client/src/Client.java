import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {
    private final int PORT = 9999;

    Encryption enc;

    private PublicKey serverPublicKey;
    private PublicKey selfPublicKey;
    private PrivateKey selfPrivateKey;

    private Socket socket;
    private ObjectOutputStream writer;
    private ObjectInputStream reader;

    Scanner sc;

    public Client() {
        this.sc = new Scanner(System.in);

        this.enc = new Encryption();
        this.selfPublicKey = enc.getPublicKey();
        this.selfPrivateKey = enc.getPrivateKey();

        try {
            this.socket = new Socket(
                    "localhost",
                    PORT
            );

            this.reader = new ObjectInputStream(socket.getInputStream());
            this.writer = new ObjectOutputStream(socket.getOutputStream());

        } catch (UnknownHostException e) {
            System.err.println("Server is not avaliable");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void run() {
        /*
         * Стартовая точка работы клиента
         * */

        // Обмен публичными ключами с сервером
        try {
            exchangeKeys();
        } catch (Exception e) {
            System.err.println("Cannot echange public keys");
            e.printStackTrace();
        }

        // Авторизация на сервере
        authorize();
    }

    private boolean checkResponse() {
        /*
         * Проверяет ответ от сервера
         * ( либо все збс, либо хуева )
         * */

        PackageType responseType = recievePackage().getType();

        if (responseType.equals(PackageType.SERVICE_ACCEPT)) {
            return true;
        } else {
            return false;
        }
    }

    private Package recievePackage() {
        /*
         * Принимает и расшифровывает пакет
         * */

        try {
            SendingPackage p = (SendingPackage) reader.readObject();
            Package pac = enc.decrypt(p.getData(), selfPrivateKey);

            return pac;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Package(PackageType.SERVICE_ERROR);
    }

    private void sendPackage(Package pac) throws Exception {
        /*
         * Шифрует пакет и потправляет серверу
         * */

        byte[] sendingPackage = enc.encrypt(pac, serverPublicKey);

        SendingPackage p = new SendingPackage(sendingPackage);
        writer.writeObject(p);
        writer.flush();
    }

    private void exchangeKeys() throws IOException, ClassNotFoundException {
        /*
         * Обмен ключами с сервером
         *
         * Сначала клиент отправляет свой ключ,
         * затем принимает ключ сервера
         * */

        writer.writeObject(selfPublicKey);
        writer.flush();

        serverPublicKey = (PublicKey) reader.readObject();
        System.out.println("Client public key: " + serverPublicKey);
    }

    private void authorize() {
        /*
         * Авторизация в приложении
         *
         * Либо регистрация
         * Либо вход в уже сущесвтующий аккаунт
         * */

        int choice;

        System.out.println("ВЫберите действие: ");
        System.out.println("1. Авторизация");
        System.out.println("2. Регистрация");

        System.out.print("\n>: ");
        choice = sc.nextInt();

        switch (choice) {
            case 1:
                authorization();
                break;

            case 2:
                try {
                    registration();
                } catch (Exception e) {
                    System.out.println("При регистрации произошла ошибка!");
                    e.printStackTrace();
                }
                break;

            default:
                System.out.println("Нет такого действия");
        }
    }

    private void registration() throws Exception {
        /*
         * Регистрация нового аккаунта
         *
         * Так же запускает главный цикл приложения
         * */

        String login;
        String password;
        String phoneNumber;
        String email;

        System.out.print("Введите логин: ");
        login = sc.next();

        System.out.print("Введите пароль: ");
        password = sc.next();

        System.out.print("Введите номер телефона: ");
        phoneNumber = sc.next();

        System.out.print("Введите электронную почту: ");
        email = sc.next();

        RegistrationPackage rgp = new RegistrationPackage(
                login,
                password,
                phoneNumber,
                email
        );

        sendPackage(rgp);

        if (checkResponse()) {
            System.out.println("Регистрация прошла успешно!");
            mainLoop();
        } else {
            System.err.println("Ошибка при регистрации");
        }
    }

    private void authorization() {
        /*
         * Авторизация уже существующего аккаунта
         *
         * Так же запускает главыный цикл приложения
         * */

        String login;
        String password;

        System.out.print("Введите логин: ");
        login = sc.next();

        System.out.print("Введите пароль: ");
        password = sc.next();

        AuthorizationPackage pack = new AuthorizationPackage(login, password);

        try {
            sendPackage(pack);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (checkResponse()) {
            System.out.println("Все крута. Добро пожаловать!");
            mainLoop();
        } else {
            System.err.println("Что-то пошло не так");
            System.exit(1);
        }
    }

    private void mainLoop() {
        while (true) {
            int choice;

            System.out.println("\nЧто сделать: ");
            System.out.println("1. Получить всю базу данных");
            System.out.println("2. Добавить запись");
            System.out.println("3. Удалить запись");
            System.out.println("4. Изменть запись");

            System.out.print(">: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    getFullDataBase();
                    break;

                case 2:
                    addAuthorizeData();
                    break;

                case 3:
                    delAuthorizeData();
                    break;
            }
        }
    }

    private void addAuthorizeData() {
        /*
         * Добавдение записи в базу данных с паролями пользователя
         * */

        String url;
        String login;
        String password;

        System.out.println("Введите url: ");
        url = sc.next();

        System.out.println("Введите логин: ");
        login = sc.next();

        System.out.println("Введите пароль: ");
        password = sc.next();

        DataPackage pack = new DataPackage(
                url,
                login,
                password,
                PackageType.ADD_AUTHORIZE_DATA
        );

        try {
            sendPackage(pack);

            if (checkResponse()) {
                System.out.println("Добавлено!");
            } else {
                System.err.println("Какая-то ошибка");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void delAuthorizeData() {
        /*
         * Удаление записи из базы данных с паролями пользователя
         * */

        String del_url;

        System.out.println("Введите url: ");
        del_url = sc.next();

        DataPackage pack = new DataPackage(del_url, PackageType.DELETE_AUTHORIZE_DATA);

        try {
            sendPackage(pack);

            if (checkResponse()) {
                System.out.println("Сделано!");
            } else {
                System.err.println("Что-то пошло не так");
            }
        } catch (Exception e) {
            System.err.println("Что-то пошло не так");
        }
    }

    private void getFullDataBase() {
        DataPackage pack = new DataPackage(PackageType.GET_FULL_DATA_BASE);

        try {
            sendPackage(pack);
            Thread.sleep(1500);
        } catch (Exception e) {
            System.err.println("Что-то пошло не так");
            e.printStackTrace();
        }

        DataPackage dataBase = (DataPackage) recievePackage();
        ArrayList<String> urls = (ArrayList<String>) dataBase.getObject();

        System.out.println("\n\nВсе записи: ");
        for (String url : urls) {
            System.out.println(url);
        }
        System.out.println("\n\n");
    }
}