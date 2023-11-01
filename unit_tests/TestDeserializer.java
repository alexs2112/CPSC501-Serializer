package unit_tests;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import org.jdom2.Document;
import java.util.ArrayList;
import serializer.Serializer;
import deserializer.Deserializer;
import object_creator.classes.*;

public class TestDeserializer {
    private Serializer serializer;
    private Deserializer deserializer;
    
    @Before
    public void setup() {
        this.serializer = new Serializer();
        this.deserializer = new Deserializer();
    }

    private void assertSamePrimitives(PrimitiveObject o, PrimitiveObject p) {
        assertEquals(o.name, p.name);
        assertEquals(o.b, p.b);
        assertEquals(o.c, p.c);
        assert(o.d == p.d);
        assert(o.f == p.f);
        assertEquals(o.i, p.i);
        assertEquals(o.j, p.j);
        assertEquals(o.s, p.s);
        assertEquals(o.z, p.z);
    }

    @Test
    public void TestPrimitive() {
        PrimitiveObject o = new PrimitiveObject();
        o.name = "Test";
        o.b = 49;
        o.c = '2';
        o.d = 3.0;
        o.f = 4.0f;
        o.i = 5;
        o.j = 6;
        o.s = 7;
        o.z = true;

        Document d = serializer.serialize(o);
        Object r = deserializer.deserialize(d);

        assertEquals(PrimitiveObject.class, r.getClass());
        PrimitiveObject p = (PrimitiveObject)r;

        assertSamePrimitives(o, p);
    }

    @Test
    public void TestReference() {
        PrimitiveObject a = new PrimitiveObject();
        a.name = "Test";
        a.b = 49;
        a.c = '2';
        a.d = 3.0;
        a.f = 4.0f;
        a.i = 5;
        a.j = 6;
        a.s = 7;
        a.z = true;
        PrimitiveObject b = new PrimitiveObject();
        b.c = ' ';
        PrimitiveObject c = new PrimitiveObject();
        c.c = ' ';
        ReferenceObject o = new ReferenceObject();
        o.A = a;
        o.B = b;
        o.C = c;

        Document d = serializer.serialize(o);
        Object r = deserializer.deserialize(d);

        assertEquals(ReferenceObject.class, r.getClass());
        ReferenceObject ro = (ReferenceObject)r;
        assertSamePrimitives(a, (PrimitiveObject)ro.A);
        assertSamePrimitives(b, (PrimitiveObject)ro.B);
        assertSamePrimitives(c, (PrimitiveObject)ro.C);
    }

    @Test
    public void TestPrimitiveArray() {
        PrimitiveArray o = new PrimitiveArray();
        int[] newInts = new int[] { 0, 1, 2 };
        o.ints = newInts;
        double[] newDoubles = new double[] { 3.3, 4.4, 5.5 };
        o.doubles = newDoubles;
        boolean[] newBools = new boolean[] { true, false };
        o.bools = newBools;

        Document d = serializer.serialize(o);
        Object r = deserializer.deserialize(d);

        assertEquals(PrimitiveArray.class, r.getClass());
        PrimitiveArray a = (PrimitiveArray)r;
        assert(newInts.equals(a.ints));
        assert(newDoubles.equals(a.doubles));
        assert(newBools.equals(a.bools));
    }

    @Test
    public void TestReferenceArray() {
        PrimitiveObject a = new PrimitiveObject();
        a.name = "Test";
        a.b = 49;
        a.c = '2';
        a.d = 3.0;
        a.f = 4.0f;
        a.i = 5;
        a.j = 6;
        a.s = 7;
        a.z = true;
        PrimitiveObject b = new PrimitiveObject();
        b.c = ' ';
        PrimitiveObject c = new PrimitiveObject();
        c.c = ' ';

        ObjectType[] objs = new ObjectType[] { b, a, c };
        ReferenceArray o = new ReferenceArray();
        o.objects = objs;

        Document d = serializer.serialize(o);
        Object r = deserializer.deserialize(d);

        assertEquals(ReferenceArray.class, r.getClass());
        ReferenceArray arr = (ReferenceArray)r;

        for (int i = 0; i < objs.length; i++) {
            assertSamePrimitives((PrimitiveObject)objs[i], (PrimitiveObject)arr.objects[i]);
        }
    }

    @Test
    public void TestReferenceList() {
        PrimitiveObject a = new PrimitiveObject();
        a.name = "Test";
        a.b = 49;
        a.c = '2';
        a.d = 3.0;
        a.f = 4.0f;
        a.i = 5;
        a.j = 6;
        a.s = 7;
        a.z = true;
        PrimitiveObject b = new PrimitiveObject();
        b.c = ' ';
        PrimitiveObject c = new PrimitiveObject();
        c.c = ' ';

        ArrayList<ObjectType> objs = new ArrayList<ObjectType>();
        objs.add(b);
        objs.add(c);
        objs.add(a);
        ReferenceList o = new ReferenceList();
        o.objects = objs;

        Document d = serializer.serialize(o);
        Object r = deserializer.deserialize(d);

        assertEquals(ReferenceList.class, r.getClass());
        ReferenceList arr = (ReferenceList)r;

        for (int i = 0; i < objs.size(); i++) {
            assertSamePrimitives((PrimitiveObject)objs.get(i), (PrimitiveObject)arr.objects.get(i));
        }
    }

    @Test
    public void TestDeserialization() {
        PrimitiveObject a = new PrimitiveObject();
        a.name = "Test";
        a.b = 49;
        a.c = '2';
        a.d = 3.0;
        a.f = 4.0f;
        a.i = 5;
        a.j = 6;
        a.s = 7;
        a.z = true;

        ReferenceObject b = new ReferenceObject();
        b.A = a;
        b.B = a;
        b.C = null;

        PrimitiveArray c = new PrimitiveArray();
        int[] newInts = new int[] { 0, 1, 2 };
        c.ints = newInts;
        double[] newDoubles = new double[] { 3.3, 4.4, 5.5 };
        c.doubles = newDoubles;
        boolean[] newBools = new boolean[] { true, false };
        c.bools = newBools;

        ReferenceArray d = new ReferenceArray();
        ObjectType[] objArray = new ObjectType[] { a, b, c };
        d.objects = objArray;

        ReferenceList e = new ReferenceList();
        ArrayList<ObjectType> objList = new ArrayList<ObjectType>();
        objList.add(a);
        objList.add(b);
        objList.add(c);
        objList.add(d);
        objList.add(e);
        e.objects = objList;

        Document doc = serializer.serialize(e);
        Object r = deserializer.deserialize(doc);
        assertEquals(ReferenceList.class, r.getClass());
    }

    @Test
    public void TestString() {
        String s = "Hello, World!";
        Document doc = serializer.serialize(s);
        Object r = deserializer.deserialize(doc);
        assertEquals("java.lang.String", r.getClass().getName());
        assertEquals(s, (String)r);
    }
}
