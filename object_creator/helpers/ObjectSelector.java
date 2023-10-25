package object_creator.helpers;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import application.Screen;
import asciiPanel.AsciiPanel;
import object_creator.classes.ObjectType;
import object_creator.handlers.ReferenceHandler;

public class ObjectSelector extends Screen {
    protected int selection;
    protected ReferenceHandler referenceHandler;
    protected Screen prevScreen;
    protected ArrayList<ObjectType> objects;

    public ObjectSelector(ReferenceHandler referenceHandler, ArrayList<ObjectType> objects) {
        this.referenceHandler = referenceHandler;
        this.prevScreen = referenceHandler;
        this.objects = objects;
    }
    public ObjectSelector(ReferenceHandler referenceHandler, Screen screen, ArrayList<ObjectType> objects) {
        this.referenceHandler = referenceHandler;
        this.prevScreen = screen;
        this.objects = objects;
    }

    @Override
    public String title() { return "Selecting Object"; }

    @Override
    public void print(AsciiPanel terminal) {
        drawBorder(terminal);

        terminal.write("Select Object to Link", 4, 3);
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
    }

    @Override
    public Screen input(KeyEvent key) {
        int len = objects.size();
        if (key.getKeyCode() == KeyEvent.VK_DOWN) {
            selection++;
            if (selection >= len) { selection = 0; }
        } else if (key.getKeyCode() == KeyEvent.VK_UP) {   
            selection--;
            if (selection < 0) { selection = len - 1; }
        } else if (key.getKeyCode() == KeyEvent.VK_ENTER) {
            if (referenceHandler != null) {
                if (objects != null && objects.size() > 0)
                    referenceHandler.addReference(objects.get(selection));
                return prevScreen;
            }
        } else if (key.getKeyCode() == KeyEvent.VK_ESCAPE) {
            return prevScreen;
        }
        return this;
    }
}
