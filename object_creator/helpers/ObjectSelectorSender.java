package object_creator.helpers;

import java.util.ArrayList;
import java.awt.event.KeyEvent;
import application.Screen;
import network.SendingScreen;
import object_creator.classes.ObjectType;

public class ObjectSelectorSender extends ObjectSelector {
    private SendingScreen sender;

    public ObjectSelectorSender(SendingScreen sender, ArrayList<ObjectType> objects) {
        super(null, sender, objects);
        this.sender = sender;
    }

    @Override
    public Screen input(KeyEvent key) {
        if (key.getKeyCode() == KeyEvent.VK_ENTER) {
            if (objects.size() > 0)
            sender.serializeObject(objects.get(selection));
            return prevScreen;
        }
        return super.input(key);
    }
}
