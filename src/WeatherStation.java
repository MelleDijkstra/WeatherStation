import models.Measurement;

import java.util.List;
import java.util.Queue;

public class WeatherStation {

    Server s;
    Parser p;
    VMConnection vmc;
    CorrectionModel cm;
    Queue queue;


    WeatherStation() {
        // TODO: setup server and autodiscovery
        s = new Server(queue);
    }

    public run() {
        String xml = s.run();
        List<Measurement> measurementList = p.parse(xml);
        List<Measurement> correctedMeasurements = cm.runCorrections(measurementList);
    }

}
