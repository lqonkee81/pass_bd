import java.io.Serializable;

public class AuthorizationPackage extends Package implements Serializable {
    private String login;
    private String password;

    AuthorizationPackage(String login, String password) {
        super(PackageType.AUTHORIZATION);

        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return (super.toString() + " " + login);
    }
}

