package network;

import java.awt.event.KeyEvent;
import java.awt.Color;
import application.Screen;
import asciiPanel.AsciiPanel;

public class ReceivingScreen extends Screen {
    private Screen returnScreen;
    private int selection;
    private Receiver receiver;
    private Object object;
    private String docName;
    private String errorString = null;
    private String[] options = new String[] {
        "Receive Object",
        "Object Inspector",
        "Back to Main Menu"
    };

    public ReceivingScreen(Screen returnScreen) {
        this.returnScreen = returnScreen;
        this.receiver = new Receiver();
    }

    @Override
    public String title() { return "Receiving"; }

    @Override
    public void print(AsciiPanel terminal) {
        drawBorder(terminal);

        int x = 4;
        int y = 3;
        if (object != null) {
            terminal.write("Received Object: " + docName, x, y++, Color.WHITE);
        } else {
            terminal.write("No received object", x, y++, Color.RED);
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
                receiver.start();
                object = receiver.deserialize();
            } else if (selection == 1) {
                if (object == null) {
                    errorString = "No objects to inspect.";
                } else {
                    // Return an object inspector here
                }
                return this;
            } else if (selection == 2) {
                return returnScreen;
            }
        }
        return this;
    }

    private void printMessages(AsciiPanel terminal, int x, int y) {
        while (receiver.messages.size() > 8) {
            receiver.messages.remove(1);
        }
        for (int i = 0; i < receiver.messages.size(); i++) {
            String m = receiver.messages.get(i);
            Color c = Color.LIGHT_GRAY;
            if (m.startsWith("Error")) { c = Color.RED; }
            else if (m.startsWith("Warning")) { c = Color.YELLOW; }
            terminal.write(m, x, y + i, c);
        }
    }
}
