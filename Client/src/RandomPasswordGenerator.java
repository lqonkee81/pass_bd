import java.util.Random;

public class RandomPasswordGenerator {
    static private String[] data = {"q", "w", "e", "r", "t", "y", "u", "i", "o", "p",
            "a", "s", "d", "f", "g", "h", "j", "k", "l",
            "z", "x", "c", "v", "b", "n", "m",
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "_"
    };

    static private int randomNum(int min, int max) {
        Random r = new Random();
        return r.nextInt(max) + min;
    }

    static String genPass() {
        int length = 15;

        String[] p = new String[length];

        for (int i = 0; i < length; i++) {
            p[i] = data[randomNum(0, data.length)];
        }

        String password = "";

        for (String s : p) {
            password += s;
        }

        return password;
    }
}
