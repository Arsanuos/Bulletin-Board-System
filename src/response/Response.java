package response;

import java.util.HashMap;
import java.util.Map;

public class Response {

    private Map<String, String> table;

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

}
