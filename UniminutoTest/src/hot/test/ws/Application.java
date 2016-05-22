package test.ws;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Cristian on 21/05/2016.
 */
public class Application extends javax.ws.rs.core.Application
{
    private Set<Class<?>> classes = new HashSet<Class<?>>();

    public Application() {
        classes.add(RESTSaludoImpl.class);
    }

    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }

    @Override
    public Set<Object> getSingletons() {
        return Collections.EMPTY_SET;
    }
}
