package visualizer;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import application.Screen;
import asciiPanel.AsciiPanel;
import serializer.helpers.FieldHelper;

public abstract class Visualizer extends Screen {
    private Screen homeScreen;
    private Screen lastScreen;
    private ArrayList<Object> breadcrumb;
    protected Object obj;
    protected int selection;

    public Visualizer(Object obj, Screen lastScreen, Screen homeScreen) {
        this.lastScreen = lastScreen;
        this.homeScreen = homeScreen;
        this.breadcrumb = new ArrayList<Object>();
        setupObject(obj);
        breadcrumb.add(obj);
    }

    public Visualizer(Object obj, Screen lastScreen, Screen homeScreen, ArrayList<Object> breadcrumb) {
        this.lastScreen = lastScreen;
        this.homeScreen = homeScreen;
        this.breadcrumb = breadcrumb;
        setupObject(obj);
        updateBreadcrumb(obj);
    }

    protected abstract void setupObject(Object obj);
    protected abstract int optionNum();

    private void updateBreadcrumb(Object obj) {
        ArrayList<Object> newBread = new ArrayList<Object>();
        for (Object o : breadcrumb) {
            if (o == obj) { break; }
            newBread.add(o);
        }
        newBread.add(obj);
        breadcrumb = newBread;
    }

    @Override
    public String title() { return "Object Visualizer"; }

    @Override
    public void print(AsciiPanel terminal) {
        drawBorder(terminal);
        drawBreadcrumb(terminal);

        int x = 4;
        int y = 5 + optionNum();
        
        Color c = (selection == optionNum()) ? Color.GREEN : Color.WHITE;
        terminal.write("Back", x, y++, c);

        c = (selection == optionNum() + 1) ? Color.GREEN : Color.WHITE;
        terminal.write("Home", x, y++, c);
    }

    private void drawBreadcrumb(AsciiPanel terminal) {
        int y = terminal.getHeightInCharacters() - 2;
        for (int i = 0; i < breadcrumb.size(); i++) {
            Object o = breadcrumb.get(i);
            String s = objectName(o);

            Color c = (i == breadcrumb.size() - 1) ? Color.WHITE : Color.LIGHT_GRAY;
            terminal.write(s, terminal.getWidthInCharacters() - s.length() - 3, y, c);
            y--;
        }
    }

    protected String objectName(Object obj) {
        return obj.getClass().getName() + "@" + Integer.toString(obj.hashCode());
    }

    @Override
    public Screen input(KeyEvent key) {
        if (key.getKeyCode() == KeyEvent.VK_DOWN) {
            selection++;
            if (selection >= optionNum() + 2) { selection = 0; }
        } else if (key.getKeyCode() == KeyEvent.VK_UP) {
            selection--;
            if (selection < 0) { selection = optionNum() + 1; }
        } else if (key.getKeyCode() == KeyEvent.VK_ENTER) {
            if (selection == optionNum()) { return lastScreen; }
            if (selection == optionNum() + 1) { return homeScreen; }
        } else if (key.getKeyCode() == KeyEvent.VK_ESCAPE) {
            return lastScreen;
        }
        return this;
    }

    protected Screen getNextScreen(Object o) {
        if (o.getClass().getName().equals("java.lang.String")) { return new VisualizerNormal(o, this, homeScreen, breadcrumb); }
        else if (FieldHelper.isPrimitive(o)) { return this; }
        else if (o.getClass().isArray()) { return new VisualizerArray(o, this, homeScreen, breadcrumb); }
        else if (o.getClass() == ArrayList.class) { return new VisualizerArray(o, this, homeScreen, breadcrumb); }
        else { return new VisualizerNormal(o, this, homeScreen, breadcrumb); }
    }

    public static Screen getVisualizer(Object o, Screen homeScreen) {
        if (o.getClass().getName().equals("java.lang.String")) { return new VisualizerNormal(o, homeScreen, homeScreen); }
        else if (FieldHelper.isPrimitive(o)) { return homeScreen; } // This should never happen
        else if (o.getClass().isArray()) { return new VisualizerArray(o, homeScreen, homeScreen); }
        else if (o.getClass() == ArrayList.class) { return new VisualizerNormal(o, homeScreen, homeScreen); }
        else { return new VisualizerNormal(o, homeScreen, homeScreen); }
    }
}
