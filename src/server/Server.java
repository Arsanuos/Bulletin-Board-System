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

        super();

        S_seq = new AtomicInteger(0);
        R_num = new AtomicInteger(0);
        O_val = new AtomicInteger(-1);
        R_seq = new AtomicInteger(0);

        readers_log = new PrintWriter("log_readers.txt");
        writers_log = new PrintWriter("log_writers.txt");

        readers_log.println(
                "Readers:\n" +
                "sSeq\toVal\trID\trNum");
        readers_log.flush();

        writers_log.println(
                "Writers:\n" +
                "sSeq\toVal\twID");
        writers_log.flush();
    }

    public static void main(String[] args) throws IOException, AlreadyBoundException {

        String server_ip = args[0];
        int server_port = Integer.parseInt(args[1]);

        System.setProperty("java.rmi.server.hostname", server_ip);

        String name = "HelloRMI";

        Server s = new Server();

        Server_RMI rmi = (Server_RMI) UnicastRemoteObject.exportObject(s, server_port);

        Registry r = LocateRegistry.createRegistry(server_port);

        r.bind(name, rmi);

        System.out.println("Server Started");

    }

    private synchronized void  write_log(String server_log, boolean type){
        if (type){
            R_num.decrementAndGet();
            readers_log.println(server_log);
            readers_log.flush();
        }else{
            writers_log.println(server_log);
            writers_log.flush();
        }
    }

    @Override
    public String apply(boolean type, int id) throws RemoteException {

        int r_req = R_seq.getAndIncrement();
        int r_num = 0, s_seq;
        String resp;
        String server_log;
        int o_val = 0;

        // starts
        if (!type){
            O_val.getAndSet(id);
        }else{
            r_num = R_num.incrementAndGet();
            o_val = O_val.get();
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
            resp = r_req + "\t" + s_seq + "\t" + o_val;
            server_log = s_seq + "\t" + o_val + "\t" + id + "\t" + r_num ;
        }else {
            resp = r_req + "\t" + s_seq;
            server_log = s_seq + "\t" + id + "\t" + id;
        }

        // logs to the server
        if (type){
            R_num.decrementAndGet();
        }
        write_log(server_log, type);

        return resp;
    }








}
