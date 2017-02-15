package squier.john.typeInformation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Scanner;

/**
 * Created by johnsquier on 2/15/17.
 */
public class ReflectorTest {

    Reflector reflector;

    @Before
    public void setup() {
        reflector = new Reflector();
    }

    @Test
    public void objectDoesImplementInterfaceTest() {
        boolean expected = true;

        boolean actual = reflector.classImplementsInterface(new Scanner(System.in), "java.io.Closeable");

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void objectDoesNotImplementInterfaceTest() {
        boolean expected = false;

        boolean actual = reflector.classImplementsInterface(new Integer(10), "java.lang.Iterable");

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void classObjectDoesImplementInterfaceTest() {
        boolean expected = true;

        boolean actual = reflector.classImplementsInterface("string".getClass(), "java.lang.CharSequence");

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void classObjectDoesNotImplementInterfaceTest() {
        boolean expected = false;

        boolean actual = reflector.classImplementsInterface(new Integer(10).getClass(), "java.lang.Iterable");

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void classNameDoesImplementInterfaceTest() {
        boolean expected = true;

        boolean actual = reflector.classImplementsInterface("java.lang.String", "java.lang.CharSequence");

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void classNameDoesNotImplementInterfaceTest() {
        boolean expected = false;

        boolean actual = reflector.classImplementsInterface("java.lang.Integer", "java.lang.Iterable");

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void stringIsNotAClassNameButAStringLiteralTest() {
        boolean expected = true;

        boolean actual = reflector.classImplementsInterface(" ", "java.lang.CharSequence");

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void tryToCompareObjectToNonExistentInterface() {
        boolean expected = false;

        boolean actual = reflector.classImplementsInterface(new Object(), "abra.kadabra.Allakhazham");

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void listAllMembersBufferedWriter() {
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
        try { actual = reflector.listAllMembers(new BufferedWriter(new FileWriter(" "))); }
        catch ( Exception e ) { }

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void listAllMembersBoolean() {
        String expected = "Fields\n" +
                "Boolean : public static final Boolean TRUE\n" +
                "Boolean : public static final Boolean FALSE\n" +
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

        String actual = reflector.listAllMembers(new Boolean(true));

        Assert.assertEquals(expected, actual);
    }
}
