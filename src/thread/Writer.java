package thread;

import data.Data;
import data.Pair;
import messagingSystem.Strategy;
import response.Response;

import java.net.Socket;

public class Writer implements Runnable {

    private Strategy strategy;
    private int val;

    public Writer(Strategy strategy, Socket socket, int val){
        this.strategy = strategy;
        this.strategy.setSocket(socket);
        this.val = val;
    }

    @Override
    public void run() {
        Pair data = Data.getInstance().setVal(val);
        //TODO:: print here or store what will be printed.
        Response res = new Response();
        res.addValue("type", "write");
        res.addValue("rSeq", Integer.toString(data.getrSeq()));
        res.addValue("sSeq", Integer.toString(data.getsSeq()));
        strategy.sendResponse(res);
    }
}
