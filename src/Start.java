import configuration.Configuration;

import java.io.*;

public class Start {

    private static int server_port;
    private static String server_ip;
    private static int num_readers;
    private static String[] readers_ip; // we might need them later
    private static int num_writers;
    private static String[] writers_ip; // we might need them later
    private static int num_access;

    private static final String config_file = "system.properties";

    public static void main(String[] args) throws IOException {
        // load configuration

        //Configuration configuration = Configuration.getInstance();

        // read files
        read_file();

        // start server as separate program.
        run_server();

        // start clients as separate programs.
        run_clients();
    }

    public static void run_server() throws IOException {

        // compile server code
        Runtime.getRuntime().exec("javac server.java");

        // calc number of incoming requests
        int num_requests = (num_readers + num_writers) * num_access;

        // run server code
        Runtime.getRuntime().exec("java server " + server_port + " "
                + num_requests);
    }

    public static void run_clients() throws IOException {

        // compile client code
        Runtime.getRuntime().exec("javac client.java");

        int current_id = 0;

        // run client code, args -> type, id, server_address, server_port, num_access
        for(int i = 0 ; i < num_readers; i++){
            Runtime.getRuntime().exec("java client 1 " + current_id + " "
                    + server_ip + " " + server_ip + " " + num_access);
            current_id++;
        }

        for(int i = 0 ; i < num_writers; i++){
            Runtime.getRuntime().exec("java client 0 " + current_id + " "
                    + server_ip + " " + server_ip + " " + num_access);
            current_id++;
        }

    }

    public static void read_file(){
        try {
            FileReader fr = new FileReader(config_file);

            BufferedReader br = new BufferedReader(fr);

            String line = br.readLine();
            server_ip = line.split("=")[1];

            line = br.readLine();
            server_port = Integer.parseInt(line.split("=")[1]);

            num_readers = Integer.parseInt(line.split("=")[1]);
            readers_ip = new String[num_readers];

            for(int i = 0 ; i < num_readers; i++){
                line = br.readLine();
                readers_ip[i] = line.split("=")[1];
            }

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
