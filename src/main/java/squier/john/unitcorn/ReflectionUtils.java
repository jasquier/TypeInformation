package squier.john.unitcorn;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Created by John A. Squier on 2/15/17.
 * TODO multiple refactor spots in src
 */
public class ReflectionUtils {

    public static boolean classImplementsInterface(String aClassName, String anInterfaceName) {
        Class<?> classClass = getClassClassFromString(aClassName);
        Class<?> interfaceClassClass = getClassClassFromString(anInterfaceName);
        return interfaceClassClass != null && ((classClass == null) ? checkClassForInterface(aClassName.getClass(), interfaceClassClass) :
                                                                        checkClassForInterface(classClass, interfaceClassClass));
    }

    public static boolean classImplementsInterface(Class<?> theClass, String anInterface) {
        return classImplementsInterface(theClass.getName(), anInterface);
    }

    public static boolean classImplementsInterface(Object o, String theInterface) {
        return classImplementsInterface(o.getClass(), theInterface);
    }

    public static String listAllMembers(Object o) {
        Class<?> c = o.getClass();
        StringBuilder sb = new StringBuilder();
        sb.append(classInfoString(c));

        while ( hasASuperClass(c) ) {
            c = c.getSuperclass();
            sb.append(classInfoString(c));
        }
        return sb.toString();
    }

    public static String getClassHierarchy(Object o) {
        Class<?> theClass = o.getClass();
        List<Class<?>> classHierarchyInReverse = new ArrayList<>();

        classHierarchyInReverse.add(theClass);
        while ( hasASuperClass(theClass) ) {
            theClass = theClass.getSuperclass();
            classHierarchyInReverse.add(theClass);
        }
        return generateClassHierarchyString(classHierarchyInReverse);
    }

    // @@@ refactor
    public static List<Object> instantiateClassHierarchy(Object o)
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

    private static Class<?> getClassClassFromString(String aClassName) {
        try {
            return aClassName == null ? null : Class.forName(aClassName);
        } catch ( ClassNotFoundException e ) {
            return null;
        }
    }

    private static boolean checkClassForInterface(Class<?> theClass, Class<?> theInterfaceClass) {
        Class<?>[] implementedInterfaces = theClass.getInterfaces();
        for ( Class c : implementedInterfaces ) {
            if ( c.getName().equals(theInterfaceClass.getName()) ) {
                return true;
            }
        }
        return false;
    }

    private static String classInfoString(Class<?> theClass) {
        StringBuilder sb = new StringBuilder();
        sb.append(fieldsInfoString(theClass));
        sb.append(constructorInfoString(theClass));
        sb.append(methodsInfoString(theClass));
        return sb.toString();
    }

    private static String fieldsInfoString(Class<?> theClass) {
        Field[] fields = theClass.getFields();
        fields = sortMemberArray(fields);

        StringBuilder sb = new StringBuilder();
        sb.append("Fields\n");

        for (Field f : fields) {
            sb.append(generateFieldInfo(f, theClass));
        }
        return sb.toString();
    }

    private static String constructorInfoString(Class<?> theClass) {
        Constructor<?>[] constructors = theClass.getConstructors();

        StringBuilder sb = new StringBuilder();
        sb.append("Constructors\n");

        for (Constructor<?> c : constructors) {
            sb.append(generateConstructorInfo(c, theClass));
        }
        return sb.toString();
    }

    private static String methodsInfoString(Class<?> theClass) {
        Method[] methods = theClass.getMethods();
        methods = sortMemberArray(methods);

        StringBuilder sb = new StringBuilder();
        sb.append("Methods\n");

        for (Method m : methods) {
            if ( methodIsDeclaredInThisClass(m, theClass) ) {
                sb.append(generateMethodInfo(m, theClass));
            }
        }
        return sb.toString();
    }

    private static String generateFieldInfo(Field f, Class<?> theClass) {
        StringBuilder sb = new StringBuilder();
        sb.append(classNameHeader(theClass));
        sb.append(modifiers(f));
        sb.append(fieldType(f));
        sb.append(fieldName(f));
        return sb.toString();
    }

    private static String generateConstructorInfo(Constructor<?> c, Class<?> theClass) {
        StringBuilder sb = new StringBuilder();
        sb.append(classNameHeader(theClass));
        sb.append(modifiers(c));
        sb.append(constructorName(c));
        sb.append(params(c));
        sb.append("\n");
        return sb.toString();
    }

    private static String generateMethodInfo(Method m, Class<?> theClass) {
        StringBuilder sb = new StringBuilder();
        sb.append(classNameHeader(theClass));
        sb.append(modifiers(m));
        sb.append(methodReturnType(m));
        sb.append(methodName(m));
        sb.append(params(m));
        sb.append("\n");
        return sb.toString();
    }

    private static String paramsInfoString(Class<?>[] params) {
        StringBuilder sb = new StringBuilder();
        if ( empty(params) ) {
            sb.append(")");
        } else {
            sb.append(allParams(params));
        }
        return sb.toString();
    }

    private static String classNameHeader(Class<?> theClass) {
        return theClass.getSimpleName() + " : ";
    }

    private static String modifiers(Field f) {
        return Modifier.toString(f.getModifiers()) + " ";
    }

    private static String modifiers(Constructor c) {
        return Modifier.toString(c.getModifiers()) + " ";
    }

    private static String modifiers(Method m) {
        return Modifier.toString((m.getModifiers())) + " ";
    }

    private static String fieldType(Field f) {
        return f.getType().getSimpleName() + " ";
    }

    private static String methodReturnType(Method m) {
        return m.getReturnType() + " ";
    }

    private static String fieldName(Field f) {
        return f.getName() + "\n";
    }

    private static String constructorName(Constructor c) {
        return c.getName() + "(";
    }

    private static String methodName(Method m) {
        return m.getName() + "(";
    }

    private static String params(Constructor c) {
        return paramsInfoString(c.getParameterTypes());
    }

    private static String params(Method m) {
        return paramsInfoString(m.getParameterTypes());
    }

    private static String allParams(Class<?>[] params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.length; i++) {
            sb.append(params[i].getName());
            sb.append(paramDelimiter(i, params));
        }
        return sb.toString();
    }

    private static String paramDelimiter(int i, Class<?>[] a) {
        String result;
        if (iIsTheLastParam(i, a.length)) {
            result = ")";
        } else {
            result = ", ";
        }
        return result;
    }

    private static Field[] sortMemberArray(Field[] f) {
        Arrays.sort(f, Comparator.comparing(Field::getName));
        return f;
    }

    private static Method[] sortMemberArray(Method[] m) {
        Arrays.sort(m, Comparator.comparing(Method::getName));
        return m;
    }

    // @@@ Refactor
    private static String generateClassHierarchyString(List<Class<?>> classHierarchyInReverse) {
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
    private static Object getNextConcreteClass(Object o) throws IllegalAccessException, InstantiationException {
        Class<?> superClass = o.getClass().getSuperclass();

        if ( hasASuperClass(o.getClass()) && isConcrete(superClass) ) {
            return superClass.newInstance();
        }
        else {
            while ( hasASuperClass(superClass) ) {
                if ( isConcrete(superClass) ) {
                    return superClass.newInstance();
                }
                superClass = superClass.getSuperclass();
            }
        }
        return superClass.newInstance();
    }

    private static Object instantiate(Object o) throws IllegalAccessException, InstantiationException {
        Class<?> c = o.getClass();
        return c.newInstance();
    }

    private static boolean hasASuperClass(Class<?> c) {
        return !c.getSimpleName().equals("Object");
    }

    private static boolean methodIsDeclaredInThisClass(Method m, Class<?> c) {
        return m.getDeclaringClass().getSimpleName().equals(c.getSimpleName());
    }

    private static boolean iIsTheLastParam(int i, int n) {
        return i == n-1;
    }

    private static boolean empty(Class<?>[] a) {
        return a.length == 0;
    }

    private static boolean OHasANoArgConstructor(Object o) {
        Constructor<?>[] constructors = o.getClass().getConstructors();

        for (Constructor<?> c : constructors ) {
            if ( c.getParameterTypes().length == 0 ) {
                return true;
            }
        }
        return false;
    }

    private static boolean isConcrete(Class<?> c) {
        boolean result = true;
        if ( Modifier.isAbstract(c.getModifiers()) ) {
            result = false;
        }
        return result;
    }

    private static boolean isObjectClass(Object o) {
        return o.getClass().getSimpleName().equals("Object");
    }
}