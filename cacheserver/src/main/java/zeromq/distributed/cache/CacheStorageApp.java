package zeromq.distributed.cache;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;

public class CacheStorageApp {
    public static void main(String[] args){
        ZMQ.Context context = ZMQ.context (1);
// Socket to talk to server
        ZMQ.Socket dealer = context.socket (SocketType.DEALER);
        dealer.bind("tcp://*:5560");
        Storage cache = new Storage();
        while (!Thread.currentThread ().isInterrupted ()) {
// Wait for next request from client
            String req = dealer.recvStr (0);
            System.out.printf ("Received request: [%s]\n", req); // Do some 'work'
            try {
                Thread.sleep (1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Send reply back to client
            if(req.equals("get"))
                dealer.send (cache.getValue(1));
            if(req.equals("put"))
                cache.putValue(4,"D");
        }
// We never get here but clean up anyhow responder.close();
        context.term();
    }
}
