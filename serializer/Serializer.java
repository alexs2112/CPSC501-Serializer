package serializer;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import org.jdom2.Document;
import org.jdom2.Element;
import serializer.helpers.FieldHelper;

@SuppressWarnings("rawtypes")
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
            serializeArray(obj, e);
        } else if (obj.getClass() == ArrayList.class) {
            e.setAttribute("size", Integer.toString(((ArrayList)obj).size()));
            serializeArrayList(obj, e);
        } else {
            serializeNormalObject(obj, e);
        }

        return e;
    }

    private void serializeNormalObject(Object obj, Element element) {
        Field[] fields = FieldHelper.findFields(obj.getClass());
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

            Element e = serializeField(obj, f, value);
            element.addContent(e);
        }
    }

    private Element serializeField(Object obj, Field f, Object value) {
        Element e = new Element("field");
        e.setAttribute("name", f.getName());
        e.setAttribute("declaringclass", obj.getClass().getName());

        Element v = serializeValue(value);
        e.addContent(v);
        return e;
    }

    private void serializeArray(Object obj, Element element) {
        int length = Array.getLength(obj);
        for (int i = 0; i < length; i++) {
            Object o = Array.get(obj, i);
            Element value = serializeValue(o);
            element.addContent(value);
        }
    }

    private void serializeArrayList(Object obj, Element element) {
        ArrayList list = (ArrayList)obj;
        for (int i = 0; i < list.size(); i++) {
            Object o = list.get(i);
            Element value = serializeValue(o);
            element.addContent(value);
        }
    }

    private Element serializeValue(Object o) {
        if (o == null) {
            Element f = new Element("value");
            f.addContent("null");
            return f;
        } else if (FieldHelper.isPrimitive(o)) {
            // Special handling for chars because the null char breaks jdom
            if (o.getClass() == Character.class) {
                if ((char)o == '\0') {
                    char c = ' ';
                    o = c;
                }
            }
            Element f = new Element("value");
            f.addContent(o.toString());
            return f;
        } else {
            Element r = new Element("reference");
            r.addContent(Integer.toString(o.hashCode()));
            return r;
        }
    }
}
