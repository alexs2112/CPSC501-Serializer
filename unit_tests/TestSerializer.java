package unit_tests;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.util.ArrayList;
import java.util.Vector;
import java.util.List;
import org.jdom2.Document;
import org.jdom2.Element;
import serializer.Serializer;
import object_creator.classes.*;

public class TestSerializer {
    private Serializer serializer;

    @Before
    public void setup() {
        serializer = new Serializer();
    }

    private String getFieldValue(Element element, String fieldName) {
        for (Element f : element.getChildren()) {
            if (f.getAttributeValue("name").equals(fieldName))
                return f.getChildTextNormalize("value");
        }
        return "";
    }
    private String getFieldReference(Element element, String fieldName) {
        for (Element f : element.getChildren()) {
            if (f.getAttributeValue("name").equals(fieldName))
                return f.getChildTextNormalize("reference");
        }
        return "";
    }

    @Test
    public void TestHeader() {
        PrimitiveObject a = new PrimitiveObject();
        
        Document d = serializer.serialize(a);
        Element root = d.getRootElement();
        Element A = root.getChildren().get(0);
        assertEquals("object_creator.classes.PrimitiveObject", A.getAttributeValue("class"));
        assertEquals(Integer.toString(a.hashCode()), A.getAttributeValue("id"));
    }

    @Test
    public void TestHeaderArray() {
        int[] a = new int[] { 1, 2, 3, 4 };
        Document d = serializer.serialize(a);
        Element root = d.getRootElement();
        Element A = root.getChildren().get(0);
        assertEquals("[I", A.getAttributeValue("class"));
        assertEquals(Integer.toString(a.hashCode()), A.getAttributeValue("id"));
        assertEquals("4", A.getAttributeValue("length"));
    }

    @Test
    public void TestHeaderList() {
        ArrayList<Object> o = new ArrayList<Object>();
        o.add(null);
        o.add(null);

        Document d = serializer.serialize(o);
        Element root = d.getRootElement();
        Element A = root.getChildren().get(0);
        assertEquals("java.util.ArrayList", A.getAttributeValue("class"));
        assertEquals(Integer.toString(o.hashCode()), A.getAttributeValue("id"));
    }

    @Test
    public void TestNormalFields() {
        PrimitiveObject a = new PrimitiveObject();
        a.name = "Test";
        a.b = (byte)49;
        a.c = '2';
        a.d = 3.0;
        a.f = 4.0f;
        a.i = 5;
        a.j = 6;
        a.s = 7;
        a.z = true;

        Document d = serializer.serialize(a);
        Element root = d.getRootElement();
        Element A = root.getChildren().get(0);
        assertEquals("Test", getFieldValue(A, "name"));
        assertEquals("49", getFieldValue(A, "b"));
        assertEquals("2", getFieldValue(A, "c"));
        assertEquals("3.0", getFieldValue(A, "d"));
        assertEquals("4.0", getFieldValue(A, "f"));
        assertEquals("5", getFieldValue(A, "i"));
        assertEquals("6", getFieldValue(A, "j"));
        assertEquals("7", getFieldValue(A, "s"));
        assertEquals("true", getFieldValue(A, "z"));
    }

    @Test
    public void TestReferencedFields() {
        PrimitiveObject a = new PrimitiveObject();
        ReferenceObject o = new ReferenceObject();
        o.A = a;

        Document d = serializer.serialize(o);
        Element root = d.getRootElement();
        Element ref = null;
        for (Element e : root.getChildren()) {
            if (e.getAttributeValue("id").equals(Integer.toString(o.hashCode())))
                ref = e;
        }
        if (ref == null) { fail("Could not find object with expected id."); }

        assertEquals(Integer.toString(a.hashCode()), getFieldReference(ref, "A"));
    }

    @Test
    public void TestArrayReferences() {
        PrimitiveArray a = new PrimitiveArray();
        a.ints = new int[] { 0 };
        a.doubles = new double[] { 1.0 };
        a.bools = new boolean[] { true };

        Document d = serializer.serialize(a);
        Element root = d.getRootElement();
        Element ref = null;
        for (Element e : root.getChildren()) {
            if (e.getAttributeValue("id").equals(Integer.toString(a.hashCode())))
                ref = e;
        }
        if (ref == null) { fail("Could not find object with expected id."); }

        assertEquals(Integer.toString(a.ints.hashCode()), getFieldReference(ref, "ints"));
        assertEquals(Integer.toString(a.doubles.hashCode()), getFieldReference(ref, "doubles"));
        assertEquals(Integer.toString(a.bools.hashCode()), getFieldReference(ref, "bools"));
        assertEquals(4, root.getChildren().size());
    }

    @Test
    public void TestListReferences() {
        ReferenceList o = new ReferenceList();
        // Constructor instantiates o.objects

        Document d = serializer.serialize(o);
        Element root = d.getRootElement();
        Element ref = null;
        for (Element e : root.getChildren()) {
            if (e.getAttributeValue("id").equals(Integer.toString(o.hashCode())))
                ref = e;
        }
        if (ref == null) { fail("Could not find object with expected id."); }

        assertEquals(Integer.toString(o.objects.hashCode()), getFieldReference(ref, "objects"));
        assertEquals(2, root.getChildren().size());
    }

    @Test
    public void TestPrimitiveArray() {
        PrimitiveArray o = new PrimitiveArray();
        o.ints = new int[] { 0, 1 };
        o.doubles = new double[] { 2.2, 3.3 };
        o.bools = new boolean[] { true, false };

        Document d = serializer.serialize(o);
        Element root = d.getRootElement();
        for (Element e : root.getChildren()) {
            /* Test ints */
            if (e.getAttributeValue("id").equals(Integer.toString(o.ints.hashCode()))) {
                int[] check = new int[2];
                for (int i = 0; i < 2; i++) {
                    Element c = e.getChildren().get(i);
                    check[i] = Integer.valueOf(c.getTextNormalize());
                }
                assert(o.ints.equals(check));
            }

            /* Test doubles */
            if (e.getAttributeValue("id").equals(Integer.toString(o.doubles.hashCode()))) {
                double[] check = new double[2];
                for (int i = 0; i < 2; i++) {
                    Element c = e.getChildren().get(i);
                    check[i] = Double.valueOf(c.getTextNormalize());
                }
                assert(o.doubles.equals(check));
            }

            /* Test bools */
            if (e.getAttributeValue("id").equals(Integer.toString(o.bools.hashCode()))) {
                boolean[] check = new boolean[2];
                for (int i = 0; i < 2; i++) {
                    Element c = e.getChildren().get(i);
                    check[i] = Boolean.valueOf(c.getTextNormalize());
                }
                assert(o.bools.equals(check));
            }
        }
    }

    @Test
    public void TestReferenceArray() {
        ReferenceArray o = new ReferenceArray();
        PrimitiveObject a = new PrimitiveObject();
        PrimitiveObject b = new PrimitiveObject();
        o.objects = new ObjectType[] { a, b };

        Document d = serializer.serialize(o);
        Element root = d.getRootElement();
        for (Element e : root.getChildren()) {
            if (e.getAttributeValue("id").equals(Integer.toString(o.objects.hashCode()))) {
                Element c = e.getChildren().get(0);
                assertEquals(Integer.toString(a.hashCode()), c.getTextNormalize());
                
                c = e.getChildren().get(1);
                assertEquals(Integer.toString(b.hashCode()), c.getTextNormalize());
            }
        }
    }

    @Test
    public void TestReferenceList() {
        ReferenceList o = new ReferenceList();
        PrimitiveObject a = new PrimitiveObject();
        PrimitiveObject b = new PrimitiveObject();
        o.objects.add(a);
        o.objects.add(b);

        Document d = serializer.serialize(o);
        Element root = d.getRootElement();
        for (Element e : root.getChildren()) {
            if (e.getAttributeValue("id").equals(Integer.toString(o.objects.hashCode()))) {
                Element c = e.getChildren().get(0);
                assertEquals(Integer.toString(a.hashCode()), c.getTextNormalize());
                
                c = e.getChildren().get(1);
                assertEquals(Integer.toString(b.hashCode()), c.getTextNormalize());
            }
        }
    }

    @Test
    public void TestVector() {
        Vector<Integer> v = new Vector<Integer>(3);
        v.add(0);
        v.add(1);
        v.add(2);
        Document d = serializer.serialize(v);

        Element root = d.getRootElement();
        Element vec = root.getChildren().get(0);
        List<Element> children = vec.getChildren("value");
        for (int i = 0; i < children.size(); i++) {
            Element e = children.get(i);
            assertEquals(Integer.toString(v.get(i)), e.getTextNormalize());
        }
    }
}
