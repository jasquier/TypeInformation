package squier.john.unitcorn;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by johnsquier on 2/16/17.
 * @@@ need to add handling for @Before and @After
 * @@@ need to reorder methods in this class for readability
 */
public class UnitCornTestRunner {

    public UnitCornTestRunner() { }

    public String runTests(Class c) {
        Method[] testMethods = getTestMethods(c);
        List<Result> testResults = generateTestResults(c, testMethods);
        return outputTestResultsToConsole(c, testResults);
    }

    public Result runTest(Class<?> c, String methodName) {
        // get method object from string
        Method method;
        try {
            method = c.getMethod(methodName);
        } catch (NoSuchMethodException e) {
            return new Result(methodName, TestStatus.NON_EXISTENT_METHOD);
        }

        if ( isATestMethod(method) ) {
            return runTestMethod(c, method);
        }
        else {
            return new Result(methodName, TestStatus.NOT_A_TEST_METHOD);
        }
    }

    private Method[] getTestMethods(Class c) {
        Method[] allMethods = c.getMethods();
        List<Method> testMethods = new ArrayList<>();

        for (Method m : allMethods) {
            if ( isATestMethod(m) ) {
                testMethods.add(m);
            }
        }
        testMethods.sort(Comparator.comparing(Method::getName));
        return testMethods.toArray(new Method[0]);
    }

    private boolean isATestMethod(Method m) {
        Annotation[] annos = m.getDeclaredAnnotations();
        for (Annotation a : annos) {
            if ( isATestAnnotation(a) ) {
                return true;
            }
        }
        return false;
    }

    private List<Result> generateTestResults(Class c, Method[] testMethods) {
        List<Result> results = new ArrayList<>();
        for (Method testMethod : testMethods) {
            // reduce to one line?? more or less readable?
            Result result = runTest(c, testMethod.getName());
            results.add(result);
        }
        return results;
    }

    // @@@ Refactor, too long
    private String outputTestResultsToConsole(Class<?> c, List<Result> testResults) {
        StringBuilder sb = new StringBuilder();
        int testsPassed = numTestsPassed(testResults);
        int testsFailed = numTestsFailed(testResults);
        int testsBroken = numTestsBroken(testResults);

        sb.append("Class Tested : ").append(c.getSimpleName()).append("\n");
        for ( Result r : testResults ) {
            sb.append("\t  Method : ").append(r.getNameOfMethodRun()).append("()\n");
            sb.append("\t  Result : ").append(r.getStatus()).append("\n\n");
        }
        sb.append(testsPassed).append(" Test").append(sOrSpace(testsPassed)).append("Passed ");
        sb.append(testsFailed).append(" Test").append(sOrSpace(testsFailed)).append("Failed ");
        sb.append(testsBroken).append(" Test").append(sOrSpace(testsBroken)).append("Broken").append("\n");
        System.out.println(sb.toString());
        return sb.toString();
    }

    private String sOrSpace(int n) {
        return n == 1 ? " " : "s ";
    }

    private int numTestsPassed(List<Result> l) {
        return countTestsWithStatus(l, TestStatus.SUCCESS);
    }

    private int numTestsFailed(List<Result> l) {
        return countTestsWithStatus(l, TestStatus.FAILURE);
    }

    private int numTestsBroken(List<Result> l) {
        return numTestsMethodError(l) + numTestsClassError(l) + numTestsBrokenTag(l);
    }

    private int numTestsMethodError(List<Result> l) {
        return countTestsWithStatus(l, TestStatus.NON_EXISTENT_METHOD);
    }

    private int numTestsClassError(List<Result> l) {
        return countTestsWithStatus(l, TestStatus.CLASS_INSTANTIATION_FAILURE);
    }

    private int numTestsBrokenTag(List<Result> l) {
        return countTestsWithStatus(l, TestStatus.BROKEN_TEST);
    }

    private int countTestsWithStatus(List<Result> l, TestStatus status) {
        int count = 0;
        for (Result r : l) {
            if ( r.getStatus().equals(status) ) {
                count++;
            }
        }
        return count;
    }

    // @@@ refactor??
    //  I use the exceptions generated during the try to determine what
    //  happens when I run the given method
    private Result runTestMethod(Class<?> c, Method method) {
        try {
            Object object = instantiateObjectFromClass(c);
            method.invoke(object);
            return new Result(method.getName(), TestStatus.SUCCESS);
        } catch (IllegalAccessException | InstantiationException e) {
            return new Result(method.getName(), TestStatus.CLASS_INSTANTIATION_FAILURE);
        } catch (InvocationTargetException e) {
            if ( e.getCause().getClass().equals(AssertionError.class) ) {
                return new Result(method.getName(), TestStatus.FAILURE, e); // look at e for useful info
            }
            else {
                return new Result(method.getName(), TestStatus.BROKEN_TEST, e);
            }
        }
    }

    private Object instantiateObjectFromClass(Class<?> c) throws IllegalAccessException, InstantiationException {
        return c.newInstance();
    }

    private boolean isATestAnnotation(Annotation a) {
        return a.annotationType().getSimpleName().equalsIgnoreCase("test");
    }
}
