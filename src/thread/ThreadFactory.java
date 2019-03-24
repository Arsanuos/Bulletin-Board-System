package thread;

import constants.Settings;
import messagingSystem.Strategy;
import request.AbstractReq;

import java.net.Socket;
import java.util.Set;

public class ThreadFactory {


    public static void CreateThread(AbstractReq req, Strategy strategy, Socket socket){
        if(req.getKey("type").equals(Settings.READ_REQ)){
            Thread thread = new Thread(new Reader(strategy, socket));
            thread.start();
            return;
        }else if(req.getKey("type").equals(Settings.WRITE)){
            Thread thread = new Thread(new Writer(strategy, socket, Integer.valueOf(req.getKey("oVal"))));
            thread.start();
            return;
        }
        throw new RuntimeException("Not a valid request type");
    }
}

