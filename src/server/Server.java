package server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

public class Server{


    private final static String log_foldername = "logs/";


    /**
     *  Args given with the same order: server_port, num_requests
     * */

    public static AtomicInteger S_seq;
    public static AtomicInteger R_seq;
    public static AtomicInteger R_num;
    public static AtomicInteger O_val;

    private static Vector<Vector<Integer>> readers_log;
    private static Vector<Vector<Integer>> writers_log;

    public static void main(String[] args) throws IOException {

        int server_port = Integer.parseInt(args[0]);
        int num_requests = Integer.parseInt(args[1]);
        ServerSocket ss = new ServerSocket(server_port);

        S_seq = new AtomicInteger(0);
        R_seq = new AtomicInteger(0);
        R_num = new AtomicInteger(0);
        O_val = new AtomicInteger(-1);

        while(num_requests > 0){
            Socket s = ss.accept();
            num_requests--;
            // make new request handler with s as input to constructor
            RequestHandler rh = new RequestHandler(s);
            rh.start();
        }
        while (Thread.activeCount() > 1){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        write_logs();
    }

    public static void write_logs() throws FileNotFoundException {

        String log_filename = "log.txt";
        PrintWriter pw = new PrintWriter(log_foldername + log_filename);

        pw.println("Readers:");
        pw.println("sSeq\toVal\trID\trNum");
        print_logs(readers_log, pw);

        pw.println("Writers:");
        pw.println("sSeq\toVal\twID");
        print_logs(writers_log, pw);

        pw.close();

    }

    public static void print_logs(Vector<Vector<Integer>> logs, PrintWriter pw){

        for(Vector<Integer> log : logs){
            StringBuilder sb = new StringBuilder();
            for(Integer i : log){
                sb.append(i);
                sb.append("\t");
            }
            pw.println(sb.toString());
        }
    }

    public static void log_server(boolean type, Vector<Integer> log){
        if(type){
            readers_log.add(log);
        }else{
            writers_log.add(log);
        }
    }

}
