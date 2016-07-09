package org.alm;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.alm.model.Attachment;
import org.alm.model.TestInstance;
import org.alm.model.TestSet;
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

    @Test(groups = { "crud", "entity" })
    public void readTestEntity() throws Exception
    {
        org.alm.model.Test expected = createTestEntity("2");

        org.alm.model.Test actual = Dao.readTest("2");

        EntityAssert.assertEquals(actual, expected);
    }

    @Test(groups = { "crud", "entity" })
    public void readTestSetEntity() throws Exception
    {
        TestSet expected = createTestSetEntity("2");

        TestSet actual = Dao.readTestSet("2");

        EntityAssert.assertEquals(actual, expected);
    }

    @Test(groups = { "crud", "entity" })
    public void readTestInstanceEntity() throws Exception
    {
        TestInstance expected = createTestInstanceEntity("2");

        TestInstance actual = Dao.readTestInstance("2");

        EntityAssert.assertEquals(actual, expected);
    }

    @Test(groups = { "crud", "enity", "attachment" })
    public void createRunAttachment() throws Exception
    {
        String fileName = "test.txt";
        String fileContent = "content of test file";

        Attachment attachment = Dao.createRunAttachment("3", fileName, fileContent.getBytes());

        Assert.assertEquals(attachment.name(), fileName);
        Assert.assertEquals(attachment.fileSize(), String.valueOf(fileContent.length()));
        Assert.assertEquals(attachment.parentId(), "3");
        Assert.assertEquals(attachment.parentType(), "runs");
    }

    @Test(groups = { "crud", "enity", "attachment" })
    public void createRunStepAttachment() throws Exception
    {
        String fileName = "run-step.txt";
        String fileContent = "content of run step file";

        Attachment attachment = Dao.createRunStepAttachment("4", fileName, fileContent.getBytes());

        Assert.assertEquals(attachment.name(), fileName);
        Assert.assertEquals(attachment.fileSize(), String.valueOf(fileContent.length()));
        Assert.assertEquals(attachment.parentId(), "4");
        Assert.assertEquals(attachment.parentType(), "run-steps");
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

    private static org.alm.model.Test createTestEntity(String id)
    {
        org.alm.model.Test test = new org.alm.model.Test();

        test.execStatus("No Run");
        test.owner("admin");
        test.status("Design");
        test.subtypeId("MANUAL");
        test.parentId("100");
        test.id(id);
        test.name("CreatePITest");
        test.description("Verify Participating Individuals are properly creted");

        return test;
    }

    private static TestSet createTestSetEntity(String id)
    {
        TestSet testSet = new TestSet();

        testSet.status("Open");
        testSet.subtypeId("101F3974-AD91-49d8-97EF-B3DEC6F0AEA3");
        testSet.comment("<html>"
                + "<body>"
                + "<div align=\"left\"><font face=\"Arial\"><span style=\"font-size:8pt\">"
                + "For running tests on Mansfield Park planning tests</span></font></div>"
                + "</body>"
                + "</html>");
        testSet.linkage("N");
        testSet.id(id);
        testSet.parentId("1");
        testSet.name("Mansfield Testing");

        return testSet;
    }

    private static TestInstance createTestInstanceEntity(String id)
    {
        TestInstance testInstance = new TestInstance();

        testInstance.testSetId("12");
        testInstance.testId("1");
        testInstance.iterations("5");
        testInstance.name("1");
        testInstance.id(id);

        return testInstance;
    }

    private static Attachment createAttachment(String name, String fileSize, String parentId, String parentType)
    {
        Attachment attachment = new Attachment();

        attachment.name(name);
        attachment.fileSize(fileSize);
        attachment.parentId(parentId);
        attachment.parentType(parentType);

        return attachment;
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

        @GET
        @Path("/rest/domains/{domain}/projects/{project}/tests/{id}")
        @Produces("application/xml")
        public org.alm.model.Test test(@PathParam("domain") String domain,
                @PathParam("project") String project,
                @PathParam("id") String id)
        {
            org.alm.model.Test test = createTestEntity(id);

            return test;
        }

        @GET
        @Path("/rest/domains/{domain}/projects/{project}/test-sets/{id}")
        @Produces("application/xml")
        public TestSet testSet(@PathParam("domain") String domain,
                @PathParam("project") String project,
                @PathParam("id") String id)
        {
            TestSet testSet = createTestSetEntity(id);

            return testSet;
        }

        @GET
        @Path("/rest/domains/{domain}/projects/{project}/test-instances/{id}")
        @Produces("application/xml")
        public TestInstance testInstance(@PathParam("domain") String domain,
                @PathParam("project") String project,
                @PathParam("id") String id)
        {
            TestInstance testInstance = createTestInstanceEntity(id);

            return testInstance;
        }

        @POST
        @Path("/rest/domains/{domain}/projects/{project}/{entityCollection}/{enityId}/attachments")
        @Consumes("application/octet-stream")
        @Produces("application/xml")
        public Attachment createAttachments(
                @HeaderParam("Slug") String fileName,
                @PathParam("enityId") String enityId,
                @PathParam("entityCollection") String entityCollection,
                File file) throws Exception
        {
            Attachment attachment = createAttachment(
                    fileName, String.valueOf(file.length()), enityId, entityCollection);

            return attachment;
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
