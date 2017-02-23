package squier.john.unitcorn;

import org.junit.*;

/**
* @author John A. Squier
 */
public class DummyTests {

    @Before
    public void setup() {
        System.out.println("BEFORE");
    }

    @Test
    public void testThatPasses() {
        Assert.assertTrue(true);
    }

    @Test
    @Ignore // test is only for use in UnitCornTestRunnerTest
    public void testThatFails() {
        //@SuppressWarnings()
        Assert.assertTrue("This test is designed to fail as part of the UnitCornTestRunnerTest class",
                false);
    }

    @Test
    @Ignore // test is only for use in UnitCornTestRunnerTest
    public void testThatIsBroken() {
        int[] a = new int[1];
        int x = a[10]; // out of bounds
    }

    public void methodThatIsntTaggedWithTest() {

    }

    @After
    public void teardown() {
        System.out.println("AFTER");
    }
}
