import models.CorrectionAnalysis;
import models.Measurement;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WeatherStation {

    private Server s;
    CorrectionAnalysis correction;
    private ConcurrentLinkedQueue<String> queue;

    WeatherStation() {
        // TODO: setup server and autodiscovery
        // Making a ThreadSafe Queue
        queue = new ConcurrentLinkedQueue<>();
        correction = new CorrectionAnalysis();
        s = new Server(queue);
    }

    public void run() {
        // Start the server
        s.start();
        while(true) {
            // Constantly check if new XML messages are coming in
            String xml = queue.poll();
            if(xml != null) {
                // If there is an XML message, parse it. Parser returns Measurement objects from the XML
                List<Measurement> measurementList = Parser.parseFromXML(xml);
                if(measurementList != null) {
                    for (Measurement m : measurementList) {
                        Measurement correctedMeasurement = correction.correct(m);
                    }
                    //correction.storeMeasurements();
                }
                //List<Measurement> correctedMeasurements = cm.runCorrections(measurementList);
            }
        }
    }

    public void storeMeasurements() {
        // TODO: store measurements on
        //measurementHistory.add();
    }

    }

