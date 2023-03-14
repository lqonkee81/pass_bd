import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;

public class ClientHandler implements Runnable {
    private Socket socket;
    private PublicKey userPublicKey;
    private PublicKey selfPublickKey;
    private PrivateKey selfPrivateKey;

    ClientHandler(Socket socket) {
        this.socket = socket;
    }

    private void listener() {
        try (
                ObjectOutputStream writer = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream reader = new ObjectInputStream(socket.getInputStream());
        ) {

            Package p = (Package) reader.readObject();

            if (p.type == PackageType.REGISTRATION) {
                System.out.println(p);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        System.out.println("Client has been connected!");
        listener();
    }
}
