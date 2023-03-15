import java.io.Serializable;

public class DataPackage extends Package implements Serializable {
    private String url;
    private String login;
    private String password;

    DataPackage(String url, String login, String password, PackageType type) {
        super(type);

        this.url = url;
        this.login = login;
        this.password = password;
    }

    DataPackage(String url, PackageType type) {
        super(type);

        this.url = url;
        this.login = null;
        this.password = null;
    }

    public String getUrl() {
        return url;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return (super.toString() + " " + url + " " + login);
    }
}
