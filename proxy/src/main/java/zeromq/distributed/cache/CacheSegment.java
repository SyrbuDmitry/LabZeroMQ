package zeromq.distributed.cache;

public class CacheSegment {
    private int start, end;
    CacheSegment(int start, int end){
        this.start = start;
        this.end = end;
    }

    public int getEnd() {
        return end;
    }

    public int getStart() {
        return start;
    }
}
