import java.io.IOException;
import java.net.*;
import java.util.Enumeration;

public class Discoverer {

    private InetAddress foundAddress;

    public void discover() {
        // find the server using UDP broadcast
        try {
            // open a random port to send the package
            DatagramSocket c = new DatagramSocket();
            c.setBroadcast(true);

            byte[] sendData = "DISCOVER_WEATHERSTATION_REQUEST".getBytes();

            // try the 255.255.255.255 first
            try {
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("255.255.255.255"), 8888);
                c.send(sendPacket);
                System.out.println(getClass().getName() + ">>> Request packet sent to: 255.255.255.255 (DEFAULT)");
            } catch (Exception e) {
                e.printStackTrace();
            }

            // broadcast the message over all the network interfaces
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();

                if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                    continue; // don't want to broadcast to the loopback interface or when network interface is disabled
                }

                for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                    InetAddress broadcast = interfaceAddress.getBroadcast();
                    if (broadcast == null) {
                        continue;
                    }

                    // send the broadcast package!
                    try {
                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcast, 8888);
                        c.send(sendPacket);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    System.out.println(getClass().getName() + ">>> Request packet sent to: " + broadcast.getHostAddress() + "; Interface: " + networkInterface.getDisplayName());
                }
            }

            System.out.println(getClass().getName() + ">>> Done looping over all network interfaces. Now waiting for a reply!");

            // wait for a response
            byte[] recvBuf = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
            c.receive(receivePacket);

            // we have a response
            System.out.println(getClass().getName() + ">>> Broadcast response from server: " + receivePacket.getAddress().getHostAddress());

            // check if the message is correct
            String message = new String(receivePacket.getData()).trim();
            if (message.equals("DISCOVER_WEATHERSTATION_RESPONSE")) {
                // DO SOMETHING WITH THE SERVER'S IP (for example, store it in your controller)
                foundAddress = receivePacket.getAddress();

                Socket s = new Socket(foundAddress, 3301);
                if(s.isConnected()) {
                    System.out.println("Connected to service!!");
                }
            }

            // cleanup
            c.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public InetAddress getFoundAddress() {
        return foundAddress;
    }
}
