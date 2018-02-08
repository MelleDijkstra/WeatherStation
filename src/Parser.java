import models.Measurement;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

class Parser {

    private static DocumentBuilder docBuilder;

    private Parser() {
    }

    @Nullable
    public static List<Measurement> parseFromFile(File file) {
        try {
            DocumentBuilder docBuilder = getDocBuilder();
            Document doc = docBuilder.parse(file);
            return getMeasurementsFromXML(doc);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static List<Measurement> parseFromXML(String xml) {
        try {
            DocumentBuilder docBuilder = getDocBuilder();
            InputSource is = new InputSource(new StringReader(xml));
            Document doc = docBuilder.parse(is);
            return getMeasurementsFromXML(doc);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static DocumentBuilder getDocBuilder() throws ParserConfigurationException {
        if (docBuilder == null) {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docBuilder = docFactory.newDocumentBuilder();
        }
        return docBuilder;
    }

    private static List<Measurement> getMeasurementsFromXML(Document doc) {
        doc.getDocumentElement().normalize();
        // retrieve all measurements nodes
        NodeList nodeList = doc.getElementsByTagName("MEASUREMENT");
        List<Measurement> measurementsList = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            // go through each node and pass it to a new Measurement which will hydrate himself
            measurementsList.add(new Measurement(nodeList.item(i)));
        }
        return measurementsList;
    }
}
