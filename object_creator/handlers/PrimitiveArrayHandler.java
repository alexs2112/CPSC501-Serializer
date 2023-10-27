package object_creator.handlers;

import java.awt.Color;
import java.awt.event.KeyEvent;
import application.Screen;
import asciiPanel.AsciiPanel;
import object_creator.ObjectCreator;
import object_creator.classes.PrimitiveArray;
import object_creator.helpers.ModifyIntArray;
import object_creator.helpers.ModifyDoubleArray;
import object_creator.helpers.ModifyBoolArray;
import object_creator.helpers.ObjectHelper;

public class PrimitiveArrayHandler extends Screen {
    private ObjectCreator objectCreator;
    private PrimitiveArray obj;
    private int selection = 0;
    private boolean editMode; // If the user is editing the name or not
    private String editString = "";
    private int returnIndex = -1;

    public PrimitiveArrayHandler(ObjectCreator objectCreator) {
        this.objectCreator = objectCreator;
        obj = new PrimitiveArray();
    }

    public PrimitiveArrayHandler(ObjectCreator objectCreator, PrimitiveArray obj, int index) {
        this.objectCreator = objectCreator;
        this.obj = obj;
        this.returnIndex = index;
    }

    @Override
    public String title() { return "Creating Primitive Array"; }

    @Override
    public void print(AsciiPanel terminal) {
        drawBorder(terminal);

        int x = 4;
        int y = 3;
        
        /* Handle setting the name of the array object separately */
        Color c = (selection == 0) ? Color.GREEN : Color.WHITE;
        String s;
        if (editMode) {
            s = "NAME = " + editString;
            terminal.write(" ", x + s.length(), y, Color.BLACK, Color.LIGHT_GRAY);
        } else {
            s = "NAME = " + obj.name;
        }
        terminal.write(s, x, y, c);
        y++;

        int i;
        for (i = 1; i < ObjectHelper.getFields(obj).length; i++) {
            c = (i == selection) ? Color.GREEN : Color.WHITE;
            s = ObjectHelper.getFields(obj)[i];
            terminal.write(s, x, y, c);
            y++;
        }
        drawArrayValues(terminal, 40, 6);

        c = (selection == i) ? Color.GREEN : Color.WHITE;
        terminal.write("Save Object", x, ++y, c);
        i++;
        y++;
    }

    @Override
    public Screen input(KeyEvent key) {
        if (key.getKeyCode() == KeyEvent.VK_DOWN) {
            if (!editMode) {
                selection++;
                if (selection >= ObjectHelper.getFields(obj).length + 1) { selection = 0; }
            }
        } else if (key.getKeyCode() == KeyEvent.VK_UP) {
            if (!editMode) {
                selection--;
                if (selection < 0) { selection = ObjectHelper.getFields(obj).length - 1 + 1; }
            }
        } else if (key.getKeyCode() == KeyEvent.VK_ENTER) {
            if (selection == ObjectHelper.getFields(obj).length - 1 + 1) {
                if (returnIndex > -1) {
                    objectCreator.remObject(returnIndex);
                    objectCreator.addObject(returnIndex, obj);
                } else {
                    objectCreator.addObject(obj);
                }
                return objectCreator;
            } else if (selection == 0) {
                if (!editMode)
                    editMode = true;
                else {
                    obj.name = editString;
                    editString = "";
                    editMode = false;
                }
            } else if (selection == 1) {
                if (obj.ints == null) { obj.ints = new int[0]; }
                return new ModifyIntArray(this, obj.ints);
            } else if (selection == 2) {
                if (obj.doubles == null) { obj.doubles = new double[0]; }
                return new ModifyDoubleArray(this, obj.doubles);
            } else if (selection == 3) {
                if (obj.bools == null) { obj.bools = new boolean[0]; }
                return new ModifyBoolArray(this, obj.bools);
            }
        } else if (key.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (editMode) {
                editMode = false;
                editString = "";
            } else {
                return objectCreator;
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

    private void drawArrayValues(AsciiPanel terminal, int x, int y) {
        if (selection == 0 || selection == ObjectHelper.getFields(obj).length) { return; }
        terminal.write(ObjectHelper.getFields(obj)[selection], x, y, Color.WHITE);
        y += 2;
        switch(selection) {
            case 1:
                if (obj.ints == null) {
                    terminal.write("Empty or Null Array", x, y);
                } else {
                    for (int i : obj.ints) {
                        terminal.write(Integer.toString(i), x + 4, y);
                        y++;
                    }
                }
                break;
            case 2:
                if (obj.doubles == null) {
                    terminal.write("Empty or Null Array", x, y);
                } else {
                    for (double i : obj.doubles) {
                        terminal.write(Double.toString(i), x + 4, y);
                        y++;
                    }
                }
                break;
            case 3:
                if (obj.bools == null) {
                    terminal.write("Empty or Null Array", x, y);
                } else {
                    for (boolean i : obj.bools) {
                        terminal.write(Boolean.toString(i), x + 4, y);
                        y++;
                    }
                }
                break;
        }
    }
    
    public void saveNewArray(int[] newInts) { obj.ints = newInts; }
    public void saveNewArray(double[] newDoubles) { obj.doubles = newDoubles; }
    public void saveNewArray(boolean[] newBools) { obj.bools = newBools; }
}
