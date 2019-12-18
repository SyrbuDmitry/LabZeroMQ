package zeromq.distributed.cache;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;

public class CacheStorageApp {
    public static void main(String[] args) {
        ZMQ.Context context = ZMQ.context(1);
// Socket to talk to server
        ZMQ.Socket responder = context.socket(SocketType.DEALER);
        responder.connect("tcp://localhost:5560");
        while (!Thread.currentThread().isInterrupted()) {
            responder.recvStr (0);
            String string = responder.recvStr (0);

            System.out.printf ("Received request: [%s]\n", string); // Do some 'work'
            try {
                Thread.sleep (1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
// Send reply back to client
            responder.send ("World");
        }
// We never get here but clean up anyhow
        responder.close();
        context.term();
    }
}