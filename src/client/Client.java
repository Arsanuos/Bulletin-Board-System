package client;


import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 *  We will deal with the client as separate program from our project
 *
 * */


public class Client {

    /**
     *  Args given with the same order: type, id, server_address, server_port, num_access
     * */
    public static void main(String[] args) throws IOException {

        // Read inputs
        Boolean client_type = Boolean.parseBoolean(args[0]);
        Integer client_id = Integer.parseInt(args[1]);
        InetAddress server_address = InetAddress.getByName(args[2]);
        Integer server_port = Integer.parseInt(args[3]);
        Integer num_access = Integer.parseInt(args[4]);

        String client_label;

        if(client_type){
            client_label = "Reader";
        }else{
            client_label = "Writer";
        }

        String log_filename = "log" + client_id.toString() + ".txt";
        PrintWriter pw = new PrintWriter(log_filename);

        pw.println("Client Type: " + client_label);
        pw.println("Client Name: " + client_id.toString());


        if(client_type) {
            pw.println("rSeq\tsSeq\toVal");
        }else{
            pw.println("rSeq\tsSeq");
        }
        System.out.println("Client" + client_id + " which is "  + client_type + " has started.");
        for(int i = 0 ; i < num_access ; i++){

            // create socket, send read request
            Socket soc = new Socket(server_address, server_port);
            DataInputStream read_soc = new DataInputStream(soc.getInputStream()) ;
            DataOutputStream write_soc = new DataOutputStream(soc.getOutputStream());

            System.out.println("Client" + client_id + " sends to the server");
            // send data to server
            String server_msg = client_label + " " + client_id.toString();
            write_soc.writeUTF(server_msg);
            write_soc.flush();

            //response_msg would be like: 1 \t 5 \t 0. With no spaces, I just put it for visualization.
            System.out.println("Client" + client_id + " receives from the server");
            String response = read_soc.readUTF();
            pw.println(response);
            write_soc.close();
            read_soc.close();
            soc.close();
            System.out.println("Client" + client_id + " Finished " + (i+1) + " requests out of " + num_access);

        }

        pw.close();
    }

}
