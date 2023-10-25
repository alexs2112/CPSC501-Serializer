package object_creator.classes;

import asciiPanel.AsciiPanel;

public class ReferenceArray extends ObjectType {
    public ObjectType[] objects;

    @Override
    public String getTypeString() { return "Reference Array"; }

    @Override
    public String[] getFields() {
        return new String[] {
            "NAME",
            "Object[] objects"
        };
    }

    @Override
    public void displayObject(AsciiPanel terminal, int x, int y) {
        terminal.write("NAME = " + name, x, y);
        y++;

        terminal.write("Object[] objects (len=" + (objects == null ? "0" : Integer.toString(objects.length)) + ")", x, y);
    }

    @Override
    public String getStringValue(int i) { return ""; }
    
}
