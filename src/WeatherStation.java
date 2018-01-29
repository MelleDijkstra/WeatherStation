import models.Measurement;
import protobuf.WeatherstationV1;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The weatherstation
 */
public class WeatherStation {

    /**
     * Server object where clients can connect with and send measurement XML information
     */
    private Server s;
    //VMConnection vmc;
    //CorrectionModel cm;

    /**
     * Thread safe queue shared among client threads
     */
    private ConcurrentLinkedQueue<String> queue;

    /**
     * Running flag
     */
    private boolean running = true;

    WeatherStation() {
        // TODO: setup server and autodiscovery
        // Making a ThreadSafe Queue
        queue = new ConcurrentLinkedQueue<>();
        s = new Server(queue);
    }

    public void run() {
        // Start the server
        //s.startDiscovery();
        s.start();
        while (running) {
            // Constantly check if new XML messages are coming in
            String xml = queue.poll();
            if (xml != null) {
                // If there is an XML message, parse it. Parser returns Measurement objects from the XML
                List<Measurement> measurementList = Parser.parseFromXML(xml);
                if (measurementList != null) {
                    // Go through measurement list and check for errors
                    // TODO: correction
                    // Store measurement
                    for (Measurement m : measurementList) {
                        try {
                            File file = new File(m.getFilename());
                            file.getParentFile().mkdirs();
                            m.toProtobuf().writeTo(new FileOutputStream(file));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /**
     * Stop the weatherstation
     */
    public void stop() {
        running = false;
    }
}
