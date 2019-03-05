package com.tetrapak.customerhub.core.mock;

import java.security.Principal;
import java.util.Iterator;

import javax.jcr.RepositoryException;
import javax.jcr.UnsupportedRepositoryOperationException;

import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.AuthorizableExistsException;
import org.apache.jackrabbit.api.security.user.AuthorizableTypeException;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.Query;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;

public class MockUserManager implements UserManager {

	@Override
	public void autoSave(boolean arg0) throws UnsupportedRepositoryOperationException, RepositoryException {
		// TODO Auto-generated method stub

	}

	@Override
	public Group createGroup(String arg0) throws AuthorizableExistsException, RepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Group createGroup(Principal arg0) throws AuthorizableExistsException, RepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Group createGroup(Principal arg0, String arg1) throws AuthorizableExistsException, RepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Group createGroup(String arg0, Principal arg1, String arg2)
			throws AuthorizableExistsException, RepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User createSystemUser(String arg0, String arg1) throws AuthorizableExistsException, RepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User createUser(String arg0, String arg1) throws AuthorizableExistsException, RepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User createUser(String arg0, String arg1, Principal arg2, String arg3)
			throws AuthorizableExistsException, RepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Authorizable> findAuthorizables(Query arg0) throws RepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Authorizable> findAuthorizables(String arg0, String arg1) throws RepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Authorizable> findAuthorizables(String arg0, String arg1, int arg2) throws RepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Authorizable getAuthorizable(String arg0) throws RepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Authorizable getAuthorizable(Principal arg0) throws RepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Authorizable> T getAuthorizable(String arg0, Class<T> arg1)
			throws AuthorizableTypeException, RepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Authorizable getAuthorizableByPath(String arg0)
			throws UnsupportedRepositoryOperationException, RepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAutoSave() {
		// TODO Auto-generated method stub
		return false;
	}

}
