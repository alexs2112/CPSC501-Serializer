package application;

import java.awt.Color;
import java.awt.event.KeyEvent;
import asciiPanel.AsciiPanel;
import object_creator.ObjectCreator;
import network.ReceivingScreen;
import network.SendingScreen;

public class MainScreen extends Screen {
    private ObjectCreator objectCreator;
    private int selection;
    private String[] options = new String[] {
        "Object Creator",
        "Serialize",
        "Receive Object",
        "Exit"
    };
    private String[] descriptions = new String[] {
        "Library of objects that can be created and edited",
        "Select an object to serialize and send it to another Serializer",
        "Receive a serialized object and deserialize it",
        ""
    };

    public MainScreen() {
        objectCreator = new ObjectCreator(this);
    }

    @Override
    public String title() { return "CPSC 501 - Serializer"; }

    @Override
    public void print(AsciiPanel terminal) {
        super.drawBorder(terminal);

        int x = 4;
        int y = 3;
        for (int i = 0; i < options.length; i++) {
            Color c = Color.WHITE;
            if (i == selection) {
                c = Color.GREEN;
                terminal.writeCenter(descriptions[i], terminal.getHeightInCharacters() - 8);
            }
            terminal.write(options[i], x, y + i, c);
        }

        terminal.writeCenter("Alex Stevenson - 30073617", terminal.getHeightInCharacters() - 2);
    }

    @Override
    public Screen input(KeyEvent key) {
        if (key.getKeyCode() == KeyEvent.VK_DOWN) {
            selection++;
            if (selection >= options.length) { selection = 0; }
        } else if (key.getKeyCode() == KeyEvent.VK_UP) {
            selection--;
            if (selection < 0) { selection = options.length-1; }
        } else if (key.getKeyCode() == KeyEvent.VK_ENTER) {
            switch(selection) {
                case 0:
                    return objectCreator;
                case 1:
                    return new SendingScreen(this, objectCreator.getObjects());
                case 2:
                    return new ReceivingScreen(this);
                case 3:
                    System.exit(0);
            }
        }
        return this;
    }
}
