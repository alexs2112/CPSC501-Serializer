package unit_tests;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;

import serializer.Serializer;
import object_creator.classes.*;

public class TestMap {
    private Serializer serializer;

    @Before
    public void setup() {
        serializer = new Serializer();
    }

    private int mapSize() {
        return serializer.getObjects().keySet().size();
    }
    
    @Test
    public void TestBasic() {
        PrimitiveObject o = new PrimitiveObject();
        serializer.serialize(o);
        assertEquals(1, mapSize());
    }

    @Test
    public void TestNullFields() {
        ReferenceObject o = new ReferenceObject();
        serializer.serialize(o);
        assertEquals(1, mapSize());
    }

    @Test
    public void TestRecursion() {
        ReferenceObject o = new ReferenceObject();
        PrimitiveObject a = new PrimitiveObject();
        PrimitiveObject b = new PrimitiveObject();
        PrimitiveObject c = new PrimitiveObject();
        o.A = a;
        o.B = b;
        o.C = c;
        serializer.serialize(o);
        assertEquals(4, mapSize());
    }

    @Test
    public void TestDuplicateValues() {
        ReferenceObject o = new ReferenceObject();
        PrimitiveObject a = new PrimitiveObject();
        o.A = a;
        o.B = a;
        o.C = a;
        serializer.serialize(o);
        assertEquals(2, mapSize());
    }

    @Test
    public void TestNestedRecursion() {
        ReferenceObject o = new ReferenceObject();
        PrimitiveObject a = new PrimitiveObject();
        PrimitiveObject b = new PrimitiveObject();
        ReferenceObject ro = new ReferenceObject();
        ro.A = a;
        ro.B = b;
        o.A = ro;
        serializer.serialize(o);
        assertEquals(4, mapSize());

        serializer.serialize(ro);
        assertEquals(3, mapSize());
    }

    @Test
    public void TestNestedDuplicates() {
        ReferenceObject o = new ReferenceObject();
        PrimitiveObject a = new PrimitiveObject();
        PrimitiveObject b = new PrimitiveObject();
        ReferenceObject ro = new ReferenceObject();
        ro.A = a;
        ro.B = b;
        ro.C = a;
        o.A = ro;
        o.B = a;
        serializer.serialize(o);
        assertEquals(4, mapSize());

        serializer.serialize(ro);
        assertEquals(3, mapSize());
    }

    @Test
    public void TestBasicArray() {
        PrimitiveObject a = new PrimitiveObject();
        PrimitiveObject b = new PrimitiveObject();
        PrimitiveObject c = new PrimitiveObject();
        ReferenceArray o = new ReferenceArray();
        o.objects = new ObjectType[] {
            a, b, c
        };
        serializer.serialize(o);
        assertEquals(4, mapSize());
    }

    @Test
    public void TestPrimitiveArray() {
        PrimitiveArray o = new PrimitiveArray();
        o.ints = new int[] { 0, 1, 2 };
        o.doubles = new double[] { 0.0, 1.1, 2.2 };
        o.bools = new boolean[] { true, false, true };
        serializer.serialize(o);
        assertEquals(1, mapSize());
    }

    @Test
    public void TestDuplicateArray() {
        PrimitiveObject a = new PrimitiveObject();
        ReferenceArray o = new ReferenceArray();
        o.objects = new ObjectType[] {
            a, a, a, a, a
        };
        serializer.serialize(o);
        assertEquals(2, mapSize());
    }

    @Test
    public void TestNestedArray() {
        PrimitiveObject a = new PrimitiveObject();
        PrimitiveObject b = new PrimitiveObject();
        PrimitiveObject c = new PrimitiveObject();
        ReferenceArray arr1 = new ReferenceArray();
        arr1.objects = new ObjectType[] {
            a, b
        };
        ReferenceArray arr2 = new ReferenceArray();
        arr2.objects = new ObjectType[] {
            c
        };
        ReferenceArray o = new ReferenceArray();
        o.objects = new ObjectType[] {
            arr1, arr2
        };
        serializer.serialize(o);
        assertEquals(6, mapSize());
    }

    @Test
    public void TestArrayWithReferences() {
        PrimitiveObject a = new PrimitiveObject();
        PrimitiveObject b = new PrimitiveObject();
        ReferenceObject r = new ReferenceObject();
        r.A = a;
        r.B = b;
        PrimitiveObject c = new PrimitiveObject();
        ReferenceArray o = new ReferenceArray();
        o.objects = new ObjectType[] {
            r, c
        };
        serializer.serialize(o);
        assertEquals(5, mapSize());
    }

    @Test
    public void TestArrayList() {
        PrimitiveObject a = new PrimitiveObject();
        PrimitiveObject b = new PrimitiveObject();
        ReferenceList o = new ReferenceList();
        o.objects.add(a);
        o.objects.add(b);
        serializer.serialize(o);
        assertEquals(3, mapSize());
    }

    @Test
    public void TestArrayListDuplicates() {
        PrimitiveObject a = new PrimitiveObject();
        PrimitiveObject b = new PrimitiveObject();
        ReferenceList o = new ReferenceList();
        o.objects.add(a);
        o.objects.add(b);
        o.objects.add(a);
        o.objects.add(b);
        serializer.serialize(o);
        assertEquals(3, mapSize());
    }

    @Test
    public void TestArrayListNestedArray() {
        PrimitiveObject a = new PrimitiveObject();
        PrimitiveObject b = new PrimitiveObject();
        ReferenceArray arr1 = new ReferenceArray();
        arr1.objects = new ObjectType[] {
            a, b
        };
        PrimitiveObject c = new PrimitiveObject();
        ReferenceArray arr2 = new ReferenceArray();
        arr2.objects = new ObjectType[] {
            c
        };

        ReferenceList o = new ReferenceList();
        o.objects.add(arr1);
        o.objects.add(arr2);

        serializer.serialize(o);
        assertEquals(6, mapSize());
    }
}
