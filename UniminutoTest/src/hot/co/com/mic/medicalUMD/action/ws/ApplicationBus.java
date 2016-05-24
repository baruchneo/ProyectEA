package co.com.mic.medicalUMD.action.ws;

import test.ws.RESTSaludoImpl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ApplicationBus extends javax.ws.rs.core.Application
{
    private Set<Class<?>> classes = new HashSet<Class<?>>();

    public ApplicationBus() {
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
