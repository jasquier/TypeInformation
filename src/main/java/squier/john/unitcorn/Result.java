package squier.john.unitcorn;

/**
 * Created by johnsquier on 2/16/17.
 */
public class Result {

    private TestStatus status;

    public Result(TestStatus status) {
        this.status = status;
    }

    public TestStatus getStatus() {
        return status;
    }

    public boolean equals(Result other) {
        return status.equals(other.status);
    }
}
