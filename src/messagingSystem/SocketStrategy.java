package messagingSystem;

import request.AbstractReq;
import request.RequestFactory;
import response.Response;
import java.io.*;
import java.net.Socket;

public class SocketStrategy implements Strategy{

    private Socket socket;

    public SocketStrategy(Socket socket){
        this.socket = socket;
    }


    @Override
    public void sendReq(AbstractReq req){
        try {
            send(req.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendResponse(Response res) {
        try {
            send(res.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void send(String data) throws IOException {
        BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"));
        wr.write(data);
        wr.write("/r/n");
        wr.flush();
        wr.close();
    }

    private String recieve(){
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
            return ret.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Not a valid response.");
    }

    @Override
    public Response getResponse(){
        return new Response(recieve());
    }

    @Override
    public AbstractReq getReq() {
        return RequestFactory.getInstance(recieve());
    }

}
