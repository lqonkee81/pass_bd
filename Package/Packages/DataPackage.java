import java.io.Serializable;

public class DataPackage extends Package implements Serializable {
    private String url;
    private String login;
    private String password;

    private Object object;

    DataPackage(String url, String login, String password, PackageType type) {
        super(type);

        this.url = url;
        this.login = login;
        this.password = password;
        this.object = null;
    }

    DataPackage(String url, PackageType type) {
        super(type);

        this.url = url;
        this.login = null;
        this.password = null;
        this.object = null;
    }

    DataPackage(String url, String password, PackageType type) {
        super(type);

        this.url = url;
        this.password = password;

    }

    DataPackage(Object object, PackageType type) {
        super(type);

        this.object = object;
    }

    DataPackage(PackageType type) {
        super(type);
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

    public Object getObject() {
        return object;
    }

    @Override
    public String toString() {
        return (super.toString() + " " + url + " " + login);
    }
}
