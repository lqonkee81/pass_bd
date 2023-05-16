import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket sSocket;

    Server() {
        try {
            // Порт, на котором работает сервер
            int PORT = 9999;
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
                ClientHandler client = new ClientHandler(s);

                new Thread(client).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}