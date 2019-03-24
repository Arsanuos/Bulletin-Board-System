package thread;

import data.Data;
import messagingSystem.Strategy;
import response.Response;

import java.net.Socket;

public class Writer implements Runnable {

    private Strategy strategy;
    private int rSeq, val;

    public Writer(Strategy strategy, Socket socket, int rSeq, int val){
        this.strategy = strategy;
        this.strategy.setSocket(socket);
        this.rSeq = rSeq;
        this.val = val;
    }

    @Override
    public void run() {
        int sSeq = Data.getInstance().setVal(val);
        //TODO:: print here or store what will be printed.
        Response res = new Response();
        res.addValue("type", "write");
        res.addValue("rSeq", Integer.toString(rSeq));
        res.addValue("sSeq", Integer.toString(sSeq));
        strategy.sendResponse(res);
    }
}
