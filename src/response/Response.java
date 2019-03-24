package response;

import java.util.HashMap;
import java.util.Map;

public class Response {

    private Map<String, String> table;

    public Response(){

    }

    public Response(String line){
        table = new HashMap<>();
        String[] values = line.split(",");
        for(String prop: values){
            String[] propartyKeyAndValue = prop.split("=");
            table.put(propartyKeyAndValue[0], propartyKeyAndValue[1]);
        }
    }

    public String getValue(String key){
        return this.table.get(key);
    }

    public void addValue(String key, String value){
        table.put(key, value);
    }

    public String toString(){
        StringBuilder builder = new StringBuilder();
        for(Map.Entry<String, String> entry: table.entrySet()){
            builder.append(entry.getKey());
            builder.append("=");
            builder.append(entry.getValue());
        }
        return builder.toString();
    }

}
