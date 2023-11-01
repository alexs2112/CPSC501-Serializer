package visualizer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import application.Screen;
import java.awt.Color;
import java.awt.event.KeyEvent;
import serializer.helpers.FieldHelper;
import asciiPanel.AsciiPanel;

public class VisualizerArray extends Visualizer {
    private ArrayList<Object> values;

    public VisualizerArray(Object obj, Screen lastScreen, Screen homeScreen) {
        super(obj, lastScreen, homeScreen);
    }
    public VisualizerArray(Object obj, Screen lastScreen, Screen homeScreen, ArrayList<Object> breadcrumb) {
        super(obj, lastScreen, homeScreen, breadcrumb);
    }

    @Override @SuppressWarnings("unchecked")
    protected void setupObject(Object obj) {
        this.obj = obj;
        values = new ArrayList<Object>();
        if (obj.getClass().isArray()) {
            for (int i = 0; i < Array.getLength(obj); i++) {
                Object o = Array.get(obj, i);
                values.add(o);
            }
        } else {
            // Assume it is an arraylist
            ArrayList<Object> arr = (ArrayList<Object>)obj;
            for (int i = 0; i < arr.size(); i++) {
                Object o = arr.get(i);
                values.add(o);
            }
        }
    }

    @Override
    protected int optionNum() {
        return values.size();
    }

    @Override
    public void print(AsciiPanel terminal) {
        super.print(terminal);
        int x = 4;
        int y = 3;
        terminal.write(objectName(obj), x, y++, Color.WHITE);

        for (int i = 0; i < values.size(); i++) {
            Object value = values.get(i);
            String valueText;
            if (value == null) {
                valueText = "null";
            } else if (FieldHelper.isPrimitive(value)) {
                valueText = value.toString();
            } else {
                valueText = objectName(value);
            }

            Color c = (i == selection) ? Color.GREEN : Color.WHITE;

            String s = "[" + Integer.toString(i) + "] = " + valueText;
            if (s.length() > terminal.getWidthInCharacters() - 16) {
                s = s.substring(0, terminal.getWidthInCharacters() - 16);
            }
            terminal.write(s, x, y++, c);
        }
    }

    @Override
    public Screen input(KeyEvent key) {
        if (key.getKeyCode() == KeyEvent.VK_ENTER) {
            if (selection < values.size()) {
                Object v = values.get(selection);
                return getNextScreen(v);
            }
        }
        return super.input(key);
    }
}
