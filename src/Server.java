/**
 * Created by jandu on 23/12/2017.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Server extends Thread {

    private ConcurrentLinkedQueue<String> queue;

    Server(ConcurrentLinkedQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            System.out.println("Starting Cached Thread Pool");
            // Make a Cached Thread Pool
            ExecutorService executor = Executors.newCachedThreadPool();
            // ExecutorService doesn't have a method to retrieve the number of threads in the Thread Pool
            // That is why we cast the ExecutorService to a ThreadPoolExecutor so we can execute the method getPoolSize()
            ThreadPoolExecutor pool = (ThreadPoolExecutor) executor;
            ServerSocket server = new ServerSocket(7789);
            System.out.print("Server started...");

            while (true) {
                Socket s = server.accept();
                executor.execute(new SocketThread(s, queue));
                System.out.println("Number of threads in pool: " + pool.getPoolSize());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class SocketThread extends Thread {

    private Socket s;
    private ConcurrentLinkedQueue<String> queue;
    private static int nth = 0;
    // Increase the id with 1 when new Thread is created
    private final int id = ++nth;

    SocketThread(Socket s, ConcurrentLinkedQueue<String> queue) {
        this.queue = queue;
        this.s = s;
    }

    @Override
    public void run() {
        try {
            System.out.println("Starting thread: " + id);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            StringBuilder xml = new StringBuilder();
            while (s.isConnected() && !isInterrupted()) {
                String line = in.readLine();
                if (line != null) {
                    xml.append(line);
                    if (xml.toString().endsWith("</WEATHERDATA>")) {
                        // processing of message is done in same thread, reading will have to wait
                        queue.offer(xml.toString());
                        xml.setLength(0);
                        System.out.println("Number of items in queue: " + queue.size());
                        System.out.println("Finished by thread: " + id);
                    }
                } else {
                    in.close();
                    s.close();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
