package object_creator.classes;

import java.util.ArrayList;

public class ReferenceList extends ObjectType {
    public ArrayList<ObjectType> objects;

    public ReferenceList() {
        objects = new ArrayList<ObjectType>();
    }
}
