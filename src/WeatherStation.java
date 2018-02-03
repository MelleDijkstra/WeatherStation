import models.Measurement;
import protobuf.WeatherstationV1;

import java.util.List;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The weatherstation
 */
public class WeatherStation {

    /**
     * Server object where clients can connect with and send measurement XML information
     */
    private Server s;

    /**
     * Thread safe queue shared among client threads
     */
    private LinkedBlockingQueue<String> queue;

    /**
     * Running flag
     */
    private boolean running = true;

    /**
     * The location where to store weather data
     */
    private File storageLocation;

    WeatherStation(Properties settings) throws FileNotFoundException {
        // retrieve port number from settings file
        int port = Integer.parseInt(settings.getProperty("port"));
        storageLocation = new File(settings.getProperty("storage_location"));
        if(!storageLocation.exists() || !storageLocation.isDirectory()) {
            throw new FileNotFoundException("Given storage location does not exist or is not a directory, path given: "+storageLocation.getAbsolutePath());
        }

        // A thread safe queue which is shared among multiple threads
        queue = new LinkedBlockingQueue<>();
        s = new Server(queue, port);
    }

    public void run() {
        // Start the server
        s.start();
        // list to store measurements temporarily
        List<Measurement> measurementList;
        while (running) {
            try {
                // Constantly check if new XML messages are coming in
                String xml = queue.take();
                if (xml != null) {
                    // If there is an XML message, parse it. Parser returns Measurement objects from the XML
                    measurementList = Parser.parseFromXML(xml);
                    if (measurementList != null) {
                        // Go through measurement list and check for errors
                        // TODO: correction
                        // Store measurement
                        for (Measurement m : measurementList) {
                            m.saveToFile(storageLocation.getAbsolutePath());
                        }
                    }
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
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
