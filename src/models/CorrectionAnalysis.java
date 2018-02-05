package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CorrectionAnalysis {
    Measurement measurement;
    private FixedQueue<Measurement> measurementHistory;

    public CorrectionAnalysis() {
        measurementHistory = new FixedQueue<>(30);
        measurement = new Measurement();
    }

    /**
     *
     * @param measurement
     * @return The corrected measurement
     */
    public Measurement correct(Measurement measurement) {
        for (HashMap.Entry<Measurement.Fields, Float> entry : measurement.fields.entrySet()) {
            // check if value is null and if there is data to make a correction
            if (entry.getValue() == null && measurementHistory.isEmpty()){
                // remove value because no data yet to make a correction
                measurement.fields.remove(entry.getKey());
            // check if value is null or if value has too much deviation
            } else if (entry.getValue() == null) {
                Float tot = 0f;
                for (Measurement m : measurementHistory) {
                    tot += m.fields.get(entry.getKey());
                }
                Float avg = tot / measurementHistory.size();
                measurement.fields.put(entry.getKey(), avg);
            }
        }
        return measurement;
    }

    public int getHistoricalMeasurementCount() {
        return measurementHistory.size();
    }

}
