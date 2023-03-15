import java.io.Serializable;

public class PackageAccept extends Package implements Serializable {
    PackageAccept() {
        super(PackageType.SERVICE_ACCEPT);
    }
}
