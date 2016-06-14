package org.alm;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestDao
{
    private static final String Host = "localhost";
    private static final String Port = "8181";
    private static final String Domain = "b2b";
    private static final String Project = "rx";

    private HttpServer server;

    public TestDao()
    {
        RestConnector.instance().init(Host, Port, Domain, Project);
    }

    @Path("/qcbin")
    public static class RestApiStub
    {
        private Map<Cookie, String> authData = new HashMap<Cookie, String>();

        @GET
        @Path("/rest/is-authenticated")
        public Response isAuthenticated(@CookieParam("LWSSO_COOKIE_KEY") Cookie cookie)
        {
            if (cookie != null && isCookieExists(cookie))
            {
                return Response.ok(cookie.getValue()).build();
            }
            else
            {
                return Response.status(Status.UNAUTHORIZED).
                        header("WWW-Authenticate", String.format("LWSSO realm=%s", authenticationPoint())).
                        build();
            }
        }

        private boolean isCookieExists(Cookie cookie)
        {
            synchronized(this)
            {
                return authData.containsKey(cookie);
            }
        }
    }

    @BeforeClass
    public void setUp() throws Exception
    {
        final ResourceConfig rc = new ResourceConfig(RestApiStub.class);

        server = GrizzlyHttpServerFactory.createHttpServer(
                URI.create(String.format("http://%s:%s/", Host, Port)), rc);
    }

    @AfterClass
    public void tearDown() throws Exception
    {
        server.shutdownNow();
    }

    @Test(groups = { "authentication" })
    public void isAutheticatedUnauthorized() throws Exception
    {
        Assert.assertEquals(Dao.isAuthenticated(), authenticationPoint() + "/authenticate");
    }

    private static String authenticationPoint()
    {
        return String.format("http://%s:%s/qcbin/authentication-point", Host, Port);
    }
}
