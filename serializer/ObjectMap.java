package serializer;

import java.util.HashMap;
import java.util.ArrayList;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Array;

@SuppressWarnings("rawtypes")
public class ObjectMap {
    private HashMap<Integer, Object> objects;
    public HashMap<Integer, Object> getObjects() { return objects; }
    public Object get(int i) { return objects.get(i); }
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

    public ObjectMap() {
        objects = new HashMap<Integer, Object>();
    }

    /* Recursively get all objects associated with obj */
    public void populate(Object obj) {
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

            if (value == null) { continue; }
            if (f.getType().isArray()) {
                // Handle array
                int length = Array.getLength(value);
                for (int i = 0; i < length; i++) {
                    Object o = Array.get(value, i);
                    if (!isValidObject(o)) { continue; }

                    populate(o);
                }
            } else if (value.getClass() == ArrayList.class) {
                // Handle ArrayList from java.util.Collections
                ArrayList l = (ArrayList)value;
                int length = l.size();
                for (int i = 0; i < length; i++) {
                    Object o = l.get(i);
                    if (!isValidObject(o)) { continue; }

                    populate(o);
                }
            } else {
                // If it isn't a primitive object, populate it if it isn't already in the list
                if (!isValidObject(value)) { continue; }
                populate(value);
            }
        }
    }

    private boolean isValidObject(Object o) {
        if (o == null) { return false; }
        if (isPrimitive(o)) { return false; }
        if (objects.containsKey(o.hashCode())) { return false; }
        return true;
    }

    /* Recursively get all public and protected fields of the object (including superclass) */
    /* Method copied from Assignment 2 */
    public static Field[] findFields(Class c) { return findFields(c, false); }
    private static Field[] findFields(Class c, boolean isSuperclass) {
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
    private static Field[] concat(Field[] a, Field[] b) {
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
    public static boolean isPrimitive(Object o) {
        for (Class c : wrapperClasses) {
            if (o.getClass() == c) { return true; }
        }
        return false;
    }
}
