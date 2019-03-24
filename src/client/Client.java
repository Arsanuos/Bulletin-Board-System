package client;

import messagingSystem.SocketStrategy;
import request.AbstractReq;
import response.Response;


public abstract class Client {

    public void sendReq(AbstractReq req, SocketStrategy socketStrategy){
        socketStrategy.sendReq(req.toString());
    }

    public Response getResponse(SocketStrategy socketStrategy){
        return socketStrategy.getResponse();
    }
}
