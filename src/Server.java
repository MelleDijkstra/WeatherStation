/**
 * Created by jandu on 23/12/2017.
 */

import java.net.*;
import java.io.*;

public class Server {
    private int portNumber = 3301;

    public void run() {
        try {
            ServerSocket server = new ServerSocket(this.portNumber);
            System.out.print("Server started...");

            while(true) {
                Socket s = server.accept();
                new SocketThread(s).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class SocketThread extends Thread {

    private Socket s;

    SocketThread(Socket s) {
        this.s = s;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            while (s.isConnected() && !this.isInterrupted()) {
                String msg = in.readLine();
                if(msg == null) break;
                System.out.println(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
