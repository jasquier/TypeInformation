package squier.john.unitcorn;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by johnsquier on 2/17/17.
 */
public class DummyTests {

    @Test
    public void testThatPasses() {
        Assert.assertTrue(true);
    }

    @Test
    public void testThatFails() {
        Assert.assertTrue("This test is designed to fail as part of the UnitCornTestRunnerTest class",
                false);
    }
}
