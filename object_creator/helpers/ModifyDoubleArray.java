package object_creator.helpers;

import asciiPanel.AsciiPanel;
import java.awt.Color;
import application.Screen;
import object_creator.handlers.PrimitiveArrayHandler;

public class ModifyDoubleArray extends ModifyArray {
    private PrimitiveArrayHandler handler;
    private double[] doubles;

    public ModifyDoubleArray(PrimitiveArrayHandler handler, double[] doubles) {
        this.handler = handler;
        this.doubles = doubles;
    }

    @Override
    protected int getLength() { return this.doubles.length; }
    
    @Override
    protected void printArray(AsciiPanel terminal) {
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

    @Override
    protected void saveNewArray() {
        handler.saveNewArray(doubles);
    }

    @Override
    protected Screen getReturnScreen() {
        return handler;
    }

    @Override
    protected boolean saveSelection(int index) {
        try {
            if (doubles != null) {
                doubles[index] = Double.valueOf(editString);
                return true;
            }
        } catch(Exception e) {
            errorString = "Invalid type, must be Double";
        }
        return false;
    }

    @Override
    protected void resizeArray(int newLen) {
        double[] newDoubles = new double[newLen];
        for (int i = 0; i < doubles.length; i++) {
            if (i >= newLen) { break; }
            newDoubles[i] = doubles[i];
        }
        doubles = newDoubles;
    }
}
