package zeromq.distributed.cache;

public class CacheSegment {
    private int start, end;
    private String id;
    CacheSegment(int start, int end, String id){
        this.start = start;
        this.end = end;
        this.id = id;
    }

    public int getEnd() {
        return end;
    }

    public int getStart() {
        return start;
    }

    public String getId() {
        return id;
    }
}
