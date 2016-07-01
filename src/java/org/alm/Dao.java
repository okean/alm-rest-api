package org.alm;

import java.net.URI;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.alm.model.Test;
import org.alm.model.TestInstance;
import org.alm.model.TestSet;
import org.apache.commons.lang.StringUtils;

public final class Dao
{
    private Dao()
    {
    }

    /**
     * @return null if authenticated.<br> a url to authenticate against if not authenticated.
     * @throws Exception
     */
    public static String isAuthenticated() throws Exception
    {
        String isAuthenticatedUrl = "qcbin/rest/is-authenticated";

        try
        {
            connector().get(isAuthenticatedUrl, Response.class, null, null);

            return null;
        }
        catch (ResponseException ex)
        {
            Response response = ex.response();

            if (response.getStatus() != Status.UNAUTHORIZED.getStatusCode())
            {
                throw ex;
            }

            String authPoint = response.getHeaderString(HttpHeaders.WWW_AUTHENTICATE);

            if (StringUtils.isNotBlank(authPoint))
            {
                authPoint = authPoint.split("=")[1].replace("\"", "");
                authPoint += "/authenticate";

                return authPoint;
            }

            throw new Exception("Invalid authentication point");
        }
    }

    /**
     * Client sends a valid Basic Authorization header to the authentication point
     * and server set cookies on client.
     *
     * @param authenticationPoint to authenticate at
     * @param username
     * @param password
     * @throws Exception
     */
    public static void login(String authenticationPoint, String username, String password) throws Exception
    {
        connector().get(authenticationPoint, Response.class, RestConnector.createBasicAuthHeader(username, password), null);
    }

    /**
     * Make a call to is-authenticated resource to obtain authenticationPoint and do login.
     *
     * @param username
     * @param password
     * @throws Exception
     */
    public static void login(String username, String password) throws Exception
    {
        String authenticationPoint = isAuthenticated();

        if (authenticationPoint != null)
        {
            URI uri = new URI(authenticationPoint);

            login(uri.getPath(), username, password);
        }
    }

    /**
     * Close session on server and clean session cookies on client
     *
     * @throws Exception
     */
    public static void logout() throws Exception
    {
        String logoutUrl = "qcbin/authentication-point/logout";

        connector().get(logoutUrl, Response.class, null, null);
    }

    /**
     * Read the test entity with the specified ID
     *
     * @param id
     * @return
     * @throws Exception
     */
    public static Test readTest(String id) throws Exception
    {
        String testUrl = connector().buildEntityUrl("test", id);

        return connector().get(testUrl, Test.class, null, null);
    }

    /**
     * Read the test set entity with the specified ID
     *
     * @param id
     * @return
     * @throws Exception
     */
    public static TestSet readTestSet(String id) throws Exception
    {
        String testSetUrl = connector().buildEntityUrl("test-set", id);

        return connector().get(testSetUrl, TestSet.class, null, null);
    }

    /**
     * Read the test instance set entity with the specified ID
     *
     * @param id
     * @return
     * @throws Exception
     */
    public static TestInstance readTestInstance(String id) throws Exception
    {
        String testInstanceUrl = connector().buildEntityUrl("test-instance", id);

        return connector().get(testInstanceUrl, TestInstance.class, null, null);
    }

    /**
     * Gets an instance of RestConnector
     *
     * @return
     */
    private static RestConnector connector()
    {
        return RestConnector.instance();
    }
}
