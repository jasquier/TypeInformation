package squier.john.unitcorn;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

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
        return null;
    }

    // runs a testMethod in a class and returns a result;
    public Result runTest(Class<?> c, String methodName) {
        Result result;
        if ( classHasMethod(c, methodName) ) {
            result = runMethod(c, methodName);
        }
        else {
            result = new Result(TestStatus.NON_EXISTENT_METHOD);
        }
        return result;
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

    // gotta catch errors thrown by Junit test methods
    private Result runMethod(Class<?> c, String methodName) {
        Method method = null;
        try {
            method = c.getMethod(methodName);
        } catch (NoSuchMethodException e) {
            e.printStackTrace(); // should never happen
        }

        Result result = null;

        Object ofClassC = null;
        try {
            ofClassC = instantiateObjectFromClass(c);
        } catch (IllegalAccessException | InstantiationException e) {
            result = new Result(TestStatus.CLASS_INSTANTIATION_FAILURE);
        }

        if ( method != null && ofClassC != null ) {
            try {
                method.invoke(ofClassC);
                result = new Result(TestStatus.SUCCESS);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // Test Failure
                // Look at e for useful information including Assert messages
                // System.out.println(e.getCause());
            }
        }
        return result;
    }

    private Object instantiateObjectFromClass(Class<?> c) throws IllegalAccessException, InstantiationException {
        Object o = c.newInstance();
        return o;
    }
}
