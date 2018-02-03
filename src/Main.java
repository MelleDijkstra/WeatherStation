import java.io.*;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        // Settings which the application needs
        Properties settings = new Properties(getDefaultProperties());
        try {
            File file = new File("weatherstation.properties");
            if (file.exists()) {
                settings.load(new FileInputStream(file));
            } else {
                // If there are no settings, store the default settings
                getDefaultProperties().store(new FileWriter(file), null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(String.format("Storing weather data files at: %s", settings.getProperty("storage_location")));

        // Setup a WeatherStation Server which will listen for incoming XML messages from different stations
        try {
            WeatherStation w = new WeatherStation(settings);
            w.run();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private static Properties getDefaultProperties() {
        Properties defaults = new Properties();
        defaults.setProperty("port", "7789");
        defaults.setProperty("storage_location", File.separatorChar + "mnt" + File.separatorChar + "remotenfs" + File.separatorChar);
        return defaults;
    }
}
