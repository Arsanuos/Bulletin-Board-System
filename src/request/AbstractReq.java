package request;

import java.util.Map;

public abstract class AbstractReq {

    private String type;
    private Map<String, String> map;

    public AbstractReq(String line){
        String[] arr = line.split(",");
        for(String s: arr){
            String[] tmp = s.split("=");
            map.put(tmp[0], tmp[1]);
        }
    }

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
