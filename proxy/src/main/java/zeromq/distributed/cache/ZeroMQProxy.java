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
        byte[] message;
// Switch messages between sockets
        while (!Thread.currentThread().isInterrupted()) {
// poll and memorize multipart detection
            items.poll();
            if (items.pollin(0)) {
                    message = frontend.recv(0);
                    more = frontend.hasReceiveMore();
                    backend.send(message, more ? ZMQ.SNDMORE : 0);
                    if (!more) {
                        break;
                    }
            }
            if (items.pollin(1)) {
                    String id = backend.recvStr();
                    backend.recvStr();
                    String msg = backend.recvStr();
                    String [] msgParams = parseString(msg);
                    if(msgParams[0].equals("NOTIFY")) {
                        CacheSegment insert = new CacheSegment(Integer.parseInt(msgParams[1]), Integer.parseInt(msgParams[2]), id);
                        serverList.add(insert);
                    }else {
                        
                        frontend.send(msg);
                    }
            }

        }

    }
    private static String[] parseString(String str){
        return str.split(" ");
    }
    private static void checkList(){

    }
}
