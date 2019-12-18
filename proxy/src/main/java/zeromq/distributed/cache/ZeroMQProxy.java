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
        byte[] message,id,dil,server=null,client=null;
// Switch messages between sockets
        while (!Thread.currentThread().isInterrupted()) {
// poll and memorize multipart detection
            items.poll();
            if (items.pollin(0)) {
                System.out.println();
                System.out.println("REQ");
//                while (true) {
                    id = frontend.recv(0);
                    dil = frontend.recv(0);
                    message = frontend.recv(0);
                    client = id;
                System.out.println(new String(id));
                System.out.println(new String(dil));
                System.out.println(new String(message));
                System.out.println();
                    backend.sendMore(server);
                    backend.sendMore(dil);
                    backend.send(message,0);

//                    if (!more) {
//                        break;
//                    }
//                }
            }
            if (items.pollin(1)) {
                System.out.println();
                System.out.println("RESP");
                //while (true) {
                id = backend.recv(0);
                dil = backend.recv(0);
                message = backend.recv(0);
                System.out.println(new String(id));
                System.out.println(new String(dil));
                System.out.println(new String(message));
                System.out.println();
                if(new String(message).equals("N"))
                    server = id;
                else {
                    System.out.printf ("Received response: [%s]\n", new String(message));
                    frontend.sendMore(client);
                    frontend.sendMore("");
                    frontend.send(message);
                }
//                   more = backend.hasReceiveMore();
                    //frontend.send(message, more ? ZMQ.SNDMORE : 0);
//                    if (!more) {
//                        break;
//                    }
                //}
            }
        }
    }
}
