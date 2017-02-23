package squier.john.unitcorn;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author John A. Squier
 */
public class DummyTests2 {

    @Test
    public void testThatIsTrue() {
        Assert.assertTrue(true);
    }

    @After
    public void teardown() {
        System.out.println("AFTER");
    }
}
