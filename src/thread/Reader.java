package thread;

import data.Data;
import data.Pair;
import messagingSystem.Strategy;
import response.Response;

import java.net.Socket;

public class Reader implements Runnable {

    private Strategy strategy;

    public Reader(Strategy strategy, Socket socket){
        this.strategy = strategy;
        this.strategy.setSocket(socket);
    }

    @Override
    public void run() {
        Pair data = Data.getInstance().getVal();
        //TODO:: print here or store what will be printed.
        Response res = new Response();
        res.addValue("type", "read");
        res.addValue("oVal", Integer.toString(data.getVal()));
        res.addValue("rSeq", Integer.toString(data.getrSeq()));
        res.addValue("sSeq", Integer.toString(data.getsSeq()));
        strategy.sendResponse(res);
    }
}
