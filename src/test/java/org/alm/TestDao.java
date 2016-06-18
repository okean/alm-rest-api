package org.alm;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestDao
{
    private final boolean useStub;

    private final String host;
    private final String port;
    private final String domain;
    private final String project;

    private HttpServer server;

    public TestDao() throws IOException
    {
        Properties almProperty = readAlmProperties();

        host = almProperty.getProperty("host");
        port = almProperty.getProperty("port");
        domain = almProperty.getProperty("domain");
        project = almProperty.getProperty("project");

        useStub = Boolean.parseBoolean(almProperty.getProperty("use-stub"));

        RestConnector.instance().init(host, port, domain, project);
    }

    @BeforeClass
    public void setUp() throws Exception
    {
        if (useStub)
        {
            final ResourceConfig rc = new ResourceConfig(RestApiStub.class);

            server = GrizzlyHttpServerFactory.createHttpServer(
                    URI.create(String.format("http://%s:%s/", host, port)), rc);
        }
    }

    @AfterClass
    public void tearDown() throws Exception
    {
        if (useStub && server != null)
        {
            server.shutdownNow();
        }
    }

    @Test(groups = { "authentication" })
    public void isAutheticatedUnauthorized() throws Exception
    {
        Assert.assertEquals(
                Dao.isAuthenticated(),
                authenticationPoint(host, port) + "/authenticate",
                "Server should refuses request and returns a reference to authentication point");
    }

    @Test(groups = { "authentication" })
    public void login() throws Exception
    {
        Dao.login("admin", "admin");

        Assert.assertNull(Dao.isAuthenticated(), "Should return null if authenticated");
    }

    @Test(groups = { "authentication" })
    public void logout() throws Exception
    {
        Dao.logout();

        Assert.assertNotNull(Dao.isAuthenticated(), "Should return a url if not authenticated");
    }

    private static String authenticationPoint(String host, String port)
    {
        return String.format("http://%s:%s/qcbin/authentication-point", host, port);
    }

    private static Properties readAlmProperties() throws IOException
    {
        Properties almProperties = new Properties();
        InputStream in = TestDao.class.getResourceAsStream("/alm.properties");
        almProperties.load(in);

        return almProperties;
    }

    @Path("/qcbin")
    public static class RestApiStub
    {
        private static List<String> cookies = new ArrayList<String>();

        @GET
        @Path("/rest/is-authenticated")
        public Response isAuthenticated(
                @CookieParam("LWSSO_COOKIE_KEY") Cookie cookie,
                @Context UriInfo uriInfo)
        {
            if (cookie != null && cookieExists(cookie))
            {
                return Response.ok().build();
            }
            else
            {
                return unauthorizedResponse(uriInfo);
            }
        }

        @GET
        @Path("/authentication-point/authenticate")
        public Response login(
                @HeaderParam(HttpHeaders.AUTHORIZATION) String authorization,
                @Context UriInfo uriInfo)
        {
            if (authorization.contains("Basic"))
            {
                NewCookie cookie = new NewCookie("LWSSO_COOKIE_KEY", UUID.randomUUID().toString());

                updateCookieHolder(cookie);

                return Response.ok().cookie(cookie).build();
            }
            else
            {
                return unauthorizedResponse(uriInfo);
            }
        }

        @GET
        @Path("/authentication-point/logout")
        public Response logout(@CookieParam("LWSSO_COOKIE_KEY") Cookie cookie)
        {
            removeCookie(cookie);

            // The server removes the LWSSOtoken from the client's active cookies.
            String cookieStr = String.format("%s=deleted;Expires=Thu, 01-Jan-1970 00:00:01 GMT", cookie.getName());

            return Response.ok().header("Set-Cookie", cookieStr).build();
        }

        private static Response unauthorizedResponse(UriInfo uriInfo)
        {
            URI baseUri = uriInfo.getBaseUri();

            String authenticateHeader = String.format(
                    "LWSSO realm=%s", authenticationPoint(baseUri.getHost(), String.valueOf(baseUri.getPort())));

            return Response.status(Status.UNAUTHORIZED).header("WWW-Authenticate", authenticateHeader).build();
        }

        private void updateCookieHolder(NewCookie cookie)
        {
            synchronized(this)
            {
                cookies.add(cookie.getValue());
            }
        }

        private void removeCookie(Cookie cookie)
        {
            synchronized(this)
            {
                if (cookies.contains(cookie.getValue()))
                {
                    cookies.remove(cookie.getValue());
                }
            }
        }

        private boolean cookieExists(Cookie cookie)
        {
            synchronized(this)
            {
                return cookies.contains(cookie.getValue());
            }
        }
    }
}
