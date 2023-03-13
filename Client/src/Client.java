import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try {
            Socket s = new Socket("localhost", 3447);
        } catch (ConnectException e) {
            System.out.println("Server is not avalible");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}