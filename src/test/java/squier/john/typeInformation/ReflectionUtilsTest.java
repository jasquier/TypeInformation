package squier.john.typeInformation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * Created by johnsquier on 2/15/17.
 */
public class ReflectionUtilsTest {

    ReflectionUtils reflectionUtils;

    @Before
    public void setup() {
        reflectionUtils = new ReflectionUtils();
    }

    @Test
    public void objectDoesImplementInterfaceTest() {
        boolean expected = true;
        boolean actual = reflectionUtils.classImplementsInterface(new Scanner(System.in), "java.io.Closeable");

        Assert.assertEquals("I expect Scanner(System.in) to implement \"java.io.Closeable\"",
                expected, actual);
    }

    @Test
    public void objectDoesNotImplementInterfaceTest() {
        boolean expected = false;
        boolean actual = reflectionUtils.classImplementsInterface(new Integer(10), "java.lang.Iterable");

        Assert.assertEquals("I don't expect Integer() to implement \"java.lang.Iterable\"",
                expected, actual);
    }

    @Test
    public void classObjectDoesImplementInterfaceTest() {
        boolean expected = true;
        boolean actual = reflectionUtils.classImplementsInterface("string".getClass(), "java.lang.CharSequence");

        Assert.assertEquals("I expect String.class to implement \"java.lang.CharSequence\"",
                expected, actual);
    }

    @Test
    public void classObjectDoesNotImplementInterfaceTest() {
        boolean expected = false;
        boolean actual = reflectionUtils.classImplementsInterface(new Integer(10).getClass(), "java.lang.Iterable");

        Assert.assertEquals("I don't expect Integer.class to implement \"java.lang.Iterable\"",
                expected, actual);
    }

    @Test
    public void classNameDoesImplementInterfaceTest() {
        boolean expected = true;
        boolean actual = reflectionUtils.classImplementsInterface("java.lang.String", "java.lang.CharSequence");

        Assert.assertEquals("I expect \"java.lang.String\" to implement \"java.lang.CharSequence\"",
                expected, actual);
    }

    @Test
    public void classNameDoesNotImplementInterfaceTest() {
        boolean expected = false;
        boolean actual = reflectionUtils.classImplementsInterface("java.lang.Integer", "java.lang.Iterable");

        Assert.assertEquals("I don't expect \"java.lang.Integer\" to implement\"java.lang.Iterable\"",
                expected, actual);
    }

    @Test
    public void tryToCompareObjectToNonExistentInterfaceTest() {
        boolean expected = false;
        boolean actual = reflectionUtils.classImplementsInterface(new Object(), "abra.kadabra.Allakhazham");

        Assert.assertEquals("I don't expect Object() to implement \"abra.kadabra.Allakhazham\"",
                expected, actual);
    }

    @Test
    public void tryToCompareClassObjectToNonExistentInterfaceTest() {
        boolean expected = false;
        boolean actual = reflectionUtils.classImplementsInterface(new Object().getClass(), "abra.kadabra.Allakhazham");

        Assert.assertEquals("I don't expect Object.class to implement \"abra.kadabra.Allakhazham\"",
                expected, actual);
    }

    @Test
    public void tryToCompareClassNameToNonExistentInterfaceTest() {
        boolean expected = false;
        boolean actual = reflectionUtils.classImplementsInterface("java.lang.Integer", "abra.kadabra.Allakhazham");

        Assert.assertEquals("I don't expect \"java.lang.Integer\" to implement \"abra.kadabra.Allakhazham\"",
                expected, actual);
    }

    @Test
    public void tryToCompareObjectToNullInterface() {
        boolean expected = false;
        boolean actual = reflectionUtils.classImplementsInterface(new Object(), null);

        Assert.assertEquals("I don't expect Object() to implement null",
                expected, actual);
    }

    @Test
    public void tryToCompareClassObjectToNullInterface() {
        boolean expected = false;
        boolean actual = reflectionUtils.classImplementsInterface(new Object().getClass(), null);

        Assert.assertEquals("I don't expect Object.class to implement null",
                expected, actual);
    }

    @Test
    public void tryToCompareClassNameToNullInterface() {
        boolean expected = false;
        boolean actual = reflectionUtils.classImplementsInterface("java.lang.Integer", null);

        Assert.assertEquals("I don't expect \"java.lang.Integer\" to implement null",
                expected, actual);
    }

    @Test
    public void stringIsNotAClassNameButAStringLiteralTest() {
        boolean expected = true;
        boolean actual = reflectionUtils.classImplementsInterface(" ", "java.lang.CharSequence");

        Assert.assertEquals("I expect \" \" to implement \"java.lang.CharSequence\"",
                expected, actual);
    }

    @Test
    public void listAllMembersBufferedWriterTest() {
        String expected = "Fields\n" +
                "Constructors\n" +
                "BufferedWriter : public java.io.BufferedWriter(java.io.Writer)\n" +
                "BufferedWriter : public java.io.BufferedWriter(java.io.Writer, int)\n" +
                "Methods\n" +
                "BufferedWriter : public void close()\n" +
                "BufferedWriter : public void flush()\n" +
                "BufferedWriter : public void newLine()\n" +
                "BufferedWriter : public void write([C, int, int)\n" +
                "BufferedWriter : public void write(int)\n" +
                "BufferedWriter : public void write(java.lang.String, int, int)\n" +
                "Fields\n" +
                "Constructors\n" +
                "Methods\n" +
                "Writer : public class java.io.Writer append(java.lang.CharSequence, int, int)\n" +
                "Writer : public class java.io.Writer append(char)\n" +
                "Writer : public class java.io.Writer append(java.lang.CharSequence)\n" +
                "Writer : public volatile interface java.lang.Appendable append(char)\n" +
                "Writer : public volatile interface java.lang.Appendable append(java.lang.CharSequence, int, int)\n" +
                "Writer : public volatile interface java.lang.Appendable append(java.lang.CharSequence)\n" +
                "Writer : public abstract void close()\n" +
                "Writer : public abstract void flush()\n" +
                "Writer : public void write([C)\n" +
                "Writer : public abstract void write([C, int, int)\n" +
                "Writer : public void write(int)\n" +
                "Writer : public void write(java.lang.String, int, int)\n" +
                "Writer : public void write(java.lang.String)\n" +
                "Fields\n" +
                "Constructors\n" +
                "Object : public java.lang.Object()\n" +
                "Methods\n" +
                "Object : public boolean equals(java.lang.Object)\n" +
                "Object : public final native class java.lang.Class getClass()\n" +
                "Object : public native int hashCode()\n" +
                "Object : public final native void notify()\n" +
                "Object : public final native void notifyAll()\n" +
                "Object : public class java.lang.String toString()\n" +
                "Object : public final void wait(long, int)\n" +
                "Object : public final native void wait(long)\n" +
                "Object : public final void wait()\n";

        String actual = null;

        try { actual = reflectionUtils.listAllMembers(new BufferedWriter(new FileWriter("test.txt"))); }
        catch ( Exception e ) { }

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void listAllMembersBooleanTest() {
        String expected = "Fields\n" +
                "Boolean : public static final Boolean FALSE\n" +
                "Boolean : public static final Boolean TRUE\n" +
                "Boolean : public static final Class TYPE\n" +
                "Constructors\n" +
                "Boolean : public java.lang.Boolean(boolean)\n" +
                "Boolean : public java.lang.Boolean(java.lang.String)\n" +
                "Methods\n" +
                "Boolean : public boolean booleanValue()\n" +
                "Boolean : public static int compare(boolean, boolean)\n" +
                "Boolean : public int compareTo(java.lang.Boolean)\n" +
                "Boolean : public volatile int compareTo(java.lang.Object)\n" +
                "Boolean : public boolean equals(java.lang.Object)\n" +
                "Boolean : public static boolean getBoolean(java.lang.String)\n" +
                "Boolean : public static int hashCode(boolean)\n" +
                "Boolean : public int hashCode()\n" +
                "Boolean : public static boolean logicalAnd(boolean, boolean)\n" +
                "Boolean : public static boolean logicalOr(boolean, boolean)\n" +
                "Boolean : public static boolean logicalXor(boolean, boolean)\n" +
                "Boolean : public static boolean parseBoolean(java.lang.String)\n" +
                "Boolean : public class java.lang.String toString()\n" +
                "Boolean : public static class java.lang.String toString(boolean)\n" +
                "Boolean : public static class java.lang.Boolean valueOf(java.lang.String)\n" +
                "Boolean : public static class java.lang.Boolean valueOf(boolean)\n" +
                "Fields\n" +
                "Constructors\n" +
                "Object : public java.lang.Object()\n" +
                "Methods\n" +
                "Object : public boolean equals(java.lang.Object)\n" +
                "Object : public final native class java.lang.Class getClass()\n" +
                "Object : public native int hashCode()\n" +
                "Object : public final native void notify()\n" +
                "Object : public final native void notifyAll()\n" +
                "Object : public class java.lang.String toString()\n" +
                "Object : public final void wait(long, int)\n" +
                "Object : public final native void wait(long)\n" +
                "Object : public final void wait()\n";

        String actual = reflectionUtils.listAllMembers(new Boolean(true));

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getClassHierarchyBooleanTest() {
        String expected = "java.lang.Object\n  java.lang.Boolean\n";
        String actual = reflectionUtils.getClassHierarchy(new Boolean(true));

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getClassHierarchyTreeMapTest() {
        String expected = "java.lang.Object\n  java.util.AbstractMap\n    java.util.TreeMap\n";
        String actual = reflectionUtils.getClassHierarchy(new TreeMap<String, String>());

        Assert.assertEquals(expected, actual);
    }

}