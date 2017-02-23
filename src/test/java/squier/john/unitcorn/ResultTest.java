package squier.john.unitcorn;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by johnsquier on 2/17/17.
 */
public class ResultTest {

    Result success1, success2, failure;

    @Before
    public void setup() {
        success1 = new Result("", TestStatus.SUCCESS);
        success2 = new Result("", TestStatus.SUCCESS);
        failure = new Result("", TestStatus.FAILURE, new AssertionError());
    }

    @Test
    public void getThrownDuringMethodInvokeTest() {
        Class<?> expected = AssertionError.class;
        Class<?> actual = failure.getThrownDuringMethodInvoke().getClass();

        Assert.assertTrue(expected.equals(actual));
    }

    @Test
    public void resultsEqualTest() {
        boolean expected = true;
        boolean actual = success1.equals(success2);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void resultsNotEqualTest() {
        boolean expected = false;
        boolean actual = success1.equals(failure);

        Assert.assertEquals(expected, actual);
    }
}
