package messagingSystem;


import request.AbstractReq;
import response.Response;

import java.net.Socket;

public interface Strategy {

    public void sendReq(AbstractReq req);

    public void sendResponse(Response res);

    public Response getResponse();

    public AbstractReq getReq();

    public void setSocket(Socket socket);
}
