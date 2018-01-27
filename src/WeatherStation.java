import models.Measurement;
import protobuf.WeatherstationV1;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
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
        WeatherstationV1.Measurement m = WeatherstationV1.Measurement.newBuilder()
                .setStation(1234567)
                .setDatetime((int) (System.currentTimeMillis() / 1000L))
                .build();
        System.out.println("Protobuf works - measurement size: " + m.getSerializedSize());
        try {
            // write the data to file
            Calendar c = GregorianCalendar.getInstance();
            c.setTime(new Date());
            System.out.println("hour: " + c.get(Calendar.HOUR_OF_DAY));

            m.writeTo(new FileOutputStream(c.get(Calendar.HOUR_OF_DAY) + ".dat"));
            // read the data from file
            WeatherstationV1.Measurement m2 = WeatherstationV1.Measurement.parseFrom(new FileInputStream(c.get(Calendar.HOUR_OF_DAY) + ".dat"));
            System.out.println("Measurement read from file: " + new Date((long) m2.getDatetime() * 1000));
        } catch (IOException e) {
            System.out.println("Could not create file!!");
            e.printStackTrace();
        }
    }

    public void run() {
        // Start the server
        s.startDiscovery();
        //s.start();
        while (true) {
            // Constantly check if new XML messages are coming in
            String xml = queue.poll();
            if (xml != null) {
                // If there is an XML message, parse it. Parser returns Measurement objects from the XML
                List<Measurement> measurementList = Parser.parseFromXML(xml);
                if (measurementList != null) {
                    System.out.println("\nTotal: " + measurementList.size() + "\n");
                    for (Measurement measurement : measurementList) {
                        System.out.println(measurement);
                    }
                }
                //List<Measurement> correctedMeasurements = cm.runCorrections(measurementList);
            }
        }
    }
}
