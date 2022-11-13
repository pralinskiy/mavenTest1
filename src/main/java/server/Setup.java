package server;

public class Setup {

    public String adressIP = "localhost";

    public int port;

    public Setup(String adressIP, int port) {
        this.adressIP = adressIP;
        this.port = port;
    }
    public Setup(int port) {
        this.port = port;
    }

}
