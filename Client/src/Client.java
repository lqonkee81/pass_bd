import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static <writer> void main(String[] args) {
        Scanner scanner = new Scanner(System.in);


        String loggin;
        String password;
        String phoneNumber;
        String email;

        System.out.print("Loggin: ");
        loggin = scanner.nextLine();

        System.out.print("Password: ");
        password = scanner.nextLine();

        System.out.print("Phone number: ");
        phoneNumber = scanner.nextLine();

        System.out.print("Email: ");
        email = scanner.nextLine();


        try {
            Socket socket = new Socket("localhost", 3447);
            ObjectOutputStream writer = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream reader = new ObjectInputStream(socket.getInputStream());

            Package p = new RegistrationPackage(
                    PackageType.REGISTRATION,
                    loggin,
                    password,
                    phoneNumber,
                    email
            );

            writer.writeObject(p);
            writer.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}