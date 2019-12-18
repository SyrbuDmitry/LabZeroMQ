package zeromq.distributed.cache;

public class CacheSegment {
    private int start, end;
    private String id;
    CacheSegment(String start, String end, String id){
        this.start = Integer.parseInt(start);
        this.end = Integer.parseInt(end);
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
