package messagingSystem;

import response.Response;

import javax.management.RuntimeErrorException;
import java.io.*;
import java.net.Socket;

public class SocketStrategy {

    private Socket socket;

    public SocketStrategy(){
        this.socket = SocketConnection.getInstance();
    }


    public void sendReq(String data){
        try {
            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"));
            wr.write("/r/n");
            wr.write(data);
            wr.flush();
            wr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Response getResponse(){
        try{
            BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            StringBuilder ret = new StringBuilder();
            //Response will be comma separted of the fields
            //Response will parse them and put them in a map.
            while ((line = rd.readLine()) != null) {
                ret.append(line);
            }
            rd.close();
            return new Response(ret.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Not a valid response.");
    }

}
