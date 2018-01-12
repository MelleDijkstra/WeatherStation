/**
 * Created by jandu on 23/12/2017.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public void run() {
        try {
            ServerSocket server = new ServerSocket(3301);
            System.out.print("Server started...");

            while (true) {
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
            StringBuilder xml = new StringBuilder();
            while (s.isConnected() && !isInterrupted()) {
                String line = in.readLine();
                if (line != null) {
                    xml.append(line);
                    if (xml.toString().endsWith("</WEATHERDATA>")) {
                        // processing of message is done in same thread, reading will have to wait
                        return xml.toString();
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
