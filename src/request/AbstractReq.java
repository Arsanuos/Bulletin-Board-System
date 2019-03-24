package request;

import java.util.Map;

public abstract class AbstractReq {

    private String type;
    private Map<String, String> map;


    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("type=");
        builder.append(this.type);
        for(Map.Entry<String, String> entry: map.entrySet()){
            builder.append(entry.getKey());
            builder.append("=");
            builder.append(entry.getValue());
        }
        return builder.toString();
    }


}
