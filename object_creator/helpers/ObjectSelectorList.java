package object_creator.helpers;

import java.util.ArrayList;

import application.Screen;
import java.awt.event.KeyEvent;
import object_creator.classes.ObjectType;
import object_creator.handlers.ReferenceListHandler;

public class ObjectSelectorList extends ObjectSelector {
    private int index;
    private ArrayList<ObjectType> obj_list;

    public ObjectSelectorList(ReferenceListHandler refListHandler, ArrayList<ObjectType> obj_list, ArrayList<ObjectType> objects) {
        super(null, refListHandler, objects);
        this.obj_list = obj_list;
        this.index = -1;
    }

    public ObjectSelectorList(ReferenceListHandler refListHandler, ArrayList<ObjectType> obj_list, ArrayList<ObjectType> objects, int index) {
        super(null, refListHandler, objects);
        this.obj_list = obj_list;
        this.index = index;
    }

    @Override
    public Screen input(KeyEvent key) {
        if (key.getKeyCode() == KeyEvent.VK_ENTER) {
            if (obj_list != null && objects.size() > 0) {
                if (index == -1) {
                    obj_list.add(objects.get(selection));
                } else {
                    obj_list.remove(index);
                    obj_list.add(index, objects.get(selection));
                }
                return prevScreen;
            }
        }
        return super.input(key);
    }
}
