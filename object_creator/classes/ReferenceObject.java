package object_creator.classes;

import asciiPanel.AsciiPanel;

public class ReferenceObject extends ObjectType {
    public ObjectType A;
    public ObjectType B;
    public ObjectType C;

    @Override
    public String getTypeString() { return "Reference"; }

    @Override
    public String[] getFields() {
        return new String[] {
            "NAME",
            "Object A",
            "Object B",
            "Object C"
        };
    }

    @Override
    public void displayObject(AsciiPanel terminal, int x, int y) {
        for (int i = 0; i < getFields().length; i++) {
            terminal.write(getFields()[i] + " = " + getStringValue(i), x, y);
            y++;
        }
    }

    @Override
    public String getStringValue(int i) {
        switch(i) {
            case 0: return name;
            case 1: 
                if (A == null) { return "<null>"; }
                else { return A.name + " (" + A.getTypeString() + ")"; }
            case 2: 
                if (B == null) { return "<null>"; }
                else { return B.name + " (" + B.getTypeString() + ")"; }
            case 3: 
                if (C == null) { return "<null>"; }
                else { return C.name + " (" + C.getTypeString() + ")"; }
            default: return "";
        }
    }
    
}
