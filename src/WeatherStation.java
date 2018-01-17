import models.Measurement;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WeatherStation {

    private Server s;
    //VMConnection vmc;
    //CorrectionModel cm;
    private ConcurrentLinkedQueue<String> queue;

    WeatherStation() {
        // TODO: setup server and autodiscovery
        // Making a ThreadSafe Queue
        queue = new ConcurrentLinkedQueue<>();
        s = new Server(queue);
    }

    public void run() {
        // Start the server
        s.startDiscovery();
        //s.start();
        while(true) {
            // Constantly check if new XML messages are coming in
            String xml = queue.poll();
            if(xml != null) {
                // If there is an XML message, parse it. Parser returns Measurement objects from the XML
                List<Measurement> measurementList = Parser.parseFromXML(xml);
                if(measurementList != null) {
                    System.out.println("\nTotal: "+measurementList.size()+"\n");
                    for (Measurement measurement : measurementList) {
                        System.out.println(measurement);
                    }
                }
                //List<Measurement> correctedMeasurements = cm.runCorrections(measurementList);
            }
        }
    }
}
