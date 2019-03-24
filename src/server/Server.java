package server;

import configuration.Configuration;
import data.Data;
import messagingSystem.Strategy;
import request.AbstractReq;
import request.RequestFactory;
import thread.ThreadFactory;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable{

    private ServerSocket server;
    private Strategy strategy;

    public Server(Strategy strategy){
        this.strategy = strategy;
    }

    @Override
    public void run() {
        while(Data.getInstance().getNumberOfRequests() != 0){
            try {
                server = new ServerSocket(Integer.valueOf(Configuration.getInstance().getProp("RW.server.port")));
                System.out.println("Server started");
                System.out.println("Waiting for a client ...");
                Socket socket = server.accept();
                System.out.println("Client accepted");
                DataInputStream dataInputStream= new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                String line = "";
                line = dataInputStream.readUTF();
                AbstractReq req = RequestFactory.getInstance(line);
                ThreadFactory.CreateThread(req, strategy, socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
