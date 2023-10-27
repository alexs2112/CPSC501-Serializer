package object_creator.helpers;

import asciiPanel.AsciiPanel;
import java.awt.event.KeyEvent;
import java.awt.Color;
import application.Screen;
import object_creator.classes.ObjectType;
import object_creator.handlers.ReferenceArrayHandler;

public class ModifyObjectArray extends ModifyArray {
    private ReferenceArrayHandler handler;
    private ObjectType[] objs;

    public ModifyObjectArray(ReferenceArrayHandler handler, ObjectType[] objs) {
        this.handler = handler;
        this.objs = objs;
    }

    @Override
    protected int getLength() { return this.objs.length; }
    
    @Override
    protected void printArray(AsciiPanel terminal) {
        int x = 4;
        int y = 3;
        terminal.write("Object[] objects", x, y);
        y += 3;
        x += 4;

        Color c;
        String s;
        for (int i = 0; i < objs.length; i++) {
            c = (selection == i + 1) ? Color.GREEN : Color.WHITE;
            s = "[" + Integer.toString(i) + "] ";
            if (objs[i] != null) {
                s += objs[i].name + "  (" + ObjectHelper.getTypeString(objs[i]) + ")";
            } else {
                s += "null";
            }
            terminal.write(s, x, y++, c);
        }

        c = (selection == objs.length + 1) ? Color.GREEN : Color.WHITE;
        terminal.write("Save Array", x - 4, ++y, c);
    }

    @Override
    protected void saveNewArray() {
        handler.saveNewArray(objs);
    }

    @Override
    protected Screen getReturnScreen() {
        return handler;
    }

    @Override
    protected boolean saveSelection(int index) {
        /* This should never actually be called for an object array */
        return true;
    }

    @Override
    protected void resizeArray(int newLen) {
        ObjectType[] newObjects = new ObjectType[newLen];
        for (int i = 0; i < objs.length; i++) {
            if (i >= newLen) { break; }
            newObjects[i] = objs[i];
        }
        objs = newObjects;
    }

    @Override
    public Screen input(KeyEvent key) {
        /* Need to return an object selection screen in the object array */
        if (key.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!editMode && objs != null && selection > 0 && selection < getLength() + 1) {
                return new ObjectSelectorArray(this, objs, handler.getObjects(), selection - 1);
            }
        }
        return super.input(key);
    }
}
