package object_creator.helpers;

import java.util.ArrayList;

import application.Screen;
import java.awt.event.KeyEvent;
import object_creator.classes.ObjectType;

public class ObjectSelectorArray extends ObjectSelector {
    private int index;
    private ObjectType[] obj_array;

    public ObjectSelectorArray(ModifyArray modifyArray, ObjectType[] obj_array, ArrayList<ObjectType> objects, int index) {
        super(null, modifyArray, objects);
        this.obj_array = obj_array;
        this.index = index;
    }

    @Override
    public Screen input(KeyEvent key) {
        if (key.getKeyCode() == KeyEvent.VK_ENTER) {
            if (obj_array != null && objects.size() > 0) {
                obj_array[index] = objects.get(selection);
                return prevScreen;
            }
        }
        return super.input(key);
    }
}
