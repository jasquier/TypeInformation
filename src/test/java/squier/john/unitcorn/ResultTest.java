package squier.john.unitcorn;

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
        failure = new Result("", TestStatus.FAILURE);
    }

    @Test
    public void resultsEqualTest() {
        boolean expected = true;
        boolean actual = success1.equals(success2);
    }

    @Test
    public void resultsNoTEqualTest() {
        boolean expected = true;
        boolean actual = success1.equals(failure);
    }
}
