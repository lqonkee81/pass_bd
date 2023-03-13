import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Server {

    private ServerSocket sSocket;

    private final int PORT = 3447;  // Порт, на котором работает сервер

    private ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>(); // Список всех клиентов, подключенных к серверу

    Server() {
        try {
            sSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removeClient(ClientHandler client) {
        clients.remove(client);
    }
    
    public void run() {
        while (true) {
            try {
                Socket s = sSocket.accept();
                ClientHandler client = new ClientHandler(s);
                clients.add(client);

                new Thread(client).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}