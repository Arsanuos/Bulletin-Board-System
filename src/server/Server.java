package server;

import java.io.*;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Server_RMI {




    /**
     *  Args given with the same order: server_port, num_requests
     * */

    private  AtomicInteger S_seq;
    private  AtomicInteger R_num;
    private  AtomicInteger O_val;
    private AtomicInteger R_seq;

    private PrintWriter readers_log;
    private PrintWriter writers_log;



    protected Server() throws FileNotFoundException {

        S_seq = new AtomicInteger(0);
        R_num = new AtomicInteger(0);
        O_val = new AtomicInteger(-1);
        R_seq = new AtomicInteger(0);

        readers_log = new PrintWriter("log_readers.txt");
        writers_log = new PrintWriter("log_writers.txt");

    }

    public static void main(String[] args) throws IOException, AlreadyBoundException {

        String server_ip = args[0];
        int server_port = Integer.parseInt(args[1]);

        System.setProperty("java.rmi.server.hostname", server_ip);

        String name = "HelloRMI";

        Server s = new Server();

        Server_RMI rmi = (Server_RMI) UnicastRemoteObject.exportObject(s, server_port);

        Registry r = LocateRegistry.getRegistry(server_port);

        r.bind(name, rmi);

        System.out.println("Server Started");

    }

    @Override
    public synchronized String apply(boolean type, int id) throws RemoteException {

        int r_req = R_seq.getAndIncrement();
        int r_num, s_seq;
        String resp;
        String server_log;

        // starts
        if (!type){
            O_val.set(id);
        }

        //sleep for while
        long sleep_period =  (long) (Math.random() * 10000);
        try {
            Thread.sleep(sleep_period);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        s_seq = S_seq.getAndIncrement();

        if (type){
            r_num = R_num.incrementAndGet();
            resp = r_req + "\t" + s_seq + "\t" + O_val.toString();
            server_log = s_seq + "\t" + O_val.toString() + "\t" + id + "\t" + r_num ;
        }else {
            O_val.set(id);
            resp = r_req + "\t" + s_seq;
            server_log = s_seq + "\t" + O_val.toString() + "\t" + id;
        }

        // logs to the server
        if (type){
            R_num.decrementAndGet();
            readers_log.println(server_log);
        }else{
            writers_log.println(server_log);
        }

        return resp;
    }








}
