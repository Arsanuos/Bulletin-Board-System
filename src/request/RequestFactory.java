package request;

import constants.Settings;

public class RequestFactory {

    public static AbstractReq getInstance(String line){
        String type = line.split(",")[0].split("=")[1];
        if(type.equals(Settings.READ_REQ)){
            return new ReadReq(line);
        }else if(type.equals(Settings.WRITE)){
            return new WriteReq(line);
        }
        throw new RuntimeException("Not a valid request type");
    }
}
