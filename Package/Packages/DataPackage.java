import java.io.Serializable;

public class DataPackage extends Package implements Serializable {
    private String url;
    private String login;
    private String password;

    DataPackage(String url, String login, String password) {
        super(PackageType.ADD_AUTHORIZE_DATA);

        this.url = url;
        this.login = login;
        this.password = password;
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
