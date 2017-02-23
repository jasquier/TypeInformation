package squier.john.unitcorn;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by johnsquier on 2/16/17.
 * @@@ need to reorder methods in this class for readability
 */
public class UnitCornTestRunner {

    public UnitCornTestRunner() { }

    public String runTests(Class c) {
        Method before = null, after = null;
        if ( hasABeforeMethod(c) ) {
            before = getBeforeMethod(c);
        }
        if ( hasAnAfterMethod(c) ) {
            after = getAfterMethod(c);
        }
        Method[] testMethods = getTestMethods(c);
        List<Result> testResults = generateTestResults(c, testMethods, before, after);
        return outputTestResultsToConsole(c, testResults);
    }

    private boolean hasAnAfterMethod(Class c) {
        return hasMethodWithAnnotation(c, "after");
    }

    private boolean hasABeforeMethod(Class c) {
        return hasMethodWithAnnotation(c, "before");
    }

    private boolean hasMethodWithAnnotation(Class<?> c, String annotation) {
        Method[] allMethods = c.getMethods();
        for ( Method m : allMethods ) {
            if ( methodHasAnno(m, annotation) ) {
                return true;
            }
        }
        return false;
    }

    private Method getBeforeMethod(Class<?> c) {
        return getMethodWithAnnotation(c, "before");
    }

    private Method getAfterMethod(Class<?> c) {
        return getMethodWithAnnotation(c, "after");
    }

    private Method[] getTestMethods(Class c) {
        return getMethodsWithAnnotation(c, "Test");
    }

    private Method[] getMethodsWithAnnotation(Class<?> c, String annotation) {
        Method[] allMethods = c.getMethods();
        List<Method> methodsWithAnno = new ArrayList<>();

        for ( Method m : allMethods ) {
            if ( methodHasAnno(m, annotation) ) {
                methodsWithAnno.add(m);
            }
        }
        methodsWithAnno.sort(Comparator.comparing(Method::getName));
        return methodsWithAnno.toArray(new Method[0]);
    }

    private Method getMethodWithAnnotation(Class<?> c, String annotation) {
        Method[] allMethods = c.getMethods();
        for (Method m : allMethods) {
            if ( methodHasAnno(m, annotation) ) {
                return m;
            }
        }
        return null; // should use a special case method object
    }

    private boolean methodHasAnno(Method m, String annotation) {
        Annotation[] annos = m.getDeclaredAnnotations();
        for (Annotation a : annos) {
            if ( a.annotationType().getSimpleName().equalsIgnoreCase(annotation) ) {
                return true;
            }
        }
        return false;
    }

    private List<Result> generateTestResults(Class c, Method[] testMethods, Method before, Method after) {
        List<Result> results = new ArrayList<>();
        Result result;
        for (Method testMethod : testMethods) {
            if ( before != null && after == null ) {
                result = runTestMethodWithBefore(c, testMethod, before);
            }
            else if ( before == null && after != null ) {
                result = runTestMethodWithAfter(c, testMethod, after);
            }
            else if ( before != null && after != null ) {
                result = runTestMethod(c, testMethod, before, after);
            }
            else {
                result = runTest(c, testMethod.getName());
            }
            results.add(result);
        }
        return results;
    }

    public Result runTest(Class<?> c, String methodName) {
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

    private boolean isATestMethod(Method method) {
        return methodHasAnno(method, "test");
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
    // pass two nulls to runTestMethod
    private Result runTestMethod(Class<?> c, Method method) {
        return runTestMethod(c, method, null, null);
    }

    private Result runTestMethodWithBefore(Class<?> c, Method testMethod, Method beforeMethod) {
        return runTestMethod(c, testMethod, beforeMethod, null);
    }

    private Result runTestMethodWithAfter(Class<?> c, Method testMethod, Method afterMethod) {
        return runTestMethod(c, testMethod, null, afterMethod);
    }

    private Result runTestMethod(Class<?> c, Method testMethod, Method beforeMethod, Method afterMethod) {
        try {
            Object object = instantiateObjectFromClass(c);
            if ( beforeMethod != null ) {
                beforeMethod.invoke(object);
            }
            testMethod.invoke(object);
            if ( afterMethod != null ) {
                afterMethod.invoke(object);
            }
            return new Result(testMethod.getName(), TestStatus.SUCCESS);
        } catch (IllegalAccessException | InstantiationException e) {
            return new Result(testMethod.getName(), TestStatus.CLASS_INSTANTIATION_FAILURE);
        } catch (InvocationTargetException e) {
            if ( e.getCause().getClass().equals(AssertionError.class) ) {
                return new Result(testMethod.getName(), TestStatus.FAILURE, e); // look at e for useful info
            }
            else {
                return new Result(testMethod.getName(), TestStatus.BROKEN_TEST, e);
            }
        }
    }


    private Object instantiateObjectFromClass(Class<?> c) throws IllegalAccessException, InstantiationException {
        return c.newInstance();
    }
}
