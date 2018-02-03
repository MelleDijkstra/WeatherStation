package models;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import protobuf.WeatherstationV1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Class Measurement
 */
public class Measurement extends BaseModel implements Hydrate {

    /**
     * Het station waarvan deze gegevens zijn
     * <p>Example:
     * {@code
     * <STN>123456</STN>
     * }
     * </p>
     */
    public int station;

    /**
     * Datum van versturen van deze gegevens, formaat: yyyy-mm-dd
     * <p>Example:
     * {@code
     * <DATE>2009-09-13</DATE>
     * }
     * </p>
     */
    public LocalDateTime dateTime;

    /**
     * Temperatuur in graden Celsius, geldige waardes van -9999.9 t/m 9999.9 met 1 decimaal
     * <p>Example:
     * {@code
     * <TEMP>-60.1</TEMP>
     * }
     * </p>
     */
    public float temp;

    /**
     * Dauwpunt in graden Celsius, geldige waardes van -9999.9 t/m 9999.9 met 1 decimaal
     * <p>Example:
     * {@code
     * <DEWP>-58.1</DEWP>
     * }
     * </p>
     */
    public float dewPoint;

    /**
     * Luchtdruk op stationsniveau in millibar, geldige waardes van 0.0 t/m 9999.9 met 1 decimaal
     * <p>Example:
     * {@code
     * <STP>1034.5</STP>
     * }
     * </p>
     */
    public float stationAirPressure;

    /**
     * Luchtdruk op zeeniveau in millibar, geldige waardes van 0.0 t/m 9999.9 met 1 decimaal
     * <p>Example:
     * {@code
     * <SLP>1007.6</SLP>
     * }
     * </p>
     */
    public float seaAirPressure;

    /**
     * Zichtbaarheid in kilometers, geldige waardes van 0.0 t/m 999.9 met 1 decimaal
     * <p>Example:
     * {@code
     * <VISIB>123.7</VISIB>
     * }
     * </p>
     */
    public float visibilityRange;

    /**
     * Windsnelheid in kilometers per uur, geldige waardes van 0.0 t/m 999.9 met 1 decimaal
     * <p>Example:
     * {@code
     * <WDSP>10.8</WDSP>
     * }
     * </p>
     */
    public float windSpeed;

    /**
     * Neerslag in centimeters, geldige waardes van 0.00 t/m 999.99 met 2 decimalen
     * <p>Example:
     * {@code
     * <PRCP>11.28</PRCP>
     * }
     * </p>
     */
    public float precipitation;

    /**
     * Gevallen sneeuw in centimeters, geldige waardes van -9999.9 t/m 9999.9 met 1 decimaal
     * <p>Example:
     * {@code
     * <SNDP>11.1</SNDP>
     * }
     * </p>
     */
    public float snowFall;

    /**
     * Gebeurtenissen op deze dag, cummulatief, binair uitgedrukt.
     * Opeenvolgend, van meest- naar minst significant:
     * 0 = Vriezen, geeft aan of het gevroren heeft
     * 1 = Regen, geeft aan of het geregend heeft.
     * 2 = Sneeuw, geeft aan of het gesneeuwd heeft.
     * 3 = Hagel, geeft aan of het gehageld heeft.
     * 4 = Onweer, geeft aan of er onweer is geweest.
     * 5 = Tornado/windhoos, geeft aan of er een tornado of windhoos geweest is.
     * <p>Example:
     * {@code
     * <FRSHTT>010101</FRSHTT>
     * }
     * </p>
     */
    public int events;


    /**
     * Bewolking in procenten, geldige waardes van 0.0 t/m 99.9 met 1 decimaal
     * <p>Example:
     * {@code
     * <CLDC>87.4</CLDC>
     * }
     * </p>
     */
    // TODO: rename to overcast?
    public float cloudCoverage;


    /**
     * Windrichting in graden, geldige waardes van 0 t/m 359 alleen gehele getallen
     * <p>Example:
     * {@code
     * <WNDDIR>342</WNDDIR>
     * }
     * </p>
     */
    public float windDirection;

    public Measurement(Node node) {
        load(node);
    }

    @Override
    public String toString() {
        return String.format("%s - %s - tmp: %s dwp: %s spres: %s seapres: %s vr: %s wind: %s prec: %s snow: %s evt: %s cls: %s wnddir: %s",
                station, dateTime, temp, dewPoint, stationAirPressure, seaAirPressure, visibilityRange, windSpeed,
                precipitation, snowFall, events, cloudCoverage, windDirection);
    }

    @Override
    public void load(Node node) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            // TODO: make int of this field
            station = Integer.parseInt(getTagValue("STN", element));
            // TODO: make a datetime of these fields
            // TODO: make all these field their according value
            String datetime = getTagValue("DATE", element) + " " + getTagValue("TIME", element);
            this.dateTime = LocalDateTime.from(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").parse(datetime));
            temp = Float.valueOf(getTagValue("TEMP", element));
            dewPoint = Float.valueOf(getTagValue("DEWP", element));
            stationAirPressure = Float.valueOf(getTagValue("STP", element));
            seaAirPressure = Float.valueOf(getTagValue("SLP", element));
            visibilityRange = Float.valueOf(getTagValue("VISIB", element));
            windSpeed = Float.valueOf(getTagValue("WDSP", element));
            precipitation = Float.valueOf(getTagValue("PRCP", element));
            snowFall = Float.valueOf(getTagValue("SNDP", element));
            events = Integer.parseInt(getTagValue("FRSHTT", element));
            cloudCoverage = Float.valueOf(getTagValue("CLDC", element));
            windDirection = Float.valueOf(getTagValue("WNDDIR", element));
        }
    }

    public WeatherstationV1.Measurement toProtobuf() {
        return WeatherstationV1.Measurement.newBuilder()
                .setStation(station)
                .setDatetime((int) dateTime.atZone(ZoneId.systemDefault()).toEpochSecond())
                .setDewpoint(dewPoint)
                .setFallenSnow(snowFall)
                .setOvercast(cloudCoverage)
                .setPrecipitation(precipitation)
                .setSeaAirPressure(seaAirPressure)
                .setStationAirPressure(stationAirPressure)
                .setTemperature(temp)
                .setVisibility(visibilityRange)
                .setFreeze((events & 1) != 0) // check 0th bit
                .setRain((events & 1 << 1) != 0) // check 1th bit
                .setSnow((events & 1 << 2) != 0) // check 2th bit
                .setHail((events & 1 << 3) != 0) // check 3th bit
                .setStorm((events & 1 << 4) != 0) // check 4th bit
                .setTornado((events & 1 << 5) != 0) // check 5th bit
                .build();
    }

    public String getFilename(String path) {
        path = (path.endsWith(File.separator)) ? path : path + File.separatorChar;
        return String.format(path + "%s" + File.separatorChar + "%d" + File.separatorChar + "%d.dat",
                dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                station,
                dateTime.getHour());
    }

    public void saveToFile(String path) throws IOException {
        File file = new File(getFilename(path));
        File directory = file.getParentFile();
        // check if directory exists where we want to store the measurement
        if (!directory.exists()) {
            directory.mkdirs();
        }
        toProtobuf().writeTo(new FileOutputStream(file));
    }
}
