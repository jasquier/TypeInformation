package squier.john.unitcorn;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by johnsquier on 2/16/17.
 */
public class UnitCornTestRunner {

    public UnitCornTestRunner() { }

    // takes a class object, finds all methods declared in the class with the org.junit.Test anno.
    //  passes those names to runTest(class, string) and handles the results
    //  pretty prints results
    public String runTests(Class c) {
        // build up a list of test results
        return null;
    }

    public Result runTest(Class<?> c, String methodName) {
        return runMethod(c, methodName);
    }

    private boolean classHasMethod(Class<?> c, String methodName) {
        boolean classHasMethod = true;
        if ( methodName == null ) {
            classHasMethod = false;
        }
        else {
            try {
                c.getMethod(methodName);
            } catch (NoSuchMethodException e) {
                classHasMethod = false;
            }
        }
        return classHasMethod;
    }

    private Result runMethod(Class<?> c, String methodName) {
        Result result;
            try {
                Method method = c.getMethod(methodName);
                Object object = instantiateObjectFromClass(c);
                Object theResult = method.invoke(object);
                result = new Result(methodName, TestStatus.SUCCESS);
            } catch (NoSuchMethodException e) {
                result = new Result(methodName, TestStatus.NON_EXISTENT_METHOD);
            } catch (IllegalAccessException | InstantiationException e) {
                result = new Result(methodName, TestStatus.CLASS_INSTANTIATION_FAILURE);
            } catch (InvocationTargetException e) {
                // Test Failure
                // Look at e for useful information including Assert messages
                // System.out.println(e.getCause());
                result = new Result(methodName, TestStatus.FAILURE, e);
            }
        return result;
    }

    private Object instantiateObjectFromClass(Class<?> c) throws IllegalAccessException, InstantiationException {
        Object o = c.newInstance();
        return o;
    }
}
