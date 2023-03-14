import java.security.PublicKey;

public class EncPackage extends Package {
    PublicKey publicKey;

    EncPackage(PackageType type, PublicKey publicKey) {
        super(type);
        this.publicKey = publicKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}
