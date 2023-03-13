import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;


    ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("Clinet has been connected!");
    }
}
