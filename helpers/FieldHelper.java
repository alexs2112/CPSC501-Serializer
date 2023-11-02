package helpers;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@SuppressWarnings("rawtypes")
public class FieldHelper {
    public static final Class[] wrapperClasses = new Class[] {
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
