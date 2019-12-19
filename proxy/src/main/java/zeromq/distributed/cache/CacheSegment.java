package zeromq.distributed.cache;

public class CacheSegment {
    private int start, end;
    private byte[] id;
    public static int count = 0;
    public int idcount = 0;
    CacheSegment(byte[] msg, byte[] id){
        String[] message = new String(msg).split(" ");
        this.start = Integer.parseInt(message[0]);
        this.end = Integer.parseInt(message[1]);
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
