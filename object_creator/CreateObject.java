package object_creator;

import application.Screen;
import asciiPanel.AsciiPanel;
import object_creator.handlers.*;
import java.awt.Color;
import java.awt.event.KeyEvent;

public class CreateObject extends Screen {
    private ObjectCreator objectCreator;
    private int selection;

    public CreateObject(ObjectCreator objectCreator) {
        this.objectCreator = objectCreator;
    }

    private static String[] ObjectTypes = new String[] {
        "Primitive",
        "Reference",
        "Primitive Array",
        "Reference Array",
        "Reference List"
    };

    @Override
    public String title() { return "Create New Object"; }

    @Override
    public void print(AsciiPanel terminal) {
        drawBorder(terminal);

        terminal.write("Select Object to Create", 4, 3);
        int x = 8;
        int y = 4;
        for (int i = 0; i < ObjectTypes.length; i++) {
            String s = ObjectTypes[i];

            Color c = (i == selection) ? Color.GREEN : Color.WHITE;
            terminal.write(s, x, y++, c);
        }
    }

    @Override
    public Screen input(KeyEvent key) {
        if (key.getKeyCode() == KeyEvent.VK_DOWN) {
            selection++;
            if (selection >= ObjectTypes.length) { selection = 0; }
        } else if (key.getKeyCode() == KeyEvent.VK_UP) {
            selection--;
            if (selection < 0) { selection = ObjectTypes.length - 1; }
        } else if (key.getKeyCode() == KeyEvent.VK_ENTER) {
            switch(selection) {
                case 0: return new PrimitiveHandler(objectCreator);
                case 1: return new ReferenceHandler(objectCreator);
                case 2: return new PrimitiveArrayHandler(objectCreator);
                case 3: return new ReferenceArrayHandler(objectCreator);
                case 4: return new ReferenceListHandler(objectCreator);
            }
        } else if (key.getKeyCode() == KeyEvent.VK_ESCAPE) {
            return objectCreator;
        }
        return this;
    }
    
}
