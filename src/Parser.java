import models.Measurement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class Parser {

    private String filename;

    Parser(String filename) {
        this.filename = filename;

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;

        try {
            docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(this.filename);
            doc.getDocumentElement().normalize();
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nodeList = doc.getElementsByTagName("MEASUREMENT");
            //now XML is loaded as Document in memory, lets convert it to Object List
            List<Measurement> measurementsList = new ArrayList<>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                measurementsList.add(parseMeasurement(nodeList.item(i)));
            }
            //lets print Employee list information
            for (Measurement m : measurementsList) {
                System.out.println(m);
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    private Measurement parseMeasurement(Node node) {
        Measurement m = new Measurement();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            m.station = getTagValue("STN", element);
            // TODO: make a datetime of this field
            m.date = getTagValue("DATE", element);
            m.time = getTagValue("TIME", element);
        }

        return m;
    }

    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }

}
