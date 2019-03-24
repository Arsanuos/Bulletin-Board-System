package messagingSystem;


import request.AbstractReq;
import response.Response;

public interface Strategy {

    public void sendReq(AbstractReq req);

    public void sendResponse(Response res);

    public Response getResponse();

    public AbstractReq getReq();
}
