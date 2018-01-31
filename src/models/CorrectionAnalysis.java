package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CorrectionAnalysis {

    private FixedQueue<Measurement> measurementHistory;

    public CorrectionAnalysis() {
        measurementHistory = new FixedQueue<>(30);
    }

    /**
     *
     * @param measurement
     * @return The corrected measurement
     */
    public Measurement correct(Measurement measurement) {

        for (HashMap.Entry<Measurement.Fields, Float> entry : measurement.fields.entrySet()) {
            if (entry.getValue() == null) {
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
