package object_creator.helpers;

import asciiPanel.AsciiPanel;
import java.awt.Color;
import application.Screen;
import object_creator.handlers.PrimitiveArrayHandler;

public class ModifyBoolArray extends ModifyArray {
    private PrimitiveArrayHandler handler;
    private boolean[] bools;

    public ModifyBoolArray(PrimitiveArrayHandler handler, boolean[] bools) {
        this.handler = handler;
        this.bools = bools;
    }

    @Override
    protected int getLength() { return this.bools.length; }
    
    @Override
    protected void printArray(AsciiPanel terminal) {
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

    @Override
    protected void saveNewArray() {
        handler.saveNewArray(bools);
    }

    @Override
    protected Screen getReturnScreen() {
        return handler;
    }

    @Override
    protected boolean saveSelection(int index) {
        try {
            if (bools != null) {
                bools[index] = Boolean.valueOf(editString);
                return true;
            }
        } catch(Exception e) {
            errorString = "Invalid type, must be Boolean";
        }
        return false;
    }

    @Override
    protected void resizeArray(int newLen) {
        boolean[] newBools = new boolean[newLen];
        for (int i = 0; i < bools.length; i++) {
            if (i >= newLen) { break; }
            newBools[i] = bools[i];
        }
        bools = newBools;
    }
}
