package object_creator.classes;

import asciiPanel.AsciiPanel;

public class PrimitiveArray extends ObjectType {
    public int[] ints;
    public double[] doubles;
    public boolean[] bools;

    @Override
    public String getTypeString() { return "Primitive Array"; }

    @Override
    public String[] getFields() {
        return new String[] {
            "NAME",
            "int[] ints",
            "double[] doubles",
            "boolean[] bools"
        };
    }

    @Override
    public void displayObject(AsciiPanel terminal, int x, int y) {
        terminal.write("NAME = " + name, x, y);
        y++;

        String[] lengths = new String[3];
        lengths[0] = (ints == null) ? "0" : Integer.toString(ints.length);
        lengths[1] = (doubles == null) ? "0" : Integer.toString(doubles.length);
        lengths[2] = (bools == null) ? "0" : Integer.toString(bools.length);
        for (int i = 1; i < getFields().length; i++) {
            terminal.write(getFields()[i] + " (len=" + lengths[i - 1] + ")", x, y);
            y++;
        }
    }

    @Override
    public String getStringValue(int i) { return ""; }
    
}
