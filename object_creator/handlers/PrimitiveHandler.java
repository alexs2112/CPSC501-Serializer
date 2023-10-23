package object_creator.handlers;

import application.Screen;
import java.awt.Color;
import java.awt.event.KeyEvent;
import asciiPanel.AsciiPanel;
import object_creator.ObjectCreator;
import object_creator.classes.PrimitiveObject;

public class PrimitiveHandler extends Screen {
    private ObjectCreator objectCreator;
    private PrimitiveObject obj;
    private int selection;
    private boolean editMode; // If the user is editing a field or not
    private String editString = "";
    private String errorString = "";

    public PrimitiveHandler(ObjectCreator objectCreator) {
        this.objectCreator = objectCreator;
        obj = new PrimitiveObject();
    }

    public PrimitiveHandler(ObjectCreator objectCreator, PrimitiveObject obj) {
        this.objectCreator = objectCreator;
        this.obj = obj;
    }

    @Override
    public String title() { return "Creating Primitive Object"; }

    @Override
    public void print(AsciiPanel terminal) {
        drawBorder(terminal);

        int i;
        int x = 4;
        int y = 3;
        for (i = 0; i < obj.getFields().length; i++) {
            Color c = (i == selection) ? Color.GREEN : Color.WHITE;
            String s;
            if (i == selection && editMode) {
                s = obj.getFields()[i] + " = " + editString;
                terminal.write(" ", x + s.length(), y, Color.BLACK, Color.LIGHT_GRAY);
            } else {
                s = obj.getFields()[i] + " = " + obj.getStringValue(i);
            }
            terminal.write(s, x, y, c);
            y++;
        }

        Color c = (i == selection) ? Color.GREEN : Color.WHITE;
        terminal.write("Save Object", x, ++y, c);
        i++;
        y++;

        if (!errorString.isEmpty()) {
            terminal.write("Error: " + errorString, 8, terminal.getHeightInCharacters() - 3, Color.RED);
        }
    }

    @Override
    public Screen input(KeyEvent key) {
        if (key.getKeyCode() == KeyEvent.VK_DOWN) {
            if (!editMode) {
                selection++;
                if (selection >= obj.getFields().length + 1) { selection = 0; }
            }
        } else if (key.getKeyCode() == KeyEvent.VK_UP) {
            if (!editMode) {
                selection--;
                if (selection < 0) { selection = obj.getFields().length - 1 + 1; }
            }
        } else if (key.getKeyCode() == KeyEvent.VK_ENTER) {
            if (selection == obj.getFields().length - 1 + 1) {
                objectCreator.addObject(obj);
                return objectCreator;
            } else if (!editMode) {
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

    /* Ensure the string entered is valid before saving it */
    private boolean saveEdit() {
        switch(selection) {
            case 0: obj.name = editString; return true;
            case 1: 
                try { obj.b = editString.getBytes()[0]; return true; }
                catch (Exception e) { typeError("Byte"); return false; }
            case 2: 
                try { obj.c = editString.toCharArray()[0]; return true; }
                catch (Exception e) { typeError("Character"); return false; }
            case 3: 
                try { obj.d = Double.valueOf(editString); return true; }
                catch (Exception e) { typeError("Double"); return false; }
            case 4: 
                try { obj.f = Float.valueOf(editString); return true; }
                catch (Exception e) { typeError("Float"); return false; }
            case 5: 
                try { obj.i = Integer.valueOf(editString); return true; }
                catch (Exception e) { typeError("Integer"); return false; }
            case 6: 
                try { obj.j = Long.valueOf(editString); return true; }
                catch (Exception e) { typeError("Long"); return false; }
            case 7: 
                try { obj.s = Short.valueOf(editString); return true; }
                catch (Exception e) { typeError("Short"); return false; }
            case 8: 
                try { obj.z = Boolean.valueOf(editString); return true; }
                catch (Exception e) { typeError("Boolean"); return false; }
            default:
                errorString = "Unknown error";
                return false;
        }
    }

    private void typeError(String type) {
        errorString = "Must be of type " + type;
    }
}
