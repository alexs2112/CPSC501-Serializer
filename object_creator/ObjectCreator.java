package object_creator;

import application.Screen;
import asciiPanel.AsciiPanel;
import object_creator.classes.ObjectType;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class ObjectCreator extends Screen {
    private int selection;
    private ArrayList<ObjectType> objects;
    public ArrayList<ObjectType> getObjects() { return objects; }
    public void addObject(ObjectType newObject) { objects.add(newObject); }

    public ObjectCreator() {
        objects = new ArrayList<ObjectType>();
    }

    @Override
    public String title() { return "Object Creator"; }

    @Override
    public void print(AsciiPanel terminal) {
        drawBorder(terminal);

        terminal.write("Objects:", 4, 3);
        int x = 8;
        int y = 4;
        int len = objects.size();
        if (len == 0) {
            terminal.write("[No objects]", x, y++, Color.GRAY);
        } else {
            for (int i = 0; i < len; i++) {
                Color c = (i == selection) ? Color.GREEN : Color.WHITE;
                ObjectType o = objects.get(i);
                terminal.write(o.name, x, y, c);
                terminal.write(o.getTypeString(), x+20, y, c);
                y++;

                if (i == selection) {
                    o.displayObject(terminal, 40, 8);
                }
            }
        }

        // After the above for loop, i = len
        int i = len;
        y++;
        x -= 4;
        Color c = (i == selection) ? Color.GREEN : Color.WHITE;
        terminal.write("Create New Object", x, y++, c);
    }

    @Override
    public Screen input(KeyEvent key) {
        if (key.getKeyCode() == KeyEvent.VK_DOWN) {
            selection++;
            if (selection >= objects.size() + 1) { selection = 0; }
        } else if (key.getKeyCode() == KeyEvent.VK_UP) {
            selection--;
            if (selection < 0) { selection = objects.size() - 1 + 1; }
        } else if (key.getKeyCode() == KeyEvent.VK_ENTER) {
            int i;
            int len = objects.size();
            for (i = 0; i < len; i++) {
                if (selection == i) {
                    // RETURN NEW EDIT SCREEN FOR objects.get(i);
                    return this;
                }
            }
            if (selection == len) {
                return new CreateObject(this);
            }
        }
        return this;
    }
}
