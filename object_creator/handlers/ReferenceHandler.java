package object_creator.handlers;

import java.awt.event.KeyEvent;
import application.Screen;
import asciiPanel.AsciiPanel;
import java.awt.Color;
import object_creator.ObjectCreator;
import object_creator.classes.ReferenceObject;
import object_creator.helpers.ObjectSelector;
import object_creator.classes.ObjectType;

public class ReferenceHandler extends Screen {
    private ObjectCreator objectCreator;
    private ReferenceObject obj;
    private int selection;
    private boolean editMode; // Editing the name field of the reference object
    private String editString = "";

    @Override
    public String title() { return "Creating Reference Object"; }

    public ReferenceHandler(ObjectCreator objectCreator) {
        this.objectCreator = objectCreator;
        obj = new ReferenceObject();
    }

    @Override
    public void print(AsciiPanel terminal) {
        drawBorder(terminal);

        int i;
        int x = 4;
        int y = 3;
        for (i = 0; i < obj.getFields().length; i++) {
            Color c = (i == selection) ? Color.GREEN : Color.WHITE;
            String s;
            if (i == 0 && editMode) {
                s = obj.getFields()[i] + " = " + editString;
                terminal.write(" ", x + s.length(), y, Color.BLACK, Color.LIGHT_GRAY);
            } else {
                s = obj.getFields()[i] + " = " + obj.getStringValue(i);
            }

            terminal.write(s, x, y, c);
            y++;

            if (i == selection) {
                /* Man this is ugly */
                if (i == 1 && obj.A != null) obj.A.displayObject(terminal, 40, 8);
                else if (i == 2 && obj.B != null) obj.B.displayObject(terminal, 40, 8);
                else if (i == 3 && obj.C != null) obj.C.displayObject(terminal, 40, 8);
            }
        }

        Color c = (i == selection) ? Color.GREEN : Color.WHITE;
        terminal.write("Save Object", x, ++y, c);
        i++;
        y++;
    }

    @Override
    public Screen input(KeyEvent key) {
        if (key.getKeyCode() == KeyEvent.VK_DOWN) {
            selection++;
            if (selection >= obj.getFields().length + 1) { selection = 0; }
        } else if (key.getKeyCode() == KeyEvent.VK_UP) {   
            selection--;
            if (selection < 0) { selection = obj.getFields().length - 1 + 1; }
        } else if (key.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (editMode) { editMode = false; }
            else { return objectCreator; }
        } else if (key.getKeyCode() == KeyEvent.VK_ENTER) {
            if (selection == 0) {
                if (editMode) {
                    editMode = false;
                    obj.name = editString;
                } else {
                    editMode = true;
                }
            } else if (selection == 4) {
                /* Save the object */
                objectCreator.addObject(obj);
                return objectCreator;
            } else {
                /* Handle object selection */
                return new ObjectSelector(this, objectCreator.getObjects());
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

    public void addReference(ObjectType ref) {
        if (selection == 1) { obj.A = ref; }
        if (selection == 2) { obj.B = ref; }
        if (selection == 3) { obj.C = ref; }
    }
}
