package squier.john.unitcorn;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by johnsquier on 2/16/17.
 */
public class UnitCornTestRunner {

    public UnitCornTestRunner() { }

    // @@@ Need to sort methods to ensure invocation order
    public String runTests(Class c) {
        Method[] methodsWithTestAnnotation = getTestMethods(c);
        List<Result> testResults = generateTestResults(c, methodsWithTestAnnotation);
        return outputTestResultsToConsole(c, testResults);
    }

    public Result runTest(Class<?> c, String methodName) {
        return runMethod(c, methodName);
    }

    // @@@ refactor, too long
    private Method[] getTestMethods(Class c) {
        Method[] allMethods = c.getMethods();
        List<Method> testMethods = new ArrayList<>();

        for (Method m : allMethods) {
            Annotation[] annos = m.getDeclaredAnnotations();
            for (Annotation a : annos) {
                if ( isATestAnnotation(a) ) {
                    testMethods.add(m);
                }
            }
        }
        testMethods.sort(Comparator.comparing(Method::getName));
        return testMethods.toArray(new Method[0]);
    }

    private List<Result> generateTestResults(Class c, Method[] methodsWithTestAnnotation) {
        List<Result> results = new ArrayList<>();
        for (Method method : methodsWithTestAnnotation) {
            Result result = runTest(c, method.getName());
            results.add(result);
        }
        return results;
    }

    // @@@ Refactor
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
        return numTestsMethodError(l) + numTestsClassError(l);
    }

    private int numTestsMethodError(List<Result> l) {
        return countTestsWithStatus(l, TestStatus.NON_EXISTENT_METHOD);
    }

    private int numTestsClassError(List<Result> l) {
        return countTestsWithStatus(l, TestStatus.CLASS_INSTANTIATION_FAILURE);
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

    private Result runMethod(Class<?> c, String methodName) {
        try {
            Method method = c.getMethod(methodName);
            Object object = instantiateObjectFromClass(c);
            Object theResult = method.invoke(object);
            return new Result(methodName, TestStatus.SUCCESS);
        } catch (NoSuchMethodException e) {
            return new Result(methodName, TestStatus.NON_EXISTENT_METHOD);
        } catch (IllegalAccessException | InstantiationException e) {
            return new Result(methodName, TestStatus.CLASS_INSTANTIATION_FAILURE);
        } catch (InvocationTargetException e) {
            return new Result(methodName, TestStatus.FAILURE, e); // look at e for useful info
        }
    }

    private Object instantiateObjectFromClass(Class<?> c) throws IllegalAccessException, InstantiationException {
        return c.newInstance();
    }

    private boolean isATestAnnotation(Annotation a) {
        return a.annotationType().getSimpleName().equalsIgnoreCase("test");
    }
}
