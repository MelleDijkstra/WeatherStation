package models;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

abstract class BaseModel {

    /**
     * Get tag value from Element
     * @param tag The tag of the XML Element
     * @param element The actual element
     * @return The node value as string
     */
    static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node != null ? node.getNodeValue() : null;
    }

}
