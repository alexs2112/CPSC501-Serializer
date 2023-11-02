package helpers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;

@SuppressWarnings("rawtypes")
public class CollectionHelper {
    public static boolean isCollection(Class c) {
        for (Class i : c.getInterfaces()) {
            if (i == Collection.class) { return true; }
            if (isCollection(i)) { return true; }
        }
        Class s = c.getSuperclass();
        if (s != null) {
            if (s == Collection.class) { return true; }
            if (isCollection(s)) { return true; }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public static Iterator<Object> getIterator(Object obj) {
        try {
            Method m = Collection.class.getMethod("iterator", new Class[0]);
            return (Iterator<Object>)m.invoke(obj, new Object[0]);
        } catch(NoSuchMethodException err) {
            System.out.println(err);
            return null;
        } catch(IllegalAccessException err) {
            System.out.println(err);
            return null;
        } catch(InvocationTargetException err) {
            System.out.println(err);
            return null;
        }
    }

    public static int getSize(Object obj) {
        try {
            Method m = Collection.class.getMethod("size", new Class[0]);
            return (int)m.invoke(obj, new Object[0]);
        } catch(NoSuchMethodException err) {
            System.out.println(err);
            return 0;
        } catch(IllegalAccessException err) {
            System.out.println(err);
            return 0;
        } catch(InvocationTargetException err) {
            System.out.println(err);
            return 0;
        }
    }

    public static void add(Object host, Object o) {
        try {
            Method m = Collection.class.getMethod("add", Object.class );
            m.invoke(host, o);
        } catch(NoSuchMethodException err) {
            System.out.println(err);
        } catch(IllegalAccessException err) {
            System.out.println(err);
        } catch(InvocationTargetException err) {
            System.out.println(err);
        }
    }
}
