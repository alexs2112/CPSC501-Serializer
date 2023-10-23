package application;

import object_creator.ObjectCreator;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import asciiPanel.AsciiPanel;

public class Main extends JFrame implements KeyListener {
    private AsciiPanel terminal;
    private Screen screen;

    private Main() {
        super();
        terminal = new AsciiPanel(80,24);
        add(terminal);
        pack();
        addKeyListener(this);

        screen = new ObjectCreator();
        repaint();
    }

    public static void main(String[] args) {
        Main app = new Main();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setVisible(true);
    }

    public void repaint(){
        terminal.clear();
        screen.print(terminal);
        super.repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        screen = screen.input(e);
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyReleased(KeyEvent e) { }
}
