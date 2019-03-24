package thread;

import data.Data;
import messagingSystem.Strategy;
import response.Response;

import java.net.Socket;

public class Reader implements Runnable {

    private Strategy strategy;
    private int rSeq, sSeq;

    public Reader(Strategy strategy, Socket socket, int rSeq, int sSeq){
        this.strategy = strategy;
        this.strategy.setSocket(socket);
        this.rSeq = rSeq;
        this.sSeq = sSeq;
    }

    @Override
    public void run() {
        int data = Data.getInstance().getVal();
        //TODO:: print here or store what will be printed.
        Response res = new Response();
        res.addValue("type", "read");
        res.addValue("rSeq", Integer.toString(rSeq));
        res.addValue("sSeq", Integer.toString(sSeq));
        strategy.sendResponse(res);
    }
}
