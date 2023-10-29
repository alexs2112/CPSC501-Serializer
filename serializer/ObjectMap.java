package serializer;

import java.util.HashMap;
import java.util.ArrayList;
import java.lang.reflect.Field;
import java.lang.reflect.Array;
import serializer.helpers.FieldHelper;

@SuppressWarnings("rawtypes")
public class ObjectMap {
    private HashMap<Integer, Object> objects;
    public HashMap<Integer, Object> getObjects() { return objects; }
    public Object get(int i) { return objects.get(i); }

    public ObjectMap() {
        objects = new HashMap<Integer, Object>();
    }

    /* Recursively get all objects associated with obj */
    public void populate(Object obj) {
        objects.put(obj.hashCode(), obj);

        Field[] fields = FieldHelper.findFields(obj.getClass());
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
        if (FieldHelper.isPrimitive(o)) { return false; }
        if (objects.containsKey(o.hashCode())) { return false; }
        return true;
    }
}
