package object_creator.helpers;

import java.awt.Color;
import java.awt.event.KeyEvent;
import application.Screen;
import asciiPanel.AsciiPanel;
import object_creator.classes.ObjectType;
import object_creator.handlers.PrimitiveArrayHandler;
import object_creator.handlers.ReferenceArrayHandler;

public class ModifyArray extends Screen {
    private PrimitiveArrayHandler handler;
    private ReferenceArrayHandler refHandler;
    private int[] ints;
    private double[] doubles;
    private boolean[] bools;
    private ObjectType[] objects;
    private int selection;
    private boolean editMode;
    private String editString = "";
    private String errorString = "";

    public ModifyArray(PrimitiveArrayHandler handler, int[] ints) { this.handler = handler; this.ints = ints; }
    public ModifyArray(PrimitiveArrayHandler handler, double[] doubles) { this.handler = handler; this.doubles = doubles; }
    public ModifyArray(PrimitiveArrayHandler handler, boolean[] bools) { this.handler = handler; this.bools = bools; }
    public ModifyArray(ReferenceArrayHandler refHandler, ObjectType[] objects) { this.refHandler = refHandler; this.objects = objects; }

    @Override
    public String title() { return "Edit Array"; }

    @Override
    public void print(AsciiPanel terminal) {
        drawBorder(terminal);

        if (ints != null) { printLength(terminal, ints.length); printInts(terminal); }
        else if (doubles != null) { printLength(terminal, doubles.length); printDoubles(terminal); }
        else if (bools != null) { printLength(terminal, bools.length); printBools(terminal); }
        else if (objects != null) { printLength(terminal, objects.length); printObjects(terminal); }

        if (errorString.length() > 0)
            terminal.write("Error: " + errorString, 8, terminal.getHeightInCharacters() - 3, Color.RED);
    }

    @Override
    public Screen input(KeyEvent key) {
        int length;
        if (ints != null) { length = ints.length; }
        else if (doubles != null) { length = doubles.length; }
        else if (bools != null) { length = bools.length; }
        else if (objects != null) { length = objects.length; }
        else { return handler; }

        if (key.getKeyCode() == KeyEvent.VK_DOWN) {
            if (!editMode) {
                selection++;
                if (selection >= length + 2) { selection = 0; }
            }
        } else if (key.getKeyCode() == KeyEvent.VK_UP) {
            if (!editMode) {
                selection--;
                if (selection < 0) { selection = length + 1; }
            }
        } else if (key.getKeyCode() == KeyEvent.VK_ENTER) {
            if (selection == length + 1) {
                if (ints != null) { handler.saveNewArray(ints); }
                else if (doubles != null) { handler.saveNewArray(doubles); }
                else if (bools != null) { handler.saveNewArray(bools); }
                else if (objects != null) { refHandler.saveNewArray(objects); }
                return getReturnScreen();
            } else if (!editMode) {
                if (objects != null && selection > 0) {
                    return new ObjectSelectorArray(this, objects, refHandler.getObjects(), selection - 1);
                }
                editMode = true;
            } else {
                boolean pass = saveEdit();
                if (pass) {
                    editMode = false;
                    editString = "";
                    errorString = "";
                }
            }
        } else if (key.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (editMode) {
                editMode = false;
                editString = "";
                errorString = "";
            } else {
                return getReturnScreen();
            }
        } else if (editMode) {
            try {
                if (key.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    if (editString.length() > 0)
                        editString = editString.substring(0, editString.length() - 1);
                } else {
                    char c = key.getKeyChar();
                    if (c >= 0 && c < 256)
                        editString += c;
                }
            } catch (java.lang.IllegalArgumentException e) { /* Ignore invalid characters */ }
        }
        return this;
    }

    private Screen getReturnScreen() {
        if (handler != null) { return handler; }
        else { return refHandler; }
    }

    private void printLength(AsciiPanel terminal, int len) {
        Color c = (selection == 0) ? Color.GREEN : Color.WHITE;
        String s;
        if (selection == 0 && editMode) {
            s = "Length = " + editString;
            terminal.write(" ", 4 + s.length(), 5, Color.BLACK, Color.LIGHT_GRAY);
        } else {
            s = "Length = " + Integer.toString(len);
        }
        terminal.write(s, 4, 5, c);
    }

    private void printInts(AsciiPanel terminal) {
        int x = 4;
        int y = 3;
        terminal.write("int[] ints", x, y);
        y += 3;
        x += 4;

        Color c;
        String s;
        for (int i = 0; i < ints.length; i++) {
            c = (selection == i + 1) ? Color.GREEN : Color.WHITE;

            if (selection == i + 1 && editMode) {
                s = "[" + Integer.toString(i) + "] " + editString;
                terminal.write(" ", x + s.length(), y, Color.BLACK, Color.LIGHT_GRAY);
            } else {
                s = "[" + Integer.toString(i) + "] " + ints[i];
            }
            terminal.write(s, x, y++, c);
        }

        c = (selection == ints.length + 1) ? Color.GREEN : Color.WHITE;
        terminal.write("Save Array", x - 4, ++y, c);
    }

    private void printDoubles(AsciiPanel terminal) {
        int x = 4;
        int y = 3;
        terminal.write("double[] doubles", x, y);
        y += 3;
        x += 4;

        Color c;
        String s;
        for (int i = 0; i < doubles.length; i++) {
            c = (selection == i + 1) ? Color.GREEN : Color.WHITE;

            if (selection == i + 1 && editMode) {
                s = "[" + Integer.toString(i) + "] " + editString;
                terminal.write(" ", x + s.length(), y, Color.BLACK, Color.LIGHT_GRAY);
            } else {
                s = "[" + Integer.toString(i) + "] " + doubles[i];
            }
            terminal.write(s, x, y++, c);
        }

        c = (selection == doubles.length + 1) ? Color.GREEN : Color.WHITE;
        terminal.write("Save Array", x - 4, ++y, c);
    }

    private void printBools(AsciiPanel terminal) {
        int x = 4;
        int y = 3;
        terminal.write("boolean[] bools", x, y);
        y += 3;
        x += 4;

        Color c;
        String s;
        for (int i = 0; i < bools.length; i++) {
            c = (selection == i + 1) ? Color.GREEN : Color.WHITE;

            if (selection == i + 1 && editMode) {
                s = "[" + Integer.toString(i) + "] " + editString;
                terminal.write(" ", x + s.length(), y, Color.BLACK, Color.LIGHT_GRAY);
            } else {
                s = "[" + Integer.toString(i) + "] " + bools[i];
            }
            terminal.write(s, x, y++, c);
        }

        c = (selection == bools.length + 1) ? Color.GREEN : Color.WHITE;
        terminal.write("Save Array", x - 4, ++y, c);
    }

    private void printObjects(AsciiPanel terminal) {
        int x = 4;
        int y = 3;
        terminal.write("Object[] objects", x, y);
        y += 3;
        x += 4;

        Color c;
        String s;
        for (int i = 0; i < objects.length; i++) {
            c = (selection == i + 1) ? Color.GREEN : Color.WHITE;
            s = "[" + Integer.toString(i) + "] ";
            if (objects[i] != null) {
                s += objects[i].name + "  (" + objects[i].getTypeString() + ")";
            } else {
                s += "null";
            }
            terminal.write(s, x, y++, c);
        }

        c = (selection == objects.length + 1) ? Color.GREEN : Color.WHITE;
        terminal.write("Save Array", x - 4, ++y, c);
    }

    private boolean saveEdit() {
        if (selection == 0) {
            try {
                int newLen = Integer.valueOf(editString);
                resizeArray(newLen);
                return true;
            } catch (Exception e) {
                errorString = "Length must be of type Integer";
                return false;
            }
        }

        try {
            if (ints != null) {
                ints[selection - 1] = Integer.valueOf(editString);
                return true;
            } else if (doubles != null) {
                doubles[selection - 1] = Double.valueOf(editString);
                return true;
            } else if (bools != null) {
                bools[selection - 1] = Boolean.valueOf(editString);
                return true;
            }
        } catch(Exception e) {
            errorString = "Invalid type";
        }
        return false;
    }

    private void resizeArray(int newLen) {
        if (ints != null) {
            int[] newInts = new int[newLen];
            for (int i = 0; i < ints.length; i++) {
                if (i >= newLen) { break; }
                newInts[i] = ints[i];
            }
            ints = newInts;
        } else if (doubles != null) { 
            double[] newDoubles = new double[newLen];
            for (int i = 0; i < doubles.length; i++) {
                if (i >= newLen) { break; }
                newDoubles[i] = doubles[i];
            }
            doubles = newDoubles;
        } else if (bools != null) { 
            boolean[] newBools = new boolean[newLen];
            for (int i = 0; i < bools.length; i++) {
                if (i >= newLen) { break; }
                newBools[i] = bools[i];
            }
            bools = newBools;
        } else if (objects != null) { 
            ObjectType[] newObjects = new ObjectType[newLen];
            for (int i = 0; i < objects.length; i++) {
                if (i >= newLen) { break; }
                newObjects[i] = objects[i];
            }
            objects = newObjects;
        }

    }
}
