package object_creator.helpers;

import asciiPanel.AsciiPanel;
import object_creator.classes.*;

public class ObjectHelper {
    public static String getTypeString(ObjectType o) {
        Class c = o.getClass();
        if (c == PrimitiveObject.class) {
            return "Primitive";
        } else if (c == PrimitiveArray.class) {
            return "Primitive Array";
        } else if (c == ReferenceObject.class) {
            return "Reference";
        } else if (c == ReferenceArray.class) {
            return "Reference Array";
        } else if (c == ReferenceList.class) {
            return "Reference List";
        }
        return "";
    }

    public static String[] getFields(ObjectType o) {
        Class c = o.getClass();
        if (c == PrimitiveObject.class) {
            return getPrimitiveFields();
        } else if (c == PrimitiveArray.class) {
            return getPrimitiveArrayFields();
        } else if (c == ReferenceObject.class) {
            return getReferenceFields();
        } else if (c == ReferenceArray.class) {
            return getReferenceArrayFields();
        } else if (c == ReferenceList.class) {
            return getReferenceListFields();
        }
        return new String[0];
    }

    public static void displayObject(ObjectType o, AsciiPanel terminal, int x, int y) {
        Class c = o.getClass();
        if (c == PrimitiveObject.class) {
            displayPrimitive((PrimitiveObject)o, terminal, x, y);
        } else if (c == PrimitiveArray.class) {
            displayPrimitiveArray((PrimitiveArray)o, terminal, x, y);
        } else if (c == ReferenceObject.class) {
            displayReference((ReferenceObject)o, terminal, x, y);
        } else if (c == ReferenceArray.class) {
            displayReferenceArray((ReferenceArray)o, terminal, x, y);
        } else if (c == ReferenceList.class) {
            displayReferenceList((ReferenceList)o, terminal, x, y);
        }
    }

    private static String[] getPrimitiveFields() {
        return new String[] {
            "NAME",
            "byte b",
            "char c",
            "double d",
            "float f",
            "int i",
            "long j",
            "short s",
            "boolean z"
        };
    }
    
    private static String[] getPrimitiveArrayFields() {
        return new String[] {
            "NAME",
            "int[] ints",
            "double[] doubles",
            "boolean[] bools"
        };
    }

    private static String[] getReferenceFields() {
        return new String[] {
            "NAME",
            "Object A",
            "Object B",
            "Object C"
        };
    }

    private static String[] getReferenceArrayFields() {
        return new String[] {
            "NAME",
            "Object[] objects"
        };
    }

    private static String[] getReferenceListFields() {
        return new String[] {
            "NAME",
            "List<Object> objects"
        };
    }

    private static void displayPrimitive(PrimitiveObject o, AsciiPanel terminal, int x, int y) {
        for (int i = 0; i < getFields(o).length; i++) {
            String value = "";
            switch(i) {
                case 0: value = o.name; break;
                case 1: value = Byte.toString(o.b); break;
                case 2: value = Character.toString(o.c); break;
                case 3: value = Double.toString(o.d); break;
                case 4: value = Float.toString(o.f); break;
                case 5: value = Integer.toString(o.i); break;
                case 6: value = Long.toString(o.j); break;
                case 7: value = Short.toString(o.s); break;
                case 8: value = Boolean.toString(o.z);
            }
            terminal.write(getFields(o)[i] + " = " + value, x, y);
            y++;
        }
    }

    private static void displayPrimitiveArray(PrimitiveArray o, AsciiPanel terminal, int x, int y) {
        terminal.write("NAME = " + o.name, x, y);
        y++;

        String[] lengths = new String[3];
        lengths[0] = (o.ints == null) ? "0" : Integer.toString(o.ints.length);
        lengths[1] = (o.doubles == null) ? "0" : Integer.toString(o.doubles.length);
        lengths[2] = (o.bools == null) ? "0" : Integer.toString(o.bools.length);
        for (int i = 1; i < getFields(o).length; i++) {
            terminal.write(getFields(o)[i] + " (len=" + lengths[i - 1] + ")", x, y);
            y++;
        }
    }

    private static void displayReference(ReferenceObject o, AsciiPanel terminal, int x, int y) {
        for (int i = 0; i < getFields(o).length; i++) {
            String value = "";
            switch(i) {
                case 0: value = o.name; break;
                case 1: 
                    if (o.A == null) { value = "<null>"; break; }
                    else { value = o.A.name + " (" + getTypeString(o.A) + ")"; break; }
                case 2: 
                    if (o.B == null) { value = "<null>"; break; }
                    else { value = o.B.name + " (" + getTypeString(o.B) + ")"; break; }
                case 3: 
                    if (o.C == null) { value = "<null>"; break; }
                    else { value = o.C.name + " (" + getTypeString(o.C) + ")"; break; }
            }

            terminal.write(getFields(o)[i] + " = " + value, x, y);
            y++;
        }
    }

    private static void displayReferenceArray(ReferenceArray o, AsciiPanel terminal, int x, int y) {
        terminal.write("NAME = " + o.name, x, y);
        y++;
        terminal.write("Object[] objects (len=" + (o.objects == null ? "0" : Integer.toString(o.objects.length)) + ")", x, y);
    }

    private static void displayReferenceList(ReferenceList o, AsciiPanel terminal, int x, int y) {
        terminal.write("NAME = " + o.name, x, y);
        y++;

        terminal.write("List<Object> objects (len=" + (o.objects == null ? "0" : Integer.toString(o.objects.size())) + ")", x, y);
    }
}
