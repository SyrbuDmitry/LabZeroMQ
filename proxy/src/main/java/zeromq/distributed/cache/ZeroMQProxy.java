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
        byte[] message,id,dil;
// Switch messages between sockets
        while (!Thread.currentThread().isInterrupted()) {
// poll and memorize multipart detection
            items.poll();
            if (items.pollin(0)) {
//                while (true) {

                    id = frontend.recv(0);
                    dil = frontend.recv(0);
                    message = frontend.recv(0);
                    System.out.println(new String(id));
                    System.out.println(new String(dil));
                    System.out.println(new String(message));
                    backend.sendMore(frontend.recv(0));
                    backend.sendMore(frontend.recv(0));
                     backend.sendMore(frontend.recv(0));

//                    if (!more) {
//                        break;
//                    }
//                }
            }
            if (items.pollin(1)) {
                //while (true) {
                System.out.println();
                System.out.println("REP");
                    id = backend.recv(0);
                    System.out.println(new String(id));
                    backend.recv(0);
                    message = backend.recv(0);
                    System.out.println(new String(message));
                    if(new String(message).equals("World")){
                        frontend.sendMore(id);
                        frontend.sendMore("");
                        frontend.send(message);
                    }

//                    more = backend.hasReceiveMore();
                    //frontend.send(message, more ? ZMQ.SNDMORE : 0);
//                    if (!more) {
//                        break;
//                    }
                //}
            }
        }
    }
}
