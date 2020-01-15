package com.tetrapak.customerhub.core.mock;

import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;

import javax.jcr.RepositoryException;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.Value;
import java.security.Principal;
import java.util.Collections;
import java.util.Iterator;

public class MockAuthorizable implements Authorizable {
    @Override
    public String getID() throws RepositoryException {
        return "admin";
    }

    @Override
    public boolean isGroup() {
        return false;
    }

    @Override
    public Principal getPrincipal() throws RepositoryException {
        return null;
    }

    @Override
    public Iterator<Group> declaredMemberOf() throws RepositoryException {
        return null;
    }

    @Override
    public Iterator<Group> memberOf() throws RepositoryException {
        return Collections.emptyIterator();
    }

    @Override
    public void remove() throws RepositoryException {

    }

    @Override
    public Iterator<String> getPropertyNames() throws RepositoryException {
        return null;
    }

    @Override
    public Iterator<String> getPropertyNames(String s) throws RepositoryException {
        return null;
    }

    @Override
    public boolean hasProperty(String s) throws RepositoryException {
        return false;
    }

    @Override
    public void setProperty(String s, Value value) throws RepositoryException {

    }

    @Override
    public void setProperty(String s, Value[] values) throws RepositoryException {

    }

    @Override
    public Value[] getProperty(String s) throws RepositoryException {
        return new Value[0];
    }

    @Override
    public boolean removeProperty(String s) throws RepositoryException {
        return false;
    }

    @Override
    public String getPath() throws UnsupportedRepositoryOperationException, RepositoryException {
        return "/home/users/O/O55NcpUFJE9AaGYbJrYE";
    }
}
