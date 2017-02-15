package squier.john.typeInformation;

/**
 * Created by johnsquier on 2/15/17.
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

    // classImplementsInterface(String, String) && classImplementsInterface(Object, String)
    //      call this method.
    public boolean classImplementsInterface(Class<?> theClass, String theInterface) {
        Class<?>[] implementedInterfaces = theClass.getInterfaces();
        Class<?> theInterfaceClass;

        try {
            theInterfaceClass = Class.forName(theInterface);
        }
        catch ( ClassNotFoundException e ) {
            System.err.println("Interface: " + theInterface + " not found");
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
}
