package squier.john.unitcorn;

import org.junit.Before;

/**
 * Created by johnsquier on 2/17/17.
 */
public class TestStatusTest {

    TestStatus success, failure, nonexistant;

    @Before
    public void setup() {
        success = TestStatus.SUCCESS;
        failure = TestStatus.FAILURE;
        nonexistant = TestStatus.NON_EXISTENT_METHOD;
    }
}
