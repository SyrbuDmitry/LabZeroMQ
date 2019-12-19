package zeromq.distributed.cache;

import java.util.HashMap;
import java.util.Map;

public class Storage {
    private Map<String,String> storageCache =  new HashMap<>();
    Storage(){
    }
    public String getValue(String key){
        return storageCache.get(key);
    }
    public void putValue(String key, String val){
        storageCache.put(key, val);
    }
}
