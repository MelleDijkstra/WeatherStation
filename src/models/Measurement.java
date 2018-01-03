package models;

public class Measurement extends BaseModel {

    public String station;
    public String date;
    public String time;

    public Measurement() {

    }

    @Override
    public String toString() {
        return String.format("%s - %s %s", station, date, time);
    }
}
