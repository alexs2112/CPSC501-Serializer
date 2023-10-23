package object_creator.classes;

import asciiPanel.AsciiPanel;

public class PrimitiveObject extends ObjectType {
    public byte b;
    public char c;
    public double d;
    public float f;
    public int i;
    public long j;
    public short s;
    public boolean z;

    @Override
    public String getTypeString() { return "Primitive"; }

    @Override
    public String[] getFields() {
        return new String[] {
            "NAME",
            "byte b",
            "char c",
            "double d",
            "float f",
            "int i",
            "long j",
            "short s",
            "boolean z"
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
            case 1: return Byte.toString(b);
            case 2: return Character.toString(c);
            case 3: return Double.toString(d);
            case 4: return Float.toString(f);
            case 5: return Integer.toString(this.i);
            case 6: return Long.toString(j);
            case 7: return Short.toString(s);
            case 8: return Boolean.toString(z);
            default: return "";
        }
    }
}
