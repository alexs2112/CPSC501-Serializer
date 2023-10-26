package object_creator.handlers;

import application.Screen;
import asciiPanel.AsciiPanel;
import java.awt.event.KeyEvent;
import java.awt.Color;
import object_creator.ObjectCreator;
import object_creator.classes.ObjectType;
import object_creator.classes.ReferenceList;
import object_creator.helpers.ObjectSelectorList;

public class ReferenceListHandler extends Screen {
    private ReferenceList objs;
    private ObjectCreator objectCreator;
    private int selection;
    private boolean editMode;
    private String editString = "";

    public ReferenceListHandler(ObjectCreator objectCreator) {
        this.objectCreator = objectCreator;
        objs = new ReferenceList();
    }

    @Override
    public String title() { return "Creating Reference List"; }
    
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
        y += 2;
        terminal.write("List Values", x, y);
        x += 2;

        int i = 0;
        if (objs.objects.size() == 0) {
            terminal.write("<Empty List>", x, y);
            x -= 2;
            y += 2;
        }
        else {
            for (i = 0; i < objs.objects.size(); i++) {
                ObjectType o = objs.objects.get(i);
                c = (selection == (i + 1)) ? Color.GREEN : Color.WHITE;
                s = o.name + "  (" + o.getTypeString() + ")";
                terminal.write(s, x, y, c);
                y++;
            }
            x -= 2;
            y++;
        }

        c = (selection == (i + 1)) ? Color.GREEN : Color.WHITE;
        terminal.write("Add Object", x, ++y, c);
        y++;
        i++;

        c = (selection == (i + 1)) ? Color.GREEN : Color.WHITE;
        terminal.write("Save Object", x, ++y, c);
    }

    @Override
    public Screen input(KeyEvent key) {
        if (key.getKeyCode() == KeyEvent.VK_DOWN) {
            if (!editMode) {
                selection++;
                if (selection >= objs.objects.size() + 3) { selection = 0; }
            }
        } else if (key.getKeyCode() == KeyEvent.VK_UP) {
            if (!editMode) {
                selection--;
                if (selection < 0) { selection = objs.objects.size() - 1 + 3; }
            }
        } else if (key.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (editMode) { editMode = false; }
            else { return objectCreator; }
        } else if (key.getKeyCode() == KeyEvent.VK_ENTER) {
            int len = objs.objects.size();
            int add_object = len + 1;
            int save_object = len + 2;
            if (selection == 0) {
                if (editMode) {
                    editMode = false;
                    objs.name = editString;
                } else {
                    editMode = true;
                }
            } else if (selection == add_object) {
                /* Add a new object */
                return new ObjectSelectorList(this, objs.objects, objectCreator.getObjects());
            } else if (selection == save_object) {
                /* Save the object */
                objectCreator.addObject(objs);
                return objectCreator;
            } else {
                /* Modify the object at a position */
                return new ObjectSelectorList(this, objs.objects, objectCreator.getObjects(), selection - 1);
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
}
