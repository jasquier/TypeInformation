package squier.john.typeInformation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Created by johnsquier on 2/15/17.
 */
public class ReflectionUtils {

    public boolean classImplementsInterface(String aClassName, String anInterfaceName) {
        Class<?> theClass = getClassName(aClassName);

        if ( theClass == null ) { // more readable than try-catch?
            return classImplementsInterface((Object) aClassName, anInterfaceName);
        } else {
            return classImplementsInterface(theClass, anInterfaceName);
        }
    }

    public boolean classImplementsInterface(Class<?> theClass, String anInterface) {
        Class<?> theInterfaceClass = getClassName(anInterface);

        if ( theInterfaceClass == null ) { // same here?
            return false;
        }
        return checkClassForInterface(theClass, theInterfaceClass);
    }

    public boolean classImplementsInterface(Object o, String theInterface) {
        return classImplementsInterface(o.getClass(), theInterface);
    }

    public String listAllMembers(Object o) {
        StringBuilder sb = new StringBuilder();

        Class<?> theClass = o.getClass();
        sb.append(classInfoString(theClass));

        while ( hasASuperClass(theClass) ) {
            theClass = theClass.getSuperclass();
            sb.append(classInfoString(theClass));
        }
        return sb.toString();
    }

    public String getClassHierarchy(Object o) {
        List<Class<?>> classHierarchyInReverse = new ArrayList<>();

        Class<?> theClass = o.getClass();
        classHierarchyInReverse.add(theClass);

        while ( hasASuperClass(theClass) ) {
            theClass = theClass.getSuperclass();
            classHierarchyInReverse.add(theClass);
        }
        return generateClassHierarchyString(classHierarchyInReverse);
    }

    // 6
    public List<Object> instantiateClassHierarcy(Object o) {
        return null;
    }

    private Class<?> getClassName(String aClassName) {
        Class<?> theInterfaceClass;

        if ( aClassName == null ) {
            return null;
        }
        try {
            theInterfaceClass = Class.forName(aClassName);
        } catch (ClassNotFoundException e) {
            System.err.println("Class: " + aClassName + " not found");
            return null;
        }
        return theInterfaceClass;
    }

    private boolean checkClassForInterface(Class<?> theClass, Class<?> theInterfaceClass) {
        Class<?>[] implementedInterfaces = theClass.getInterfaces();

        for ( Class c : implementedInterfaces ) {
            if ( c.getName().equals(theInterfaceClass.getName()) ) {
                return true;
            }
        }
        return false;
    }

    private String classInfoString(Class<?> theClass) {
        StringBuilder sb = new StringBuilder();

        sb.append(fieldsInfoString(theClass));
        sb.append(constructorInfoString(theClass));
        sb.append(methodsInfoString(theClass));

        return sb.toString();
    }

    private String fieldsInfoString(Class<?> theClass) {
        StringBuilder sb = new StringBuilder();

        Field[] fields = theClass.getFields();
        sb.append("Fields\n");

        fields = sortMemberArray(fields);

        for (Field f : fields) {
            sb.append(classNameHeader(theClass));
            sb.append(modifiers(f));
            sb.append(fieldType(f));
            sb.append(fieldName(f));
        }
        return sb.toString();
    }

    private String constructorInfoString(Class<?> theClass) {
        StringBuilder sb = new StringBuilder();

        Constructor<?>[] constructors = theClass.getConstructors();
        sb.append("Constructors\n");

        // sort constructors by name?

        for (Constructor<?> c : constructors) {
            sb.append(classNameHeader(theClass));
            sb.append(modifiers(c));
            sb.append(constructorName(c));
            sb.append(params(c));
            sb.append("\n");
        }
        return sb.toString();
    }

    private String methodsInfoString(Class<?> theClass) {
        StringBuilder sb = new StringBuilder();

        Method[] methods = theClass.getMethods();
        sb.append("Methods\n");

        methods = sortMemberArray(methods);

        for (Method m : methods) {
            if ( methodIsDeclaredInThisClass(m, theClass) ) {
                sb.append(classNameHeader(theClass));
                sb.append(modifiers(m));
                sb.append(methodReturnType(m));
                sb.append(methodName(m));
                sb.append(params(m));
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    private String paramsInfoString(Class<?>[] params) {
        StringBuilder sb = new StringBuilder();

        if ( empty(params) ) {
            sb.append(")");
        } else {
            sb.append(allParams(params));
        }
        return sb.toString();
    }

    // @@@ Refactor
    private String generateClassHierarchyString(List<Class<?>> classHierarchyInReverse) {
        StringBuilder sb = new StringBuilder();

        int numSpaces = 0;
        for (int i = classHierarchyInReverse.size() - 1; i >= 0; i--) {
            for (int j = 0; j < numSpaces; j++) {
                sb.append(" ");
            }
            numSpaces += 2;
            sb.append(classHierarchyInReverse.get(i).getName());
            sb.append("\n");
        }
        return sb.toString();
    }

    private String params(Constructor c) {
        return paramsInfoString(c.getParameterTypes());
    }

    private String params(Method m) {
        return paramsInfoString(m.getParameterTypes());
    }

    private String allParams(Class<?>[] params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.length; i++) {
            sb.append(params[i].getName());

            if (iIsTheLastParam(i, params.length)) {
                sb.append(")");
            } else {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    private String classNameHeader(Class<?> theClass) {
        return theClass.getSimpleName() + " : ";
    }

    private String modifiers(Field f) {
        return Modifier.toString(f.getModifiers()) + " ";
    }

    private String modifiers(Constructor c) {
        return Modifier.toString(c.getModifiers()) + " ";
    }

    private String modifiers(Method m) {
        return Modifier.toString((m.getModifiers())) + " ";
    }

    private String fieldType(Field f) {
        return f.getType().getSimpleName() + " ";
    }

    private String methodReturnType(Method m) {
        return m.getReturnType() + " ";
    }

    private String fieldName(Field f) {
        return f.getName() + "\n";
    }

    private String constructorName(Constructor c) {
        return c.getName() + "(";
    }

    private String methodName(Method m) {
        return m.getName() + "(";
    }

    private Method[] sortMemberArray(Method[] m) {
        Arrays.sort(m, new Comparator<Method>() {
            @Override
            public int compare(Method o1, Method o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return m;
    }

    private Field[] sortMemberArray(Field[] f) {
        Arrays.sort(f, new Comparator<Field>() {
            @Override
            public int compare(Field o1, Field o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return f;
    }

    private boolean methodIsDeclaredInThisClass(Method m, Class<?> theClass) {
        return m.getDeclaringClass().getSimpleName().equals(theClass.getSimpleName());
    }

    private boolean hasASuperClass(Class<?> theClass) {
        return !theClass.getSimpleName().equals("Object");
    }

    private boolean empty(Class<?>[] params) {
        return params.length == 0;
    }

    private boolean iIsTheLastParam(int i, int n) {
        return i == n-1;
    }
}

