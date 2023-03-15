import java.io.Serializable;

public final class RegistrationPackage extends Package implements Serializable {
    private final String loggin;
    private final String password;
    private final String phoneNumber;
    private final String email;

    RegistrationPackage(String loggin, String password, String phoneNumber, String email) {
        super(PackageType.REGISTRATION);

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
