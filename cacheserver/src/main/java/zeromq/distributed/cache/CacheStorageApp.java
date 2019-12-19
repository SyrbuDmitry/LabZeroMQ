package zeromq.distributed.cache;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;

public class CacheStorageApp {
    public static void main(String[] args) {
        ZMQ.Context context = ZMQ.context(1);
// Socket to talk to server
        ZMQ.Socket responder = context.socket(SocketType.DEALER);
        responder.connect("tcp://localhost:5560");

        responder.sendMore("");
        responder.send("N 0 3");
        String client, message;
        System.out.println("NOTIFY SENT");
        while (!Thread.currentThread().isInterrupted()) {
// Wait for next request from client
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            responder.recvStr();
            message = responder.recvStr();
            System.out.println(message);
        }
// We never get here but clean up anyhow
        responder.close();
        context.term();
    }
}