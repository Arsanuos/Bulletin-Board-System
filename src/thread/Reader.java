package thread;

import data.Data;
import data.Pair;
import messagingSystem.Strategy;
import response.Response;

import java.net.Socket;

public class Reader implements Runnable {

    private Strategy strategy;
    private int rSeq;

    public Reader(Strategy strategy, Socket socket, int rSeq){
        this.strategy = strategy;
        this.strategy.setSocket(socket);
        this.rSeq = rSeq;
    }

    @Override
    public void run() {
        Pair data = Data.getInstance().getVal();
        //TODO:: print here or store what will be printed.
        Response res = new Response();
        res.addValue("type", "read");
        res.addValue("oVal", Integer.toString(data.getFirst()));
        res.addValue("rSeq", Integer.toString(rSeq));
        res.addValue("sSeq", Integer.toString(data.getSecond()));
        strategy.sendResponse(res);
    }
}
