package client;


import server.Server_RMI;

import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *  We will deal with the client as separate program from our project
 *
 * */


public class Client {

    /**
     *  Args given with the same order: type, id, server_address, server_port, num_access
     * */
    public static void main(String[] args) throws IOException, NotBoundException {

        // Read inputs
        boolean client_type = Boolean.parseBoolean(args[0]);
        int client_id = Integer.parseInt(args[1]);
        String server_address = args[2];
        int server_port = Integer.parseInt(args[3]);
        int num_access = Integer.parseInt(args[4]);

        String name = "HelloRMI";

        System.setProperty("java.rmi.server.hostname", server_address);

        Registry r = LocateRegistry.getRegistry(server_address, server_port);

        Server_RMI caller = (Server_RMI) r.lookup(name);


        String client_label;

        if(client_type){
            client_label = "Reader";
        }else{
            client_label = "Writer";
        }

        String log_filename = "log" + client_id + ".txt";
        PrintWriter pw = new PrintWriter(log_filename);

        pw.println("Client Type: " + client_label);
        pw.println("Client Name: " + client_id);


        if(client_type) {
            pw.println("rSeq\tsSeq\toVal");
        }else{
            pw.println("rSeq\tsSeq");
        }
        System.out.println("Client" + client_id + " which is "  + client_type + " has started.");
        for(int i = 0 ; i < num_access ; i++){

            // create socket, send read request

            System.out.println("Client" + client_id + " sends to the server");

            // send data to server
            String response = caller.apply(client_type, client_id);

            //response_msg would be like: 1 \t 5 \t 0. With no spaces, I just put it for visualization.
            System.out.println("Client" + client_id + " receives from the server");

            pw.println(response);

            System.out.println("Client" + client_id + " Finished " + (i+1) + " requests out of " + num_access);

        }

        pw.close();
    }

}
