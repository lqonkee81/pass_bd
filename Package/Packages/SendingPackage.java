import java.io.Serializable;

public class SendingPackage implements Serializable {
    private byte[] data;

    SendingPackage(byte[] o) {
        this.data = o;
    }

    public byte[] getData() {
        return data;
    }
}
