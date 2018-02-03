/**
 * Created by jandu on 23/12/2017.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * WeatherStation Server
 */
public class Server extends Thread {

    /**
     * Port to serve on
     */
    private final int port;

    /**
     * Thread safe queue
     */
    private LinkedBlockingQueue<String> queue;

    /**
     * Flag for serving state
     */
    private boolean serving = true;

    /**
     * Server initialization
     * @param queue Thread safe queue to put socket messages in
     * @param port The port to listen on
     */
    Server(LinkedBlockingQueue<String> queue, int port) {
        this.port = port;
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            // Make a Cached Thread Pool
            ExecutorService executor = Executors.newCachedThreadPool();
            // ExecutorService doesn't have a method to retrieve the number of threads in the Thread Pool
            // That is why we cast the ExecutorService to a ThreadPoolExecutor so we can execute the method getPoolSize()
            ThreadPoolExecutor pool = (ThreadPoolExecutor) executor;
            ServerSocket server = new ServerSocket(port);
            System.out.println("Server starting on port: "+port);

            while (serving) {
                Socket s = server.accept();
                executor.execute(new SocketThread(s, queue));
                System.out.println("Number of threads in pool: " + pool.getPoolSize());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stop the server
     */
    public void stopServing() {
        serving = false;
    }
}

/**
 * WeatherStation socket thread
 * Handles every individual client
 */
class SocketThread extends Thread {

    /**
     * The socket connection with a client
     */
    private Socket s;

    /**
     * Thread safe queue
     */
    private LinkedBlockingQueue<String> queue;

    /**
     * SocketThread initialization
     * @param s The socket connection with client
     * @param queue The thread safe queue
     */
    SocketThread(Socket s, LinkedBlockingQueue<String> queue) {
        this.queue = queue;
        this.s = s;
    }

    @Override
    public void run() {
        try {
            // Get a reader which can read information from the socket communication
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            // Because we get the xml line by line we need to build a whole XML file
            StringBuilder xml = new StringBuilder();
            while (s.isConnected() && !isInterrupted()) {
                String line = in.readLine();
                if (line != null) {
                    xml.append(line);
                    if (xml.toString().endsWith("</WEATHERDATA>")) {
                        // A whole XML message is received, put it in the queue to have another thread process it
                        queue.put(xml.toString());
                        xml.setLength(0);
                    }
                } else {
                    in.close();
                    s.close();
                    break;
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
