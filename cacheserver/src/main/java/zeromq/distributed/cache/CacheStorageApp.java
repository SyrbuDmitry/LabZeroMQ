package zeromq.distributed.cache;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;

public class CacheStorageApp {
    public static void main(String[] args){
        ZMQ.Context context = ZMQ.context (1);
// Socket to talk to server
        ZMQ.Socket dealer = context.socket (SocketType.DEALER);
        dealer.connect("tcp://localhost:5560");
        System.out.println("bind to localhost:5560");
//        dealer.sendMore("");
//        dealer.send("NOTIFY 0 5");
        Storage cache = new Storage();
        System.out.println("START LISTENING TO POLLS");
        while (!Thread.currentThread ().isInterrupted ()) {
            String req = dealer.recvStr();
            System.out.printf ("Received request: [%s]\n", req); // Do some 'work'
            try {
                Thread.sleep (1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String [] reqPar = req.split(" ");
            if(reqPar[0].equals("GET")){
                dealer.sendMore("");
                dealer.send(cache.getValue(Integer.parseInt(reqPar[1])));
            }
           if(reqPar[0].equals("PUT")){
                System.out.println("REQUEST PUT FROM CLIENT");
                cache.putValue(Integer.parseInt(reqPar[1]),reqPar[2]);
                dealer.send("");
            }
        }
        context.term();
    }
}
