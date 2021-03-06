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

        System.out.println(actual.getStatus());

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
    public void runBrokenTestFromDummyTests() {
        Result expected = new Result("testThatIsBroken", TestStatus.BROKEN_TEST);
        Result actual = unitCornTestRunner.runTest(DummyTests.class, "testThatIsBroken");

        Assert.assertTrue(expected.equals(actual));
    }

    @Test
    public void runMethodThatIsntATestFromDummyTests() {
        Result expected = new Result("methodThatIsntTaggedWithTest", TestStatus.NOT_A_TEST_METHOD);
        Result actual = unitCornTestRunner.runTest(DummyTests.class, "methodThatIsntTaggedWithTest");

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
        class PrivateConstructor{ private PrivateConstructor(){} @Test public void aMethod(){} }

        Result expected = new Result("aMethod", TestStatus.CLASS_INSTANTIATION_FAILURE);
        Result actual = unitCornTestRunner.runTest(PrivateConstructor.class, "aMethod");

        Assert.assertTrue(expected.equals(actual));
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
                            "\t  Result : BROKEN_TEST\n" +
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
                            "21 Tests Passed 0 Tests Failed 1 Test Broken\n";
        String actual = unitCornTestRunner.runTests(ReflectionUtilsTest.class);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void runTestsInResultTestAndGenerateResults() {
        String expected = "Class Tested : ResultTest\n" +
                            "\t  Method : getThrownDuringMethodInvokeTest()\n" +
                            "\t  Result : SUCCESS\n" +
                            "\n" +
                            "\t  Method : resultsEqualTest()\n" +
                            "\t  Result : SUCCESS\n" +
                            "\n" +
                            "\t  Method : resultsNotEqualTest()\n" +
                            "\t  Result : SUCCESS\n" +
                            "\n" +
                            "3 Tests Passed 0 Tests Failed 0 Tests Broken\n";
        String actual = unitCornTestRunner.runTests(ResultTest.class);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void runTestsInClassDummyTestsAndGenerateResults() {
        String expected = "Class Tested : DummyTests\n" +
                "\t  Method : testThatFails()\n" +
                "\t  Result : FAILURE\n" +
                "\n" +
                "\t  Method : testThatIsBroken()\n" +
                "\t  Result : BROKEN_TEST\n" +
                "\n" +
                "\t  Method : testThatPasses()\n" +
                "\t  Result : SUCCESS\n" +
                "\n" +
                "1 Test Passed 1 Test Failed 1 Test Broken\n";
        String actual = unitCornTestRunner.runTests(DummyTests.class);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void runTestsInDummyTests2AndGenerateResults() {
        String expected = "Class Tested : DummyTests2\n" +
                            "\t  Method : testThatIsTrue()\n" +
                            "\t  Result : SUCCESS\n" +
                            "\n" +
                            "1 Test Passed 0 Tests Failed 0 Tests Broken\n";
        String actual = unitCornTestRunner.runTests(DummyTests2.class);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void runTestsInDummyTests3AndGenerateResults() {
        String expected = "Class Tested : DummyTests3\n" +
                "\t  Method : testThatIsTrue()\n" +
                "\t  Result : SUCCESS\n" +
                "\n" +
                "1 Test Passed 0 Tests Failed 0 Tests Broken\n";
        String actual = unitCornTestRunner.runTests(DummyTests3.class);

        Assert.assertEquals(expected, actual);
    }
}
