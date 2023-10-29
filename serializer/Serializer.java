package serializer;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import org.jdom2.Document;
import org.jdom2.Element;

public class Serializer {
    ObjectMap objects;

    public Document serialize(Object obj) {
        Element root = new Element("serialized");
        Document doc = new Document(root);

        objects = new ObjectMap();
        objects.populate(obj);
        for (Integer i : objects.getObjects().keySet()) {
            Element e = serializeObject(objects.get(i));
            root.addContent(e);
        }

        return doc;
    }

    private Element serializeObject(Object obj) {
        Element e = new Element("object");
        e.setAttribute("class", obj.getClass().getName());
        e.setAttribute("id", Integer.toString(obj.hashCode()));
        if (obj.getClass().isArray()) {
            e.setAttribute("length", Integer.toString(Array.getLength(obj)));
        } else {
            serializeFields(obj, e);
        }

        return e;
    }

    private void serializeFields(Object obj, Element element) {
        Field[] fields = ObjectMap.findFields(obj.getClass());
        if (fields.length == 0) { return; }
        for(Field f : fields) {
            Object value;
            try {
                value = f.get(obj);
            } catch (NullPointerException except) {
                value = null;
            } catch (IllegalAccessException except) {
                continue;
            }

            if (f.getType().isArray()) {
                // Parse array fields
            } else {
                Element e = serializeNormalField(obj, f, value);
                element.addContent(e);
            }
        }
    }

    private Element serializeNormalField(Object obj, Field f, Object value) {
        Element e = new Element("field");
        e.setAttribute("name", f.getName());
        e.setAttribute("declaringclass", obj.getClass().getName());
        
        if (value == null) {
            Element v = new Element("value");
            v.addContent("null");
            e.addContent(v);
        } else if (ObjectMap.isPrimitive(value)) {
            Element v = new Element("value");

            // Special handling for chars because the null char breaks jdom
            if (value.getClass() == Character.class) {
                if ((char)value == '\0') {
                    char c = ' ';
                    value = c;
                }
            }

            v.addContent(value.toString());
            e.addContent(v);
        } else {
            Element r = new Element("reference");
            r.addContent(Integer.toString(value.hashCode()));
            e.addContent(r);
        }
        return e;
    }
}
