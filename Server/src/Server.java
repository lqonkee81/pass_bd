import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private final int PORT = 10521;  // Порт, на котором работает сервер
    private ServerSocket sSocket;
    private final String pathDbFile = "DataBase/users.db";
    DataBaseHandler dataBaseHandler = new DataBaseHandler(pathDbFile);

    Server() {
        try {
            sSocket = new ServerSocket(PORT);
            System.out.println("Server started on port: " + PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        /*
         * Вот тут сервер начинает свою работу
         *
         * Создает для каждого нового клиента отдльеный обрабьотчик в новом потоке
         * */

        while (true) {
            try {
                Socket s = sSocket.accept();
                ClientHandler client = new ClientHandler(s, this);

                new Thread(client).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}