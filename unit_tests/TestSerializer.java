package unit_tests;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
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
}
