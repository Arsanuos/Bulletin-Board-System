package messagingSystem;

import configuration.Configuration;

import java.io.IOException;
import java.net.Socket;

public class SocketConnection {

    private static String ip = Configuration.getInstance().getProp("RW.server");
    private static int port = Integer.valueOf(Configuration.getInstance().getProp("RW.server.port"));

    private static Socket socket;

    private SocketConnection() {
    }

    public static Socket getInstance(){
        //open socket with server.
        try {
            socket = new Socket(ip, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return socket;
    }
}
