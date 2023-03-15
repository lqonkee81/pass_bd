import java.io.Serializable;
public class PackageError extends Package implements Serializable {
    PackageError() {
        super(PackageType.SERVICE_ERROR);
    }
}
