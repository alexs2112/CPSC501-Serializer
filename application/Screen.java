package application;

import java.awt.event.KeyEvent;
import asciiPanel.AsciiPanel;

public abstract class Screen {
    public abstract String title();
    public abstract void print(AsciiPanel terminal);
    public abstract Screen input(KeyEvent key);

    protected void drawBorder(AsciiPanel terminal) {
        int width = terminal.getWidthInCharacters();
        int height = terminal.getHeightInCharacters();
        for (int x = 0; x < width - 1; x++) {
            terminal.write("=", x, 0);
            terminal.write("=", x, height-1);
        }
        for (int y = 1; y < height - 1; y++) {
            terminal.write("|", 0, y);
            terminal.write("|", width-2, y);
        }

        terminal.write(title(), 8, 0);
    }
}
