package zeromq.distributed.cache;

import java.util.Random;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;
import org.zeromq.ZContext;

/**
 * ROUTER-TO-REQ example
 */
public class rtdealer
{

    /**
     * While this example runs in a single process, that is just to make
     * it easier to start and stop the example. Each thread has its own
     * context and conceptually acts as a separate process.
     */
    public static void main(String[] args) throws Exception
    {
        ZMQ.Context context = ZMQ.context(1);
// Socket to talk to server
        Socket requester = context.socket(SocketType.REQ);
        requester.connect("tcp://localhost:5559");
        System.out.println("launch and connect client.");
            requester.send("PUT 0 A", 0);
            requester.send("PUT 1 B", 0);
            requester.send("GET 0", 0);
            String message = requester.recvStr(0);
            System.out.println(message);
// We never get here but clean up anyhow requester.close();
        context.term();
    }
}