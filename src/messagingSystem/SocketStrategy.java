package messagingSystem;

import request.AbstractReq;
import response.Response;
import java.io.*;
import java.net.Socket;

public class SocketStrategy implements Strategy{

    private Socket socket;

    public SocketStrategy(){
        this.socket = SocketConnection.getInstance();
    }


    @Override
    public void sendReq(AbstractReq req){
        try {
            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"));
            wr.write(req.toString());
            wr.write("/r/n");
            wr.flush();
            wr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void sendResponse(Response res) {

    }

    @Override
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

    @Override
    public AbstractReq getReq() {
        return null;
    }

}
