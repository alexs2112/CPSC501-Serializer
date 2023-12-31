package serializer;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import org.jdom2.Document;
import org.jdom2.Element;
import helpers.FieldHelper;
import helpers.CollectionHelper;

@SuppressWarnings("rawtypes")
public class Serializer {
    ObjectMap objects;

    public Document serialize(Object obj) {
        Element root = new Element("serialized");
        Document doc = new Document(root);

        objects = new ObjectMap();
        objects.populate(obj);

        // Ensure that the given object is always the first one serialized
        Element e = serializeObject(obj);
        root.addContent(e);

        for (Integer i : objects.getObjects().keySet()) {
            if (i == obj.hashCode()) { continue; }
            e = serializeObject(objects.get(i));
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
        } else if (CollectionHelper.isCollection(obj.getClass())) {
            serializeCollection(obj, e);
        } else {
            serializeNormalObject(obj, e);
        }

        return e;
    }

    private void serializeNormalObject(Object obj, Element element) {
        Field[] fields = FieldHelper.findFields(obj.getClass());
        if (fields.length == 0) { return; }
        for(Field f : fields) {
            if (Modifier.toString(f.getModifiers()).contains("static")) { continue; }
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
        e.setAttribute("declaringclass", getDeclaringClass(obj.getClass(), f));

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

    private void serializeCollection(Object obj, Element element) {
        Iterator<Object> iterator = CollectionHelper.getIterator(obj);
        while (iterator.hasNext()) {
            Object o = iterator.next();
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

    private String getDeclaringClass(Class c, Field f) {
        if (c == null) { return ""; }
        for (Field f2 : c.getDeclaredFields()) {
            if (f.equals(f2)) { return c.getName(); }
        }
        return getDeclaringClass(c.getSuperclass(), f);
    }
}
