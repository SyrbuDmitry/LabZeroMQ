package zeromq.distributed.cache;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;

import java.util.Arrays;

public class CacheStorageApp {
    public static void main(String[] args) {
        ZMQ.Context context = ZMQ.context(1);
// Socket to talk to server
        ZMQ.Socket responder = context.socket(SocketType.DEALER);
        responder.connect("tcp://localhost:5560");

        responder.sendMore("");
        responder.send("N 0 3");
        byte[] client, message;
        System.out.println("NOTIFY SENT");
        while (!Thread.currentThread().isInterrupted()) {
// Wait for next request from client
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            responder.recvStr();
            client = responder.recv();
            responder.recvStr();
            message = responder.recv();
            System.out.println("REQUEST FROM " + Arrays.toString(client) +" MESSAGE ["+
            new String(message)+"]");

            responder.sendMore("");
            responder.sendMore(client);
            responder.sendMore("");
            responder.send("SUCCESSFUL PUT");

        }
// We never get here but clean up anyhow
        responder.close();
        context.term();
    }
}