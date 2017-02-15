package squier.john.typeInformation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
    public void tryToCompareObjectToNonExistantInterface() {
        boolean expected = false;

        boolean actual = reflector.classImplementsInterface(new Object(), "java.lang.HocusPocus");

        Assert.assertEquals(expected, actual);
    }
}
