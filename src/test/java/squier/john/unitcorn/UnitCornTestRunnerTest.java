package squier.john.unitcorn;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;

/**
 * Created by johnsquier on 2/16/17.
 */
public class UnitCornTestRunnerTest {

    UnitCornTestRunner unitCornTestRunner;

    @Before
    public void setup() throws InitializationError {
        unitCornTestRunner = new UnitCornTestRunner();
    }

    @Test
    public void runTestMethodFromClassReflectionUtilsTest() {
        Result expected = new Result("objectDoesImplementInterfaceTest", TestStatus.SUCCESS);
        Result actual = unitCornTestRunner.runTest(ReflectionUtilsTest.class, "objectDoesImplementInterfaceTest");

        Assert.assertTrue(expected.equals(actual));
    }

    @Test
    public void runSuccessfulTestFromDummyTests() {
        Result expected = new Result("testThatPasses", TestStatus.SUCCESS);
        Result actual = unitCornTestRunner.runTest(DummyTests.class, "testThatPasses");

        Assert.assertTrue(expected.equals(actual));
    }

    @Test
    public void runFailureTestFromDummyTests() {
        Result expected = new Result("testThatFails", TestStatus.FAILURE);
        Result actual = unitCornTestRunner.runTest(DummyTests.class, "testThatFails");

        Assert.assertTrue(expected.equals(actual));
    }

    @Test
    public void runTestThatDoesntExist() {
        Result expected = new Result("testThatDoesntExist",TestStatus.NON_EXISTENT_METHOD);
        Result actual = unitCornTestRunner.runTest(DummyTests.class, "testThatDoesntExist");

        Assert.assertTrue(expected.equals(actual));
    }

    @Test
    public void runTestOnClassWithoutNoArgConstructor() {
        class PrivateConstructor{ private PrivateConstructor(){} public void aMethod(){} }

        Result expected = new Result("aMethod", TestStatus.CLASS_INSTANTIATION_FAILURE);
        Result actual = unitCornTestRunner.runTest(PrivateConstructor.class, "aMethod");

        Assert.assertTrue(expected.equals(actual));
    }
}
