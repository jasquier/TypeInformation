package squier.john.typeInformation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Created by johnsquier on 2/15/17.
 * @@@ Refactor getClassHierarchy(Object)
 */
public class Reflector {

    public boolean classImplementsInterface(String theClassName, String theInterface) {
        try {
            Class<?> theClass = Class.forName(theClassName);
            return classImplementsInterface(theClass, theInterface);
        }
        catch ( ClassNotFoundException e ) {
            return classImplementsInterface((Object)theClassName, theInterface);
        }
    }

    public boolean classImplementsInterface(Class<?> theClass, String theInterface) {
        Class<?>[] implementedInterfaces = theClass.getInterfaces();
        Class<?> theInterfaceClass = getClassFromInterface(theInterface);

        // uncle bob says no null's but I think it makes the code more readable than a try catch
        if ( theInterfaceClass == null ) {
            return false;
        }

        for ( Class c : implementedInterfaces ) {
            if ( c.getName().equals(theInterfaceClass.getName()) ) {
                return true;
            }
        }

        return false;
    }

    public boolean classImplementsInterface(Object o, String theInterface) {
        return classImplementsInterface(o.getClass(), theInterface);
    }

    public String listAllMembers(Object o) {
        StringBuilder sb = new StringBuilder();

        Class<?> theClass = o.getClass();

        sb.append(generateClassInfoString(theClass));

        while ( !theClass.getSimpleName().equals("Object") ) {
            theClass = theClass.getSuperclass();
            sb.append(generateClassInfoString(theClass));
        }

        return sb.toString();
    }

    // @@@ refactor
    public String getClassHierarchy(Object o) {
        List<Class<?>> classHierarchyInReverse = new ArrayList<>();
        Class<?> theClass = o.getClass();

        classHierarchyInReverse.add(theClass);

        while( !theClass.getSimpleName().equals("Object") ) {
            theClass = theClass.getSuperclass();
            classHierarchyInReverse.add(theClass);
        }

        StringBuilder sb = new StringBuilder();
        int numSpaces = 0;

        for ( int i = classHierarchyInReverse.size()-1; i >= 0; i-- ) {
            for ( int j = 0; j < numSpaces; j++ ) {
                sb.append(" ");
            }
            numSpaces += 2;
            sb.append(classHierarchyInReverse.get(i).getName());
            sb.append("\n");
        }

        return sb.toString();
    }

    public List<Object> instantiateClassHierarcy(Object o) {
        return null;
    }

    private Class<?> getClassFromInterface(String theInterface) {
        Class<?> theInterfaceClass;
        try {
            theInterfaceClass = Class.forName(theInterface);
        }
        catch ( ClassNotFoundException e ) {
            System.err.println("Interface: " + theInterface + " not found");
            return null;
        }
        return theInterfaceClass;
    }

    private String generateClassInfoString(Class<?> theClass) {
        StringBuilder sb = new StringBuilder();

        sb.append(generateFieldsInfoString(theClass));
        sb.append(generateConstructorInfoString(theClass));
        sb.append(generateMethodsInfoString(theClass));

        return sb.toString();
    }

    private String generateFieldsInfoString(Class<?> theClass) {
        StringBuilder sb = new StringBuilder();

        Field[] fields = theClass.getFields();

        sb.append("Fields\n");

        for ( Field f : fields ) {
            sb.append(theClass.getSimpleName()).append(" : ");
            sb.append(Modifier.toString(f.getModifiers())).append(" ");
            sb.append(f.getType().getSimpleName()).append(" ");
            sb.append(f.getName()).append("\n");
        }

        return sb.toString();
    }

    private String generateConstructorInfoString(Class<?> theClass) {
        StringBuilder sb = new StringBuilder();

        Constructor<?>[] constructors = theClass.getConstructors();

        sb.append("Constructors\n");

        for ( Constructor<?> c : constructors ) {
            sb.append(theClass.getSimpleName()).append(" : ");
            sb.append(Modifier.toString(c.getModifiers())).append(" ");
            sb.append(c.getName()).append("(");

            Class<?>[] params = c.getParameterTypes();
            sb.append(generateParamsInfoString(params));

            sb.append("\n");
        }

        return sb.toString();
    }

    private String generateMethodsInfoString(Class<?> theClass) {
        StringBuilder sb = new StringBuilder();

        Method[] methods = theClass.getMethods();

        Arrays.sort(methods, new Comparator<Method>() {
            @Override
            public int compare(Method o1, Method o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        sb.append("Methods\n");
        for ( Method m : methods ) {
            if ( m.getDeclaringClass().getSimpleName().equals(theClass.getSimpleName()) ) {
                sb.append(theClass.getSimpleName()).append(" : ");
                sb.append(Modifier.toString(m.getModifiers())).append(" ");
                sb.append(m.getReturnType()).append(" ");
                sb.append(m.getName()).append("(");

                Class<?>[] params = m.getParameterTypes();
                sb.append(generateParamsInfoString(params));

                sb.append("\n");
            }
        }

        return sb.toString();
    }

    private String generateParamsInfoString(Class<?>[] params) {
        StringBuilder sb = new StringBuilder();

        if ( params.length == 0 ) {
            sb.append(")");
        }
        else {
            for ( int i = 0; i < params.length; i++ ) {
                sb.append(params[i].getName());
                if (i < params.length - 1) {
                    sb.append(", ");
                } else {
                    sb.append(")");
                }
            }
        }

        return sb.toString();
    }
}
