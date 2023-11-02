package visualizer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.awt.event.KeyEvent;
import java.awt.Color;
import asciiPanel.AsciiPanel;
import helpers.FieldHelper;
import application.Screen;

public class VisualizerNormal extends Visualizer {
    private ArrayList<Field> fields;
    private ArrayList<Object> values;

    public VisualizerNormal(Object obj, Screen lastScreen, Screen homeScreen) {
        super(obj, lastScreen, homeScreen);
    }
    public VisualizerNormal(Object obj, Screen lastScreen, Screen homeScreen, ArrayList<Object> breadcrumb) {
        super(obj, lastScreen, homeScreen, breadcrumb);
    }

    public int optionNum() { return fields.size(); }

    public void setupObject(Object obj) {
        this.obj = obj;
        fields = new ArrayList<Field>();
        values = new ArrayList<Object>();
        for (Field f : FieldHelper.findFields(obj.getClass())) {
            try {
                fields.add(f);
                values.add(f.get(obj));
            } catch(IllegalAccessException e) { continue; }
        }
    }

    @Override
    public void print(AsciiPanel terminal) {
        super.print(terminal);
        int x = 4;
        int y = 3;
        terminal.write(objectName(obj), x, y++, Color.WHITE);

        for (int i = 0; i < fields.size(); i++) {
            String name = fields.get(i).getName();
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

            String s = name + " = " + valueText;
            if (s.length() > terminal.getWidthInCharacters() - 16) {
                s = s.substring(0, terminal.getWidthInCharacters() - 16);
            }
            terminal.write(s, x, y++, c);
        }
    }

    @Override
    public Screen input(KeyEvent key) {
        if (key.getKeyCode() == KeyEvent.VK_ENTER) {
            if (selection < fields.size()) {
                Object v = values.get(selection);
                return getNextScreen(v);
            }
        }
        return super.input(key);
    }
}
