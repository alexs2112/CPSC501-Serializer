package object_creator.handlers;

import application.Screen;
import java.awt.Color;
import java.awt.event.KeyEvent;
import asciiPanel.AsciiPanel;
import object_creator.ObjectCreator;
import object_creator.classes.PrimitiveObject;
import object_creator.helpers.ObjectHelper;

public class PrimitiveHandler extends Screen {
    private ObjectCreator objectCreator;
    private PrimitiveObject obj;
    private int selection;
    private boolean editMode; // If the user is editing a field or not
    private String editString = "";
    private String errorString = "";
    private int returnIndex = -1;

    public PrimitiveHandler(ObjectCreator objectCreator) {
        this.objectCreator = objectCreator;
        obj = new PrimitiveObject();
    }

    public PrimitiveHandler(ObjectCreator objectCreator, PrimitiveObject obj, int index) {
        this.objectCreator = objectCreator;
        this.obj = obj;
        this.returnIndex = index;
    }

    @Override
    public String title() { return "Creating Primitive Object"; }

    @Override
    public void print(AsciiPanel terminal) {
        drawBorder(terminal);

        int i;
        int x = 4;
        int y = 3;
        for (i = 0; i < ObjectHelper.getFields(obj).length; i++) {
            Color c = (i == selection) ? Color.GREEN : Color.WHITE;
            String s;
            if (i == selection && editMode) {
                s = ObjectHelper.getFields(obj)[i] + " = " + editString;
                terminal.write(" ", x + s.length(), y, Color.BLACK, Color.LIGHT_GRAY);
            } else {
                s = ObjectHelper.getFields(obj)[i] + " = " + getValue(i);
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

    private String getValue(int index) {
        String value = "";
        switch(index) {
            case 0: value = obj.name; break;
            case 1: value = Byte.toString(obj.b); break;
            case 2: value = Character.toString(obj.c); break;
            case 3: value = Double.toString(obj.d); break;
            case 4: value = Float.toString(obj.f); break;
            case 5: value = Integer.toString(obj.i); break;
            case 6: value = Long.toString(obj.j); break;
            case 7: value = Short.toString(obj.s); break;
            case 8: value = Boolean.toString(obj.z);
        }
        return value;
    }
}
