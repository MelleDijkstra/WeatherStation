import models.Measurement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
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

    Parser(File file) {
        try {
            DocumentBuilder docBuilder = getDocBuilder();
            Document doc = docBuilder.parse(file);
            List<Measurement> measurementsList = this.getMeasurementsFromXML(doc);
            //lets print Employee list information
            for (Measurement m : measurementsList) {
                System.out.println(m);
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    Parser(String xml) {
        try {
            DocumentBuilder docBuilder = getDocBuilder();
            InputSource is = new InputSource(new StringReader(xml));
            Document doc = docBuilder.parse(is);
            List<Measurement> measurements = getMeasurementsFromXML(doc);
            System.out.println(String.format("Total measurements (%s):", measurements.size()));
            for (Measurement measurement : measurements) {
                System.out.println("\t"+measurement);
            }
            System.out.println();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    private DocumentBuilder getDocBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        return docFactory.newDocumentBuilder();
    }

    private List<Measurement> getMeasurementsFromXML(Document doc) {
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
