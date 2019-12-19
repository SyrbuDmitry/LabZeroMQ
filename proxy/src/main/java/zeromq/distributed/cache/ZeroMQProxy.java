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
        backend.bind("tcp://localhost:5560");
        System.out.println("launch and connect broker.");
// Initialize poll set
        ZMQ.Poller items = context.poller(2);
        items.register(frontend, ZMQ.Poller.POLLIN);
        items.register(backend, ZMQ.Poller.POLLIN);
        boolean more;
        byte[] message;
        List<byte[]> frames = new ArrayList<>();
        List<CacheSegment> serverList = new ArrayList<>();
// Switch messages between sockets
        while (!Thread.currentThread().isInterrupted()) {
// poll and memorize multipart detection
            items.poll();
            if (items.pollin(0)) {
                frames.clear();
                while (true) {
                    message = frontend.recv();
                    frames.add(message);
                    more = frontend.hasReceiveMore();
                    if (!more) {
                        break;
                    }
                }
                backend.sendMore(serverList.get(0).getId());
                backend.sendMore("");
                backend.sendMore(frames.get(0));
                backend.sendMore("");
                backend.send(frames.get(2), 0);
            }
            if (items.pollin(1)) {
                frames.clear();
                while (true) {
                    message = backend.recv();
                    frames.add(message);
                    more = backend.hasReceiveMore();
                    if (!more) {
                        break;
                    }
                }
                if (frames.size() == 3) {
                    serverList.add(new CacheSegment(frames.get(2), frames.get(0)));
                } else {
                    frontend.sendMore(frames.get(2));
                    frontend.sendMore("");
                    frontend.send(frames.get(4));
                }
            }
        }
    }
}
