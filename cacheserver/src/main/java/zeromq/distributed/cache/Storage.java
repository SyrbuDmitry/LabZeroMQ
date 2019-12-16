package zeromq.distributed.cache;

import java.util.HashMap;
import java.util.Map;

public class Storage {
    private Map<Integer,String> storageCache =  new HashMap<>();
    Storage(){
        storageCache.put(1,"A");
        storageCache.put(2,"B");
        storageCache.put(3,"C");
    }
    public String getValue(Integer key){
        return storageCache.get(key);
    }
    public void putValue(Integer key, String val){
        storageCache.put(key, val);
    }
}
