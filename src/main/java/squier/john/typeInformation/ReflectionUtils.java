package squier.john.typeInformation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Created by John A. Squier on 2/15/17.
 * @@@ multiple refactor spots in src
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

    // @@@ refactor
    public List<Object> instantiateClassHierarchy(Object o)
            throws ClassInHierarchyLacksNoArgConstructor, InstantiationException, IllegalAccessException {
        List<Object> theHierarchy = new ArrayList<>();

        while ( hasASuperClass(o.getClass()) || isObjectClass(o) ) {
            if ( OHasANoArgConstructor(o) ) {
                theHierarchy.add(instantiate(o));
            } else {
                throw new ClassInHierarchyLacksNoArgConstructor();
            }
            if ( !isObjectClass(o) ) {
                o = getNextConcreteClass(o);
            }
            else {
                break;
            }
        }
        return theHierarchy;
    }

    // @@@ refactor
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

        boolean result = false;
        for ( Class c : implementedInterfaces ) {
            if ( c.getName().equals(theInterfaceClass.getName()) ) {
                result = true;
            }
        }
        return result;
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

    // @@@ refactor loop body into sb.append(methodInfoString(m))
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
            sb.append(paramDelimiter(i, params));
        }
        return sb.toString();
    }

    private String paramDelimiter(int i, Class<?>[] a) {
        String result;
        if (iIsTheLastParam(i, a.length)) {
            result = ")";
        } else {
            result = ", ";
        }
        return result;
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

    private Method[] sortMemberArray(Method[] m) {
        Arrays.sort(m, new Comparator<Method>() {
            @Override
            public int compare(Method o1, Method o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return m;
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

    // @@@ refactor
    private Object getNextConcreteClass(Object o) throws IllegalAccessException, InstantiationException {
        Class<?> theSuperClass = o.getClass().getSuperclass();

        if ( hasASuperClass(o.getClass()) && isConcrete(theSuperClass) ) {
            return theSuperClass.newInstance();
        }
        else {
            while ( hasASuperClass(theSuperClass) ) {
                if ( isConcrete(theSuperClass) ) {
                    return theSuperClass.newInstance();
                }
                theSuperClass = theSuperClass.getSuperclass();
            }
        }
        return theSuperClass.newInstance();
    }

    private Object instantiate(Object o) throws IllegalAccessException, InstantiationException {
        Class<?> theClass = o.getClass();
        return theClass.newInstance();
    }

    private boolean hasASuperClass(Class<?> c) {
        return !c.getSimpleName().equals("Object");
    }

    private boolean methodIsDeclaredInThisClass(Method m, Class<?> c) {
        return m.getDeclaringClass().getSimpleName().equals(c.getSimpleName());
    }

    private boolean iIsTheLastParam(int i, int n) {
        return i == n-1;
    }

    private boolean empty(Class<?>[] a) {
        return a.length == 0;
    }

    private boolean OHasANoArgConstructor(Object o) {
        Constructor<?>[] constructors = o.getClass().getConstructors();

        for (Constructor<?> c : constructors ) {
            if ( c.getParameterTypes().length == 0 ) {
                return true;
            }
        }
        return false;
    }

    private boolean isConcrete(Class<?> c) {
        boolean result = true;
        if ( Modifier.isAbstract(c.getModifiers()) ) {
            result = false;
        }
        return result;
    }

    private boolean isObjectClass(Object o) {
        return o.getClass().getSimpleName().equals("Object");
    }
}