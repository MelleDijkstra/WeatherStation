import models.Measurement;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class WeatherStation {

    private Server s;
    //VMConnection vmc;
    //CorrectionModel cm;
    private LinkedBlockingQueue<String> queue;

    WeatherStation() {
        // TODO: setup server and autodiscovery
        // Making a ThreadSafe Queue
        queue = new LinkedBlockingQueue<>();
        s = new Server(queue);
    }

    public void run() {
        // Start the server
        s.start();
        // list to store measurements temporarily
        List<Measurement> measurementList;
        while (true) {
            try {
                // Constantly check if new XML messages are coming in
                String xml = queue.take();
                if (xml != null) {
                    // If there is an XML message, parse it. Parser returns Measurement objects from the XML
                    measurementList = Parser.parseFromXML(xml);
                    if (measurementList != null) {
                        System.out.println("\nTotal: " + measurementList.size() + "\n");
                        for (Measurement measurement : measurementList) {
                            System.out.println(measurement);
                        }
                    }
                    //List<Measurement> correctedMeasurements = cm.runCorrections(measurementList);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
