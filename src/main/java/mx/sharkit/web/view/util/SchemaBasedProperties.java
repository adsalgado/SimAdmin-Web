package mx.sharkit.web.view.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author asalgado
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "parametros")
public class SchemaBasedProperties {

    @XmlTransient
    Map<String, Map<String, String>> properties;

    @XmlAnyElement(lax = true)
    List<Object> xmlmap;

    public Map<String, Map<String, String>> getProperties() {
        if (properties == null) {
            properties = new LinkedHashMap<>(); // I want same order
        }
        return properties;
    }

    boolean beforeMarshal(Marshaller m) {
        try {
            if (properties != null && !properties.isEmpty()) {
                if (xmlmap == null) {
                    xmlmap = new ArrayList<>();
                } else {
                    xmlmap.clear();
                }

                javax.xml.parsers.DocumentBuilderFactory dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
                javax.xml.parsers.DocumentBuilder db = dbf.newDocumentBuilder();
                org.w3c.dom.Document doc = db.newDocument();
                org.w3c.dom.Element element;

                Map<String, String> attrs;

                for (Map.Entry<String, Map<String, String>> it : properties.entrySet()) {
                    element = doc.createElement(it.getKey());
                    attrs = it.getValue();

                    if (attrs != null) {
                        for (Map.Entry<String, String> at : attrs.entrySet()) {
                            element.setAttribute(at.getKey(), at.getValue());
                        }
                    }

                    xmlmap.add(element);
                }
            } else {
                xmlmap = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    void afterUnmarshal(Unmarshaller u, Object p) {
        org.w3c.dom.Node node;
        org.w3c.dom.NamedNodeMap nodeMap;

        String name;
        Map<String, String> attrs;

        getProperties().clear();

        if (xmlmap != null) {
            for (Object xmlNode : xmlmap) {
                if (xmlNode instanceof org.w3c.dom.Node) {
                    node = (org.w3c.dom.Node) xmlNode;
                    nodeMap = node.getAttributes();

                    name = node.getLocalName();
                    attrs = new HashMap<>();

                    for (int i = 0, l = nodeMap.getLength(); i < l; i++) {
                        node = nodeMap.item(i);
                        attrs.put(node.getNodeName(), node.getNodeValue());
                    }

                    getProperties().put(name, attrs);
                }
            }
        }

        xmlmap = null;
    }

}
