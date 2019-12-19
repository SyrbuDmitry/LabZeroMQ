package zeromq.distributed.cache;

public class CacheSegment {
    private int start, end;
    private byte[] id;
    public static int count = 0;
    public int idcount = 0;
    CacheSegment(String start, String end, byte[] id){
        this.start = Integer.parseInt(start);
        this.end = Integer.parseInt(end);
        this.id = id;
        count++;
        idcount = count;
    }

    public int getEnd() {
        return end;
    }

    public int getStart() {
        return start;
    }

    public byte[] getId() {
        return id;
    }

    public  int getCount() {
        return idcount;
    }
}
