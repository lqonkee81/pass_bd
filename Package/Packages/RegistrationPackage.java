import java.io.Serializable;

public class RegistrationPackage extends Package implements Serializable {
    private String loggin;
    private String password;
    private String phoneNumber;
    private String email;

    RegistrationPackage(PackageType type, String loggin, String password, String phoneNumber, String email) {
        super(type);

        this.loggin = loggin;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public String getLoggin() {
        return loggin;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return (
                super.toString() + ' ' +
                        this.loggin + ' ' +
                        this.password + ' ' +
                        this.phoneNumber + ' ' +
                        this.email
        );
    }
}
