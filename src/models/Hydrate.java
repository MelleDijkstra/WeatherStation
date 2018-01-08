package models;

import org.w3c.dom.Node;

public interface Hydrate {

    /**
     * Fills this object with information from node
     * @param node The node with information for this object
     */
    void load(Node node);

}
