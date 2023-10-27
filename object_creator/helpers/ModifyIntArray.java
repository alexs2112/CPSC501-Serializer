package object_creator.helpers;

import asciiPanel.AsciiPanel;
import java.awt.Color;
import application.Screen;
import object_creator.handlers.PrimitiveArrayHandler;

public class ModifyIntArray extends ModifyArray {
    private PrimitiveArrayHandler handler;
    private int[] ints;

    public ModifyIntArray(PrimitiveArrayHandler handler, int[] ints) {
        this.handler = handler;
        this.ints = ints;
    }

    @Override
    protected int getLength() { return this.ints.length; }
    
    @Override
    protected void printArray(AsciiPanel terminal) {
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

    @Override
    protected void saveNewArray() {
        handler.saveNewArray(ints);
    }

    @Override
    protected Screen getReturnScreen() {
        return handler;
    }

    @Override
    protected boolean saveSelection(int index) {
        try {
            if (ints != null) {
                ints[index] = Integer.valueOf(editString);
                return true;
            }
        } catch(Exception e) {
            errorString = "Invalid type, must be Integer";
        }
        return false;
    }

    @Override
    protected void resizeArray(int newLen) {
        int[] newInts = new int[newLen];
        for (int i = 0; i < ints.length; i++) {
            if (i >= newLen) { break; }
            newInts[i] = ints[i];
        }
        ints = newInts;
    }
}
