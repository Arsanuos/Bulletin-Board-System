package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

public class Server{


    /**
     *  Args given with the same order: server_port, num_requests
     * */

    public static AtomicInteger S_seq;
    public static AtomicInteger R_num;
    public static AtomicInteger O_val;

    private static Vector<Vector<Integer>> readers_log;
    private static Vector<Vector<Integer>> writers_log;

    public static void main(String[] args) throws IOException {

        int server_port = Integer.parseInt(args[0]);
        int num_requests = Integer.parseInt(args[1]);
        ServerSocket ss = new ServerSocket(server_port);

        int R_seq = 0;
        S_seq = new AtomicInteger(0);
        R_num = new AtomicInteger(0);
        O_val = new AtomicInteger(-1);

        readers_log = new Vector<>();
        writers_log = new Vector<>();

        System.out.println("Server Started");

        while(num_requests > 0){
            Socket s = ss.accept();
            num_requests--;
            R_seq++;

            System.out.println("Server accepts new connection");
            // make new request handler with s as input to constructor
            RequestHandler rh = new RequestHandler(s, R_seq);
            rh.start();
        }

        System.out.println("Server waits for all threads to finish");
        while (Thread.activeCount() > 1){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Server Ended");

        write_logs();
    }

    public static void write_logs() throws FileNotFoundException {

        String log_filename = "log.txt";
        PrintWriter pw = new PrintWriter(log_filename);

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

    public static synchronized void log_server(boolean type, Vector<Integer> log){
        if(type){
            readers_log.add(log);
        }else{
            writers_log.add(log);
        }
    }

}

class RequestHandler extends Thread {

    private Socket s;
    private int r_req;

    public RequestHandler(Socket s, int r_req){
        this.r_req = r_req;
        this.s = s;
    }

    @Override
    public void run() {
        DataInputStream read_soc;
        DataOutputStream write_soc;
        try {

            read_soc = new DataInputStream(s.getInputStream()) ;
            write_soc = new DataOutputStream(s.getOutputStream());

            String line = read_soc.readUTF();
            String type_name = line.split(" ")[0];
            int id = Integer.parseInt(line.split(" ")[1]);
            boolean type = type_name.equalsIgnoreCase("Reader");
            String msg;

            int r_num = 0;
            int o_val;

            if(type){
                r_num = Server.R_num.incrementAndGet();
            }

            //sleep for while ( process request )
            long sleep_period =  (long) (Math.random() * 10000);
            Thread.sleep(sleep_period);

            int s_seq = Server.S_seq.incrementAndGet();

            // starts
            if (type){
                o_val = Server.O_val.intValue();
                msg = r_req + "\t" + s_seq + "\t" + o_val;
            }else {
                Server.O_val.getAndSet(id);
                o_val = id;
                msg = r_req + "\t" + s_seq;
            }

            // send response
            write_soc.writeUTF(msg);
            write_soc.flush();


            // logs to the server
            Vector<Integer> log = new Vector<>();
            log.add(s_seq);
            log.add(o_val);
            log.add(id);

            if (type){
                log.add(r_num);
                Server.R_num.decrementAndGet();
            }

            write_soc.close();
            read_soc.close();

            Server.log_server(type, log);

        } catch (IOException e) {
            System.out.println("Error in the read/write socket");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
