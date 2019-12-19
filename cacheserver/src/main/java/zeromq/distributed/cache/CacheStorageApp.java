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
        Storage cache = new Storage();
        responder.sendMore("");
        responder.send("NOTIFY 0 3");
        byte[] client;
        String[] message;
        String value;
        while (!Thread.currentThread().isInterrupted()) {

// Wait for next request from client
            
            System.out.println("PROCESSING");
            responder.recvStr();
            client = responder.recv();
            responder.recvStr();
            message = responder.recvStr().split(" ");

            if(message[0].equals("PUT")){
                cache.putValue(message[1],message[2]);
                responder.sendMore("");
                responder.sendMore(client);
                responder.sendMore("");
                responder.send("SUCCESSFUL PUT");
            }

            if(message[0].equals("GET")){
                value = cache.getValue(message[1]);
                responder.sendMore("");
                responder.sendMore(client);
                responder.sendMore("");
                responder.send(value);
            }
        }

        responder.close();
        context.term();
    }
}