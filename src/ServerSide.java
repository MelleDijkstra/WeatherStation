/**
 * Created by jandu on 23/12/2017.
 */

import java.net.*;
import java.io.*;

public class ServerSide {
    public static void main(String[] args) throws Exception {
        try{
            ServerSocket server=new ServerSocket(8888);
            System.out.println("Server Started ....");
            while(true){
                Socket serverClient=server.accept();  //server accept the client connection request
                System.out.println("Connection Established");
                ServerClientThread sc = new ServerClientThread(serverClient); //send  the request to a separate thread
                sc.start();
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }
}


class ServerClientThread extends Thread {
    Socket ServerClientThread;

    ServerClientThread(Socket inSocket){
        ServerClientThread = inSocket;
    }
    public void run(){
        try{
            // Parsing the XML
        }catch(Exception ex){
            System.out.println(ex);
        }
    }
}
