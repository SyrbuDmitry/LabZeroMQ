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
        String message,identity;

// Switch messages between sockets
        while (!Thread.currentThread().isInterrupted()) {
                items.poll();
// poll and memorize multipart detection items.poll();
            if (items.pollin(0)) {
                System.out.println("REQUEST");
                identity = frontend.recvStr();
                frontend.recvStr();
                message = frontend.recvStr();
                System.out.println(message);

                backend.send(message);
            }
            if (items.pollin(1)) {
                System.out.println("RESPONSE");
                identity = backend.recvStr(0);
                backend.recvStr(0);
                message = backend.recvStr(0);
                String [] sarr = message.split(" " );
                if(sarr[0].equals("NOTIFY")) {
                    serverList.add(new CacheSegment(sarr[1], sarr[2], identity));
                    //System.out.println(serverList.get(serverList.size()-1).getCount());
                }
                else {
                    frontend.sendMore(identity);
                    frontend.sendMore("");
                    frontend.send(message);
                }
            }
        }
    }
}
