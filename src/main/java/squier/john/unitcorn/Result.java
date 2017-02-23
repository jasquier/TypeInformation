package squier.john.unitcorn;

/**
 * Created by johnsquier on 2/16/17.
 */
public class Result {

    private String nameOfMethodRun;
    private TestStatus status;
    private Throwable thrownDuringMethodInvoke;

    public Result(String nameOfMethodRun, TestStatus status) {
        this(nameOfMethodRun, status, null);
    }

    public Result(String nameOfMethodRun, TestStatus status, Throwable thrownDuringMethodInvoke) {
        this.nameOfMethodRun = nameOfMethodRun;
        this.status = status;
        this.thrownDuringMethodInvoke = thrownDuringMethodInvoke;
    }

    public boolean equals(Result other) {
        return status.equals(other.status) && nameOfMethodRun.equalsIgnoreCase(other.nameOfMethodRun);
    }

    public String getNameOfMethodRun() {
        return nameOfMethodRun;
    }

    public TestStatus getStatus() {
        return status;
    }

    public Throwable getThrownDuringMethodInvoke() {
        return thrownDuringMethodInvoke;
    }
}
