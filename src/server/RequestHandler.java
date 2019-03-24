package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

public class RequestHandler extends Thread {

    private Socket s;
    public RequestHandler(Socket s){
        this.s = s;
    }

    @Override
    public void run() {
        DataInputStream read_soc = null;
        DataOutputStream write_soc = null;
        try {

             read_soc = new DataInputStream(s.getInputStream()) ;
             write_soc = new DataOutputStream(s.getOutputStream());

            String line = read_soc.readUTF();
             String type_name = line.split(" ")[0];
             int id = Integer.parseInt(line.split(" ")[1]);
             boolean type = type_name.equalsIgnoreCase("Reader");
             String msg;

             // starts
            Server.S_seq.incrementAndGet();
            if (type){
                 Server.R_num.incrementAndGet();
                 Server.R_seq.incrementAndGet();
                 msg = Server.R_seq.toString() + "\t" + Server.S_seq.toString() + "\t" + Server.O_val.toString();
             }else {
                Server.O_val.set(id);
                msg = Server.R_seq.toString() + "\t" + Server.S_seq.toString();
            }
            // send response
            write_soc.writeUTF(msg);
            write_soc.flush();

            //sleep for while
            long sleep_period =  (long) (Math.random() * 10000);
            Thread.sleep(sleep_period);

            // logs to the server
            Vector<Integer> log = new Vector<>();
            log.add(Server.S_seq.intValue());
            log.add(Server.O_val.intValue());
            log.add(id);

            if (type){
                log.add(Server.R_num.intValue());
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
