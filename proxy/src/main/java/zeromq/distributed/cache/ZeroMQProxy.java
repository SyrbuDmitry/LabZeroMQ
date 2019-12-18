package zeromq.distributed.cache;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;

import java.util.ArrayList;
import java.util.List;

public class ZeroMQProxy {


    public static void main(String[] args) {
        List<CacheSegment> serverList = new ArrayList<>();
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
        String message, identity;

// Switch messages between sockets
        while (!Thread.currentThread().isInterrupted()) {
            items.poll();
// poll and memorize multipart detection items.poll();
            if (items.pollin(0)) {
                    System.out.println("REQ");
                    identity = frontend.recvStr(0);
                    System.out.println(identity);
                    frontend.recvStr(0);
                    message = frontend.recvStr(0);
                    backend.sendMore(identity);
                    backend.sendMore("");
                    backend.send(message);
            }
            if (items.pollin(1)) {
                while (true) {
                    message = frontend.recvStr(0);
                    System.out.println(message);
                    more = frontend.hasReceiveMore();
                    backend.send(message, more ? ZMQ.SNDMORE : 0);
                    if (!more) {
                        break;
                    }
                }
            }
        }
    }
}
