package models;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

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
    public String station;

    /**
     * Datum van versturen van deze gegevens, formaat: yyyy-mm-dd
     * <p>Example:
     * {@code
     * <DATE>2009-09-13</DATE>
     * }
     * </p>
     */
    public String date;

    /**
     * Tijd van versturen van deze gegevens, formaat: hh:mm:ss
     * <p>Example:
     * {@code
     * <TIME>15:59:46</TIME>
     * }
     * </p>
     */
    public String time;

    /**
     * Temperatuur in graden Celsius, geldige waardes van -9999.9 t/m 9999.9 met 1 decimaal
     * <p>Example:
     * {@code
     * <TEMP>-60.1</TEMP>
     * }
     * </p>
     */
    public String temp;

    /**
     * Dauwpunt in graden Celsius, geldige waardes van -9999.9 t/m 9999.9 met 1 decimaal
     * <p>Example:
     * {@code
     * <DEWP>-58.1</DEWP>
     * }
     * </p>
     */
    public String dewPoint;

    /**
     * Luchtdruk op stationsniveau in millibar, geldige waardes van 0.0 t/m 9999.9 met 1 decimaal
     * <p>Example:
     * {@code
     * <STP>1034.5</STP>
     * }
     * </p>
     */
    public String stationAirPressure;

    /**
     * Luchtdruk op zeeniveau in millibar, geldige waardes van 0.0 t/m 9999.9 met 1 decimaal
     * <p>Example:
     * {@code
     * <SLP>1007.6</SLP>
     * }
     * </p>
     */
    public String seaAirPressure;

    /**
     * Zichtbaarheid in kilometers, geldige waardes van 0.0 t/m 999.9 met 1 decimaal
     * <p>Example:
     * {@code
     * <VISIB>123.7</VISIB>
     * }
     * </p>
     */
    public String visibilityRange;

    /**
     * Windsnelheid in kilometers per uur, geldige waardes van 0.0 t/m 999.9 met 1 decimaal
     * <p>Example:
     * {@code
     * <WDSP>10.8</WDSP>
     * }
     * </p>
     */
    public String windSpeed;

    /**
     * Neerslag in centimeters, geldige waardes van 0.00 t/m 999.99 met 2 decimalen
     * <p>Example:
     * {@code
     * <PRCP>11.28</PRCP>
     * }
     * </p>
     */
    public String precipitation;

    /**
     * Gevallen sneeuw in centimeters, geldige waardes van -9999.9 t/m 9999.9 met 1 decimaal
     * <p>Example:
     * {@code
     * <SNDP>11.1</SNDP>
     * }
     * </p>
     */
    public String snowFall;

    /**
     * Gebeurtenissen op deze dag, cummulatief, binair uitgedrukt.
     * Opeenvolgend, van meest- naar minst significant:
     * Vriezen, geeft aan of het gevroren heeft
     * Regen, geeft aan of het geregend heeft.
     * Sneeuw, geeft aan of het gesneeuwd heeft.
     * Hagel, geeft aan of het gehageld heeft.
     * Onweer, geeft aan of er onweer is geweest.
     * Tornado/windhoos, geeft aan of er een tornado of windhoos geweest is.
     * <p>Example:
     * {@code
     * <FRSHTT>010101</FRSHTT>
     * }
     * </p>
     */
    public String events;


    /**
     * Bewolking in procenten, geldige waardes van 0.0 t/m 99.9 met 1 decimaal
     * <p>Example:
     * {@code
     * <CLDC>87.4</CLDC>
     * }
     * </p>
     */
    // TODO: rename to overcast?
    public String cloudCoverage;


    /**
     * Windrichting in graden, geldige waardes van 0 t/m 359 alleen gehele getallen
     * <p>Example:
     * {@code
     * <WNDDIR>342</WNDDIR>
     * }
     * </p>
     */
    public String windDirection;

    public Measurement() {
    }

    public Measurement(Node node) {
        load(node);
    }

    @Override
    public String toString() {
        return String.format("%s - %s %s - tmp: %s dwp: %s spres: %s seapres: %s vr: %s wind: %s prec: %s snow: %s evt: %s cls: %s wnddir: %s",
                station, date, time, temp, dewPoint, stationAirPressure, seaAirPressure, visibilityRange, windSpeed,
                precipitation, snowFall, events, cloudCoverage, windDirection);
    }

    @Override
    public void load(Node node) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            // TODO: make int of this field
            station = getTagValue("STN", element);
            // TODO: make a datetime of these fields
            // TODO: make all these field their according value
            date = getTagValue("DATE", element);
            time = getTagValue("TIME", element);
            temp = getTagValue("TEMP", element);
            dewPoint = getTagValue("DEWP", element);
            stationAirPressure = getTagValue("STP", element);
            seaAirPressure = getTagValue("SLP", element);
            visibilityRange = getTagValue("VISIB", element);
            windSpeed = getTagValue("WDSP", element);
            precipitation = getTagValue("PRCP", element);
            snowFall = getTagValue("SNDP", element);
            events = getTagValue("FRSHTT", element);
            cloudCoverage = getTagValue("CLDC", element);
            windDirection = getTagValue("WNDDIR", element);
        }
    }
}
