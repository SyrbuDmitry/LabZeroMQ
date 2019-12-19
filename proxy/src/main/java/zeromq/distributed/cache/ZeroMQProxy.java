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
        boolean more = false;
        byte[] message, client = null, server = null;
        List<byte[]> frames = new ArrayList<>();
        List<CacheSegment> serverList = new ArrayList<>();
// Switch messages between sockets
        while (!Thread.currentThread().isInterrupted()) {
// poll and memorize multipart detection
            items.poll();
            if (items.pollin(0)) {
                System.out.println("REQUEST");
                frames.clear();
                while (true) {
                    message = frontend.recv();
                    System.out.println(new String(message));
                    frames.add(message);
                    more = frontend.hasReceiveMore();
                    //backend.send(message, more ? ZMQ.SNDMORE : 0);
                    if (!more) {
                        break;
                    }
                }

                    System.out.println("SENDING  TO ");
                System.out.println("-------------");
                    System.out.println("SERVER "+ Arrays.toString(serverList.get(0).getId())); //server
                    backend.sendMore(serverList.get(0).getId());

                    System.out.println("");
                    backend.sendMore("");

                    System.out.println("CLIENT "+ Arrays.toString(frames.get(0)));     //client
                    backend.sendMore(frames.get(0));

                    System.out.println("");
                    backend.sendMore("");

                    System.out.println("MESSAGE "+new String(frames.get(2)));
                    backend.send(frames.get(2),0);
                    System.out.println("-------------");
                    System.out.println("SENT");
                }
                if (items.pollin(1)) {
                    System.out.println("REP");
                    frames.clear();
                    while (true) {
                        message = backend.recv();
                        frames.add(message);
                        System.out.println(new String(message));
                        more = backend.hasReceiveMore();
                        //backend.send(message, more ? ZMQ.SNDMORE : 0);
                        if (!more) {
                            break;
                        }
                    }
                        if (frames.size() == 3) {
                            //String[] msgPars = frames.get(2).split(" ");
                            serverList.add(new CacheSegment("0", "5", frames.get(0)));
                        } else {
                            frontend.sendMore(frames.get(2));
                            frontend.sendMore("");
                            frontend.send(frames.get(4));
                        }
//                    server = backend.recv(0);
//                    backend.recv(0);
//                    message = backend.recv(0);
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
