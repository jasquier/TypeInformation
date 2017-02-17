package squier.john.unitcorn;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
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
        Result expected = new Result(TestStatus.SUCCESS);

        Result actual = unitCornTestRunner.runTest(ReflectionUtilsTest.class, "objectDoesImplementInterfaceTest");

        Assert.assertTrue(expected.equals(actual));
    }
}
