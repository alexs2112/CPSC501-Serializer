package object_creator.classes;

import java.util.ArrayList;
import asciiPanel.AsciiPanel;

public class ReferenceList extends ObjectType {
    public ArrayList<ObjectType> objects;

    public ReferenceList() {
        objects = new ArrayList<ObjectType>();
    }

    @Override
    public String getTypeString() { return "Reference List"; }

    @Override
    public String[] getFields() {
        return new String[] {
            "NAME",
            "List<Object> objects"
        };
    }

    @Override
    public void displayObject(AsciiPanel terminal, int x, int y) {
        terminal.write("NAME = " + name, x, y);
        y++;

        terminal.write("List<Object> objects (len=" + (objects == null ? "0" : Integer.toString(objects.size())) + ")", x, y);
    }

    @Override
    public String getStringValue(int i) { return ""; }
    
}
