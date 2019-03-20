package com.tetrapak.customerhub.core.mock;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import javax.jcr.*;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.retention.RetentionManager;
import javax.jcr.security.AccessControlManager;
import javax.jcr.version.VersionException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.AccessControlException;

public class MockSessionImpl implements Session {


    @Override
    public Repository getRepository() {
        return null;
    }

    @Override
    public String getUserID() {
        return "admin";
    }

    @Override
    public String[] getAttributeNames() {
        return new String[0];
    }

    @Override
    public Object getAttribute(String name) {
        return null;
    }

    @Override
    public Workspace getWorkspace() {
        return null;
    }

    @Override
    public Node getRootNode() throws RepositoryException {
        return null;
    }

    @Override
    public Session impersonate(Credentials credentials) throws LoginException, RepositoryException {
        return null;
    }

    @Override
    public Node getNodeByUUID(String uuid) throws ItemNotFoundException, RepositoryException {
        return null;
    }

    @Override
    public Node getNodeByIdentifier(String id) throws ItemNotFoundException, RepositoryException {
        return null;
    }

    @Override
    public Item getItem(String absPath) throws PathNotFoundException, RepositoryException {
        return null;
    }

    @Override
    public Node getNode(String absPath) throws PathNotFoundException, RepositoryException {
        return null;
    }

    @Override
    public Property getProperty(String absPath) throws PathNotFoundException, RepositoryException {
        return null;
    }

    @Override
    public boolean itemExists(String absPath) throws RepositoryException {
        return false;
    }

    @Override
    public boolean nodeExists(String absPath) throws RepositoryException {
        return false;
    }

    @Override
    public boolean propertyExists(String absPath) throws RepositoryException {
        return false;
    }

    @Override
    public void move(String srcAbsPath, String destAbsPath) throws ItemExistsException, PathNotFoundException, VersionException, ConstraintViolationException, LockException, RepositoryException {

    }

    @Override
    public void removeItem(String absPath) throws VersionException, LockException, ConstraintViolationException, AccessDeniedException, RepositoryException {

    }

    @Override
    public void save() throws AccessDeniedException, ItemExistsException, ReferentialIntegrityException, ConstraintViolationException, InvalidItemStateException, VersionException, LockException, NoSuchNodeTypeException, RepositoryException {

    }

    @Override
    public void refresh(boolean keepChanges) throws RepositoryException {

    }

    @Override
    public boolean hasPendingChanges() throws RepositoryException {
        return false;
    }

    @Override
    public ValueFactory getValueFactory() throws UnsupportedRepositoryOperationException, RepositoryException {
        return null;
    }

    @Override
    public boolean hasPermission(String absPath, String actions) throws RepositoryException {
        return false;
    }

    @Override
    public void checkPermission(String absPath, String actions) throws AccessControlException, RepositoryException {

    }

    @Override
    public boolean hasCapability(String methodName, Object target, Object[] arguments) throws RepositoryException {
        return false;
    }

    @Override
    public ContentHandler getImportContentHandler(String parentAbsPath, int uuidBehavior) throws PathNotFoundException, ConstraintViolationException, VersionException, LockException, RepositoryException {
        return null;
    }

    @Override
    public void importXML(String parentAbsPath, InputStream in, int uuidBehavior) throws IOException, PathNotFoundException, ItemExistsException, ConstraintViolationException, VersionException, InvalidSerializedDataException, LockException, RepositoryException {

    }

    @Override
    public void exportSystemView(String absPath, ContentHandler contentHandler, boolean skipBinary, boolean noRecurse) throws PathNotFoundException, SAXException, RepositoryException {

    }

    @Override
    public void exportSystemView(String absPath, OutputStream out, boolean skipBinary, boolean noRecurse) throws IOException, PathNotFoundException, RepositoryException {

    }

    @Override
    public void exportDocumentView(String absPath, ContentHandler contentHandler, boolean skipBinary, boolean noRecurse) throws PathNotFoundException, SAXException, RepositoryException {

    }

    @Override
    public void exportDocumentView(String absPath, OutputStream out, boolean skipBinary, boolean noRecurse) throws IOException, PathNotFoundException, RepositoryException {

    }

    @Override
    public void setNamespacePrefix(String prefix, String uri) throws NamespaceException, RepositoryException {

    }

    @Override
    public String[] getNamespacePrefixes() throws RepositoryException {
        return new String[0];
    }

    @Override
    public String getNamespaceURI(String prefix) throws NamespaceException, RepositoryException {
        return null;
    }

    @Override
    public String getNamespacePrefix(String uri) throws NamespaceException, RepositoryException {
        return null;
    }

    @Override
    public void logout() {

    }

    @Override
    public boolean isLive() {
        return false;
    }

    @Override
    public void addLockToken(String lt) {

    }

    @Override
    public String[] getLockTokens() {
        return new String[0];
    }

    @Override
    public void removeLockToken(String lt) {

    }

    @Override
    public AccessControlManager getAccessControlManager() throws UnsupportedRepositoryOperationException, RepositoryException {
        return null;
    }

    @Override
    public RetentionManager getRetentionManager() throws UnsupportedRepositoryOperationException, RepositoryException {
        return null;
    }
}
