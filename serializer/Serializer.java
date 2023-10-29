package serializer;

import org.jdom2.Document;
import org.jdom2.Element;
import java.util.HashMap;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@SuppressWarnings("rawtypes")
public class Serializer {
    private HashMap<Integer, Object> objects;
    private static final Class[] wrapperClasses = new Class[] {
        Boolean.class,
        Character.class,
        Byte.class,
        Short.class,
        Integer.class,
        Long.class,
        Float.class,
        Double.class,
        Void.class,
        java.lang.String.class
    };

    public Document serialize(Object obj) {
        populateMap(obj);

        objects = new HashMap<Integer, Object>();
        Element root = new Element("serialized");
        Document doc = new Document(root);

        return doc;
    }

    /* Recursively get all objects associated with obj */
    private void populateMap(Object obj) {
        objects.put(obj.hashCode(), obj);

        Field[] fields = findFields(obj.getClass());
        for (Field f : fields) {
            Object value;
            try {
                value = f.get(obj);
            } catch (NullPointerException e) {
                continue;
            } catch (IllegalAccessException e) {
                continue;
            }

            if (f.getType().isArray()) {
                // Handle array
            } else {
                // If it isn't a primitive object, populate it if it isn't already in the list
                if (isPrimitive(value)) { continue; }
                if (objects.containsKey(value.hashCode())) { continue; }

                populateMap(value);
            }
        }
    }

    /* Recursively get all public and protected fields of the object (including superclass) */
    /* Method copied from Assignment 2 */
    private Field[] findFields(Class c) { return findFields(c, false); }
    private Field[] findFields(Class c, boolean isSuperclass) {
        Field[] fs = c.getDeclaredFields();

        for (int i = 0; i < fs.length; i++) {
            Field f = fs[i];
            if (Modifier.isProtected(f.getModifiers())) {
                f.setAccessible(true);
            }
            if (!isSuperclass && Modifier.isPrivate(f.getModifiers())) {
                f.setAccessible(true);
            }
        }

        if (c.getSuperclass() == null) { return fs; }

        return concat(fs, findFields(c.getSuperclass(), true));
    }

    /* Concatenate two arrays of fields as this isn't just default java implementation */
    /* Method copied from Assignment 2 */
    private Field[] concat(Field[] a, Field[] b) {
        int alen = a.length;
        if (alen == 0) { return b; }
        
        int blen = b.length;
        if (blen == 0) { return a; }

        int len = alen + blen;
        Field[] f = new Field[len];
        for (int i = 0; i < alen; i++) {
            f[i] = a[i];
        }
        for (int i = 0; i < blen; i++) {
            f[i+alen] = b[i];
        }
        return f;
    }

    /* Check if an object is a wrapper object (eg. Integer instead of int) */
    /* Method copied from Assignment 2 */
    private boolean isPrimitive(Object o) {
        for (Class c : wrapperClasses) {
            if (o.getClass() == c) { return true; }
        }
        return false;
    }
}
