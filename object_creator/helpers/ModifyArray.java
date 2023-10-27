package object_creator.helpers;

import java.awt.Color;
import java.awt.event.KeyEvent;
import application.Screen;
import asciiPanel.AsciiPanel;

public abstract class ModifyArray extends Screen {
    protected int selection;
    protected boolean editMode;
    protected String editString = "";
    protected String errorString = "";

    /* Get length of the array we are modifying */
    protected abstract int getLength();

    /* Print the info of the array */
    protected abstract void printArray(AsciiPanel terminal);

    /* Save the array */
    protected abstract void saveNewArray();

    /* Figure out what screen to return to */
    protected abstract Screen getReturnScreen();

    /* Save the selected value of the array */
    protected abstract boolean saveSelection(int index);

    /* Resize the array to a given length */
    protected abstract void resizeArray(int newLen);

    @Override
    public String title() { return "Edit Array"; }

    @Override
    public void print(AsciiPanel terminal) {
        drawBorder(terminal);

        printLength(terminal);
        printArray(terminal);

        if (errorString.length() > 0)
            terminal.write("Error: " + errorString, 8, terminal.getHeightInCharacters() - 3, Color.RED);
    }

    @Override
    public Screen input(KeyEvent key) {
        int length = getLength();

        if (key.getKeyCode() == KeyEvent.VK_DOWN) {
            if (!editMode) {
                selection++;
                if (selection >= length + 2) { selection = 0; }
            }
        } else if (key.getKeyCode() == KeyEvent.VK_UP) {
            if (!editMode) {
                selection--;
                if (selection < 0) { selection = length + 1; }
            }
        } else if (key.getKeyCode() == KeyEvent.VK_ENTER) {
            if (selection == length + 1) {
                saveNewArray();

                return getReturnScreen();
            } else if (!editMode) {
                editMode = true;
            } else {
                boolean pass = saveEdit();
                if (pass) {
                    editMode = false;
                    editString = "";
                    errorString = "";
                }
            }
        } else if (key.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (editMode) {
                editMode = false;
                editString = "";
                errorString = "";
            } else {
                return getReturnScreen();
            }
        } else if (editMode) {
            try {
                if (key.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    if (editString.length() > 0)
                        editString = editString.substring(0, editString.length() - 1);
                } else {
                    char c = key.getKeyChar();
                    if (c >= 0 && c < 256)
                        editString += c;
                }
            } catch (java.lang.IllegalArgumentException e) { /* Ignore invalid characters */ }
        }
        return this;
    }

    private void printLength(AsciiPanel terminal) {
        Color c = (selection == 0) ? Color.GREEN : Color.WHITE;
        String s;
        if (selection == 0 && editMode) {
            s = "Length = " + editString;
            terminal.write(" ", 4 + s.length(), 5, Color.BLACK, Color.LIGHT_GRAY);
        } else {
            s = "Length = " + Integer.toString(getLength());
        }
        terminal.write(s, 4, 5, c);
    }

    private boolean saveEdit() {
        if (selection == 0) {
            try {
                int newLen = Integer.valueOf(editString);
                resizeArray(newLen);
                return true;
            } catch (Exception e) {
                errorString = "Length must be of type Integer";
                return false;
            }
        }

        return saveSelection(selection - 1);
    }
}
