public class Main {
    public static void main(String[] args) {
        // Setup a server which will listen for incoming XML messages from different stations
        WeatherStation w = new WeatherStation();
        w.run();
    }
}
