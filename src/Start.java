import sshHandler.sshHandler;

import java.io.*;

public class Start {

    private static int server_port;
    private static String server_ip;
    private static int num_readers;
    private static String[] readers_ip;
    private static int num_writers;
    private static String[] writers_ip;
    private static int num_access;

    private static final String config_file = "system.properties";

    private static final String server_username = "muhammed";
    private static final String server_password = "az123456789";
    private static final String client_username = "muhammed";
    private static final String client_password = "az123456789";
    private static final int ssh_port = 22;

    public static void main(String[] args) throws InterruptedException {

        // read files
        read_file();

        // start server as separate program.
        run_server();

        Thread.sleep(1000);

        // start clients as separate programs.
        run_clients();

    }

    private static void run_server()  {

        sshHandler sshHandler = new sshHandler();

        boolean connected = sshHandler.canConnect(server_username,server_password, server_ip, ssh_port);

        if(connected){
            // assume server folder at home directory
            sshHandler.applyCommand("cd \n");

            // compile server code
            sshHandler.applyCommand("javac server/Server.java \n");

            // calc number of incoming requests
            int num_requests = (num_readers + num_writers) * num_access;

            // run server code
            sshHandler.applyCommand("java server.Server " + server_ip + " " + server_port + " \n");

            System.out.println("Server is on now");

            sshHandler.close();
        }


    }

    private static void run_clients() {

        sshHandler sshHandler = new sshHandler();

        int current_id = 0;

        // run client code, args -> type, id, server_address, server_port, num_access
        for(int i = 0 ; i < num_readers; i++){
            makeClient(sshHandler, current_id, readers_ip[i], true);
            current_id++;
        }

        for(int i = 0 ; i < num_writers; i++){
            makeClient(sshHandler, current_id, writers_ip[i], false);
            current_id++;
        }

    }

    private static void makeClient(sshHandler sshHandler, int current_id, String ip, boolean type){

        boolean connected = sshHandler.canConnect(client_username, client_password, ip, ssh_port);

        if(connected){
            // assume server folder at home directory
            sshHandler.applyCommand("cd \n");

            // compile server code
            sshHandler.applyCommand("javac client/Client.java \n");

            // run server code
            sshHandler.applyCommand("java client.Client " + type + " " + current_id + " "
                    + server_ip + " " + server_port + " " + num_access + " \n");

            System.out.println("Client" + current_id + " is now on");

            sshHandler.close();
        }
    }

    private static void read_file(){
        try {
            FileReader fr = new FileReader(config_file);

            BufferedReader br = new BufferedReader(fr);

            String line = br.readLine();
            server_ip = line.split("=")[1];

            line = br.readLine();
            server_port = Integer.parseInt(line.split("=")[1]);

            line = br.readLine();
            num_readers = Integer.parseInt(line.split("=")[1]);
            readers_ip = new String[num_readers];

            for(int i = 0 ; i < num_readers; i++){
                line = br.readLine();
                readers_ip[i] = line.split("=")[1];
            }

            line = br.readLine();
            num_writers = Integer.parseInt(line.split("=")[1]);
            writers_ip = new String[num_writers];

            for(int i = 0 ; i < num_writers; i++){
                line = br.readLine();
                writers_ip[i] = line.split("=")[1];
            }

            line = br.readLine();
            num_access = Integer.parseInt(line.split("=")[1]);


        } catch (FileNotFoundException e) {
            System.out.println("File not Found");
        } catch (IOException e) {
            System.out.println("Error in the File");
        }
    }
}
