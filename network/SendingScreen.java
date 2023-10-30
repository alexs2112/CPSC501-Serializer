package network;

import java.util.ArrayList;
import java.awt.Color;
import java.awt.event.KeyEvent;
import org.jdom2.Document;
import asciiPanel.AsciiPanel;
import application.Screen;
import serializer.Serializer;
import object_creator.classes.ObjectType;
import object_creator.helpers.ObjectSelectorSender;

import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class SendingScreen extends Screen {
    private Screen returnScreen;
    private int selection;
    private ArrayList<ObjectType> objects;
    private Sender sender;
    private String errorString = null;
    private Document doc;
    private String docName;
    private String[] options = new String[] {
        "Serialize Object",
        "Send Object to Receiver",
        "Back to Main Menu"
    };

    public SendingScreen(Screen returnScreen, ArrayList<ObjectType> objects) {
        this.returnScreen = returnScreen;
        this.sender = new Sender();
        this.objects = objects;
    }

    @Override
    public String title() { return "Serialization"; }

    @Override
    public void print(AsciiPanel terminal) {
        super.drawBorder(terminal);

        int x = 4;
        int y = 3;
        if (doc != null) {
            terminal.write("Serialized Object: " + docName, x, y++, Color.WHITE);
        } else {
            terminal.write("No serialized object", x, y++, Color.RED);
        }

        terminal.write("Hostname: localhost", x, y++);
        terminal.write("Port: 6666", x, y++);
        y++;

        for (int i = 0; i < options.length; i++) {
            Color c = (i == selection) ? Color.GREEN : Color.WHITE;
            terminal.write(options[i], x, y++, c);
        }

        printMessages(terminal, x, y + 2);

        if (errorString != null) {
            terminal.write("Error: " + errorString, 4, terminal.getHeightInCharacters() - 3, Color.RED);
        }
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
            errorString = null;
            if (selection == 0) {
                return new ObjectSelectorSender(this, objects);
            } else if (selection == 1) {
                if (doc == null) {
                    errorString = "Cannot send an object without serializing it first.";
                } else {
                    sender.sendDocument(doc);
                }
                return this;
            } else if (selection == 2) {
                return returnScreen;
            }
        }
        return this;
    }

    public void serializeObject(ObjectType o) {
        docName = o.name;
        doc = new Serializer().serialize(o);

        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        String xmlString = xmlOutputter.outputString(doc);
        System.out.println(xmlString);
    }

    private void printMessages(AsciiPanel terminal, int x, int y) {
        while (sender.messages.size() > 8) {
            sender.messages.remove(1);
        }
        for (int i = 0; i < sender.messages.size(); i++) {
            String m = sender.messages.get(i);
            Color c = Color.LIGHT_GRAY;
            if (m.startsWith("Error")) { c = Color.RED; }
            else if (m.startsWith("Warning")) { c = Color.YELLOW; }
            terminal.write(m, x, y + i, c);
        }
    }
}
