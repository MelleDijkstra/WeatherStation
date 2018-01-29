/**
 * Created by jandu on 23/12/2017.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            ServerSocket server = new ServerSocket(3301);
            System.out.println("Server started...");

            while (true) {
                Socket s = server.accept();
                executor.execute(new SocketThread(s, queue));
                System.out.println("Number of threads in pool: " + pool.getPoolSize());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startDiscovery() {
        new AutoDiscovery().start();
    }
}

class AutoDiscovery extends Thread {

    private boolean stopDiscovery = false;

    @Override
    public void run() {
        try {
            //Keep a socket open to listen to all the UDP traffic that is destined for this port
            DatagramSocket udp = new DatagramSocket(8888, InetAddress.getByName("0.0.0.0"));
            udp.setBroadcast(true);

            while (!stopDiscovery) {
                //Receive a packet
                byte[] recvBuf = new byte[1024];
                DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
                System.out.println("Started discovery...");
                udp.receive(packet);

                //Packet received
                System.out.println(getClass().getName() + ">>>Discovery packet received from: " + packet.getAddress().getHostAddress());
                System.out.println(getClass().getName() + ">>>Packet received; data: " + new String(packet.getData()));

                //See if the packet holds the right command (message)
                String message = new String(packet.getData()).trim();
                if (message.equals("DISCOVER_WEATHERSTATION_REQUEST")) {
                    byte[] sendData = "DISCOVER_WEATHERSTATION_RESPONSE".getBytes();

                    //Send a response
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(), packet.getPort());
                    udp.send(sendPacket);

                    System.out.println(getClass().getName() + ">>>Sent packet to: " + sendPacket.getAddress().getHostAddress());
                    stopDiscovery = true;
                }
            }

            start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void stopDiscovery() {
        stopDiscovery = true;
    }

}

class SocketThread extends Thread {

    private Socket s;
    private ConcurrentLinkedQueue<String> queue;

    SocketThread(Socket s, ConcurrentLinkedQueue<String> queue) {
        this.queue = queue;
        this.s = s;
    }

    @Override
    public void run() {
        try {
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
