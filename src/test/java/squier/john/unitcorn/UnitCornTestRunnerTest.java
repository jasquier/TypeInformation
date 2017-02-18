package squier.john.unitcorn;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by johnsquier on 2/16/17.
 */
public class UnitCornTestRunnerTest {

    UnitCornTestRunner unitCornTestRunner;

    @Before
    public void setup() {
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
    public void runTestThatDoesNotExist() {
        Result expected = new Result("testThatDoesNotExist",TestStatus.NON_EXISTENT_METHOD);
        Result actual = unitCornTestRunner.runTest(DummyTests.class, "testThatDoesNotExist");

        Assert.assertTrue(expected.equals(actual));
    }

    @Test
    public void runTestOnClassWithoutNoArgConstructor() {
        class PrivateConstructor{ private PrivateConstructor(){} public void aMethod(){} }

        Result expected = new Result("aMethod", TestStatus.CLASS_INSTANTIATION_FAILURE);
        Result actual = unitCornTestRunner.runTest(PrivateConstructor.class, "aMethod");

        Assert.assertTrue(expected.equals(actual));
    }

    @Test
    public void runTestsInClassDummyTestsAndGenerateResults() {
        String expected = "Class Tested : DummyTests\n" +
                            "\t  Method : testThatFails()\n" +
                            "\t  Result : FAILURE\n" +
                            "\n" +
                            "\t  Method : testThatPasses()\n" +
                            "\t  Result : SUCCESS\n" +
                            "\n" +
                            "1 Test Passed 1 Test Failed 0 Tests Broken\n";
        String actual = unitCornTestRunner.runTests(DummyTests.class);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void runTestsInReflectionUtilsTestAndGenerateResults() {
        String expected = "Class Tested : ReflectionUtilsTest\n" +
                            "\t  Method : classNameDoesImplementInterfaceTest()\n" +
                            "\t  Result : SUCCESS\n" +
                            "\n" +
                            "\t  Method : classNameDoesNotImplementInterfaceTest()\n" +
                            "\t  Result : SUCCESS\n" +
                            "\n" +
                            "\t  Method : classObjectDoesImplementInterfaceTest()\n" +
                            "\t  Result : SUCCESS\n" +
                            "\n" +
                            "\t  Method : classObjectDoesNotImplementInterfaceTest()\n" +
                            "\t  Result : SUCCESS\n" +
                            "\n" +
                            "\t  Method : getClassHierarchyBooleanTest()\n" +
                            "\t  Result : SUCCESS\n" +
                            "\n" +
                            "\t  Method : getClassHierarchyTreeMapTest()\n" +
                            "\t  Result : SUCCESS\n" +
                            "\n" +
                            "\t  Method : instantiateClassHierarchyArrayList()\n" +
                            "\t  Result : SUCCESS\n" +
                            "\n" + // myTestRunner makes this test fail since I don't check for exceptions
                            "\t  Method : instantiateClassHierarchyBoolean()\n" +
                            "\t  Result : FAILURE\n" +
                            "\n" +
                            "\t  Method : instantiateClassHierarchyJPanel()\n" +
                            "\t  Result : SUCCESS\n" +
                            "\n" +
                            "\t  Method : instantiateClassHierarchyObjectTest()\n" +
                            "\t  Result : SUCCESS\n" +
                            "\n" +
                            "\t  Method : instantiateClassHierarchyString()\n" +
                            "\t  Result : SUCCESS\n" +
                            "\n" +
                            "\t  Method : listAllMembersBooleanTest()\n" +
                            "\t  Result : SUCCESS\n" +
                            "\n" +
                            "\t  Method : listAllMembersBufferedWriterTest()\n" +
                            "\t  Result : SUCCESS\n" +
                            "\n" +
                            "\t  Method : objectDoesImplementInterfaceTest()\n" +
                            "\t  Result : SUCCESS\n" +
                            "\n" +
                            "\t  Method : objectDoesNotImplementInterfaceTest()\n" +
                            "\t  Result : SUCCESS\n" +
                            "\n" +
                            "\t  Method : stringIsNotAClassNameButAStringLiteralTest()\n" +
                            "\t  Result : SUCCESS\n" +
                            "\n" +
                            "\t  Method : tryToCompareClassNameToNonExistentInterfaceTest()\n" +
                            "\t  Result : SUCCESS\n" +
                            "\n" +
                            "\t  Method : tryToCompareClassNameToNullInterfaceTest()\n" +
                            "\t  Result : SUCCESS\n" +
                            "\n" +
                            "\t  Method : tryToCompareClassObjectToNonExistentInterfaceTest()\n" +
                            "\t  Result : SUCCESS\n" +
                            "\n" +
                            "\t  Method : tryToCompareClassObjectToNullInterfaceTest()\n" +
                            "\t  Result : SUCCESS\n" +
                            "\n" +
                            "\t  Method : tryToCompareObjectToNonExistentInterfaceTest()\n" +
                            "\t  Result : SUCCESS\n" +
                            "\n" +
                            "\t  Method : tryToCompareObjectToNullInterfaceTest()\n" +
                            "\t  Result : SUCCESS\n" +
                            "\n" +
                            "21 Tests Passed 1 Test Failed 0 Tests Broken\n";
        String actual = unitCornTestRunner.runTests(ReflectionUtilsTest.class);

        Assert.assertEquals(expected, actual);
    }
}
