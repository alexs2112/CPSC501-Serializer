package object_creator.handlers;

import application.Screen;
import asciiPanel.AsciiPanel;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.awt.Color;
import object_creator.ObjectCreator;
import object_creator.classes.ObjectType;
import object_creator.classes.ReferenceArray;
import object_creator.helpers.ModifyArray;

public class ReferenceArrayHandler extends Screen {
    private ReferenceArray objs;
    private ObjectCreator objectCreator;
    private int selection;
    private boolean editMode;
    private String editString = "";

    public ReferenceArrayHandler(ObjectCreator objectCreator) {
        this.objectCreator = objectCreator;
        objs = new ReferenceArray();
    }

    @Override
    public String title() { return "Creating Reference Array"; }
    
    @Override
    public void print(AsciiPanel terminal) {
        drawBorder(terminal);

        int x = 4;
        int y = 3;

        Color c = (selection == 0) ? Color.GREEN : Color.WHITE;
        String s;
        if (editMode) {
            s = "NAME = " + editString;
            terminal.write(" ", x + s.length(), y, Color.BLACK, Color.LIGHT_GRAY);
        } else {
            s = "NAME = " + objs.name;
        }
        terminal.write(s, x, y, c);
        y++;

        c = (selection == 1) ? Color.GREEN : Color.WHITE;
        s = objs.getFields()[1];
        terminal.write(s, x, y, c);
        drawArrayValues(terminal, 40, 6);

        c = (2 == selection) ? Color.GREEN : Color.WHITE;
        terminal.write("Save Object", x, ++y, c);
        y++;
    }

    @Override
    public Screen input(KeyEvent key) {
        if (key.getKeyCode() == KeyEvent.VK_DOWN) {
            selection++;
            if (selection >= objs.getFields().length + 1) { selection = 0; }
        } else if (key.getKeyCode() == KeyEvent.VK_UP) {   
            selection--;
            if (selection < 0) { selection = objs.getFields().length - 1 + 1; }
        } else if (key.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (editMode) { editMode = false; }
            else { return objectCreator; }
        } else if (key.getKeyCode() == KeyEvent.VK_ENTER) {
            if (selection == 0) {
                if (editMode) {
                    editMode = false;
                    objs.name = editString;
                } else {
                    editMode = true;
                }
            } else if (selection == 2) {
                /* Save the object */
                objectCreator.addObject(objs);
                return objectCreator;
            } else {
                /* Modify the object array */
                ObjectType[] arr;
                if (objs.objects == null) { arr = new ObjectType[0]; }
                else { arr = objs.objects; }
                return new ModifyArray(this, arr);
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

    public void saveNewArray(ObjectType[] newObjects) {
        objs.objects = newObjects;
    }

    private void drawArrayValues(AsciiPanel terminal, int x, int y) {
        if (selection == 0 || selection == objs.getFields().length) { return; }
        terminal.write(objs.getFields()[selection], x, y, Color.WHITE);
        y += 2;
        if (objs.objects == null || objs.objects.length == 0) {
            terminal.write("Empty or Null Array", x, y);
        } else {
            for (ObjectType o : objs.objects) {
                String s;
                if (o == null) { s = "null"; }
                else { s = o.name + "  (" + o.getTypeString() + ")"; }
                terminal.write(s, x, y);
                y++;
            }
        }
    }

    public ArrayList<ObjectType> getObjects() {
        return objectCreator.getObjects();
    }
}
