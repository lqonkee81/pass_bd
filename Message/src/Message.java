/*
* Сетевой пакет, который содержит всю необходимю информацию для передачи
* по сети
* */

import java.io.Serializable;

public class Message implements Serializable {
    private String loggin;
    private String password;
    private String url;


    Message(String loggin, String password, String url) {
        this.loggin = loggin;
        this.password = password;
        this.url = url;
    }
}