package object_creator.classes;

import asciiPanel.AsciiPanel;

public abstract class ObjectType {
    public String name = "<No Name>";
    public abstract String getTypeString();

    public abstract String[] getFields();
    public abstract void displayObject(AsciiPanel terminal, int x, int y);
    public abstract String getStringValue(int i);
}
