package serializer;

import org.jdom2.Document;
import org.jdom2.Element;

public class Serializer {
    ObjectMap objects;

    public Document serialize(Object obj) {
        objects = new ObjectMap();
        objects.populate(obj);

        Element root = new Element("serialized");
        Document doc = new Document(root);

        return doc;
    }

}