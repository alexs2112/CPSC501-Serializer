package deserializer;

import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;
import java.util.HashMap;
import java.util.List;
import java.lang.reflect.Constructor;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import helpers.CollectionHelper;

@SuppressWarnings("rawtypes")
public class Deserializer {
    public HashMap<Integer, Object> objects;

    public Object deserialize(Document document) {
        Object o = null;
        objects = new HashMap<Integer, Object>();
        mapElements(document);

        for (Element e : document.getRootElement().getChildren()) {
            if (e.getAttribute("length") != null) {
                deserializeArray(e);
            } else {
                int id;
                try {
                    id = e.getAttribute("id").getIntValue();
                } catch(DataConversionException err) {
                    System.out.println(err);
                    continue;
                }
                o = objects.get(id);

                if (CollectionHelper.isCollection(objects.get(id).getClass())) {
                    deserializeCollection(e, o);
                } else {
                    deserializeNormalObject(e, o);
                }
            }
        }

        try {
            int id = document.getRootElement().getChildren().get(0).getAttribute("id").getIntValue();
            o = objects.get(id);
        } catch(DataConversionException err) {
            System.out.println(err);
        } catch(IndexOutOfBoundsException err) { }
        return o;
    }

    @SuppressWarnings("unchecked")
    private void mapElements(Document document) {
        for (Element e : document.getRootElement().getChildren()) {
            String classString = e.getAttribute("class").getValue();
            try {
                Class c = Class.forName(classString);
                int id = e.getAttribute("id").getIntValue();
                if (c.isArray()) {
                    // Add an array of correct length full of default values to objects
                    int length = e.getAttribute("length").getIntValue();
                    Class type = c.getComponentType();
                    Object o = Array.newInstance(type, length);
                    objects.put(id, o);
                } else {
                    // Add the object by default constructor to objects
                    Constructor con = c.getDeclaredConstructor(new Class[0]);
                    con.setAccessible(true);
                    Object o = con.newInstance();
                    objects.put(id, o);
                }
            } catch(Exception err) {
                System.out.println(err);
                return;
            }
        }
    }

    private void deserializeNormalObject(Element e, Object o) {
        for (Element field : e.getChildren("field")) {
            // Read name and class from attributes
            String fieldName = field.getAttributeValue("name");
            String declaringClass = field.getAttributeValue("declaringclass");

            // Get a copy of the Class and Field
            Class c = null;
            try {
                c = Class.forName(declaringClass);
            } catch (ClassNotFoundException err) {
                System.out.println(err);
                continue;
            }

            Field f = null;
            try {
                f = c.getDeclaredField(fieldName);
                f.setAccessible(true);
            } catch(NoSuchFieldException err) {
                System.out.println(err);
                continue;
            }

            // Set the value of the field if primitive, otherwise point it to the object reference by ID
            if (field.getChildren("value").size() > 0) {
                String text = field.getChildren("value").get(0).getText();
                Object value = wrapObject(text, f.getType());
                try {
                    f.set(o, value);
                } catch(IllegalAccessException err) {
                    System.out.println(err);
                    continue;
                }
            } else if (field.getChildren("reference").size() > 0) {
                String value = field.getChildren("reference").get(0).getTextTrim();
                try {
                    int objId = Integer.valueOf(value);
                    f.set(o, objects.get(objId));
                } catch(NumberFormatException err) {
                    System.out.println(err);
                    continue;
                } catch(IllegalAccessException err) {
                    System.out.println(err);
                    continue;
                }
            }
        }
    }

    private void deserializeArray(Element e) {
        int id;
        int length;
        try {
            id = e.getAttribute("id").getIntValue();
            length = e.getAttribute("length").getIntValue();
        } catch(DataConversionException err) {
            System.out.println(err);
            return;
        }

        if (e.getChildren("value").size() > 0) {
            List<Element> children = e.getChildren("value");
            for (int i = 0; i < length; i++) {
                String text = children.get(i).getText();
                Object value = wrapObject(text, objects.get(id).getClass().getComponentType());
                try {
                    Array.set(objects.get(id), i, value);
                } catch(IllegalArgumentException err) {
                    System.out.println(err);
                    continue;
                }
            }
        } else if (e.getChildren("reference").size() > 0) {
            List<Element> children = e.getChildren("reference");
            for (int i = 0; i < length; i++) {
                String text = children.get(i).getTextTrim();
                try {
                    int objId = Integer.valueOf(text);
                    Array.set(objects.get(id), i, objects.get(objId));
                } catch(NumberFormatException err) {
                    System.out.println(err);
                    continue;
                } catch(IllegalArgumentException err) {
                    System.out.println(err);
                    continue;
                }
            }
        }
    }

    private void deserializeCollection(Element e, Object o) {
        if (e.getChildren("value").size() > 0) {
            List<Element> children = e.getChildren("value");
            for (Element child : children) {
                String text = child.getText();
                Object value = wrapObject(text, o.getClass());
                try {
                    CollectionHelper.add(o, value);
                } catch(IllegalArgumentException err) {
                    System.out.println(err);
                    continue;
                }
            }
        } else if (e.getChildren("reference").size() > 0) {
            List<Element> children = e.getChildren("reference");
            for (Element child : children) {
                String text = child.getTextTrim();
                try {
                    int objId = Integer.valueOf(text);
                    CollectionHelper.add(o, objects.get(objId));
                } catch(NumberFormatException err) {
                    System.out.println(err);
                    continue;
                } catch(IllegalArgumentException err) {
                    System.out.println(err);
                    continue;
                }
            }
        }
    }

    private Object wrapObject(String value, Class c) {
        if (c == null) { System.out.println(value); }
        if (c.getName().equals("boolean")) { return Boolean.valueOf(value); }
        else if (c.getName().equals("char")) { 
            if (value.toCharArray().length == 0) { return ' '; }
            return value.toCharArray()[0];
        }
        else if (c.getName().equals("byte")) { return Byte.valueOf(value); }
        else if (c.getName().equals("short")) { return Short.valueOf(value); }
        else if (c.getName().equals("int")) { return Integer.valueOf(value); }
        else if (c.getName().equals("long")) { return Long.valueOf(value); }
        else if (c.getName().equals("float")) { return Float.valueOf(value); }
        else if (c.getName().equals("double")) { return Double.valueOf(value); }
        else if (value.equals("null")) { return null; }
        else { return value; }
    }
}
