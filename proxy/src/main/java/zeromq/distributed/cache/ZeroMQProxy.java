package zeromq.distributed.cache;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ZeroMQProxy {


    public static void main(String[] args) {
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket frontend = context.socket(SocketType.ROUTER);
        ZMQ.Socket backend = context.socket(SocketType.ROUTER);
        frontend.bind("tcp://*:5559");
        backend.bind("tcp://*:5560");
        System.out.println("launch and connect broker.");
// Initialize poll set
        ZMQ.Poller items = context.poller(2);
        items.register(frontend, ZMQ.Poller.POLLIN);
        items.register(backend, ZMQ.Poller.POLLIN);
        boolean more = false;
        byte[] message,client=null,server=null;
// Switch messages between sockets
        while (!Thread.currentThread().isInterrupted()) {
// poll and memorize multipart detection
            items.poll();
            if (items.pollin(0)) {
                System.out.println("REQUEST");

                    client = frontend.recv(0);

                    frontend.recv(0);
                    message = frontend.recv(0);
                    System.out.println(new String(client));
                    
                    backend.sendMore(server);
                    backend.sendMore("");
                    backend.sendMore(client);
                    backend.sendMore("");
                    backend.send(message);

                System.out.println();
            }
            if (items.pollin(1)) {

                    server = backend.recv(0);
                    backend.recv(0);
                    message = backend.recv(0);
//                    if (new String(server).equals("N"))
//                    System.out.println(Arrays.toString(message));
//                    more = backend.hasReceiveMore();
//                    frontend.send(message, more ? ZMQ.SNDMORE : 0);
//                    if (!more) {
//                        break;
//                    }
                System.out.println();
            }
        }
    }
}
