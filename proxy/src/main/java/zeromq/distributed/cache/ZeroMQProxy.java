package zeromq.distributed.cache;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;

import java.util.ArrayList;
import java.util.List;

public class ZeroMQProxy {


    public static void main(String[] args) {

        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket frontend = context.socket(SocketType.ROUTER);
        ZMQ.Socket backend = context.socket(SocketType.DEALER);
        frontend.bind("tcp://*:5559");
        backend.bind("tcp://*:5560");
        System.out.println("launch and connect broker.");
// Initialize poll set
        ZMQ.Poller items = context.poller(2);
        items.register(frontend, ZMQ.Poller.POLLIN);
        items.register(backend, ZMQ.Poller.POLLIN);
        boolean more = false;
        byte[] message;
// Switch messages between sockets
        while (!Thread.currentThread().isInterrupted()) {
// poll and memorize multipart detection items.poll();

        }

    }
}
