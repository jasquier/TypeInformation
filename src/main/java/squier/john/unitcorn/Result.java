package squier.john.unitcorn;

/**
 * Created by johnsquier on 2/16/17.
 */
public class Result {

    private String methodRun;
    private TestStatus status;
    private Throwable thrownDuringInvoke = null;

    public Result(String methodRun, TestStatus status) {
        this.methodRun = methodRun;
        this.status = status;
    }

    public Result(String methodRun, TestStatus status, Throwable thrownDuringInvoke) {
        this.methodRun = methodRun;
        this.status = status;
        this.thrownDuringInvoke = thrownDuringInvoke;
    }

    public TestStatus getStatus() {
        return status;
    }

    public boolean equals(Result other) {
        return status.equals(other.status) && methodRun.equalsIgnoreCase(other.methodRun);
    }
}
