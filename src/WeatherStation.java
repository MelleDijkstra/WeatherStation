import models.Measurement;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WeatherStation {

    Server s;
    Parser p;
    //VMConnection vmc;
    //CorrectionModel cm;
    private Queue<String> queue;



    WeatherStation() {
        // TODO: setup server and autodiscovery
        // Making a ThreadSafe Queue
        queue = new ConcurrentLinkedQueue<>();
        s = new Server(queue);
    }

    public void run() {
        s.start();
        while(true) {
            String xml = queue.poll();
            if(xml != null) {
                List<Measurement> measurementList = p.parse(xml);
                //List<Measurement> correctedMeasurements = cm.runCorrections(measurementList);
            }
        }
    }
}
