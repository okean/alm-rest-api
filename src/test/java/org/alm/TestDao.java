package org.alm;

import java.io.IOException;
import java.net.URI;
import java.util.Properties;

import org.alm.model.*;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.alm.Util.*;

public class TestDao
{
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

        RestConnector.instance().init(host, port, domain, project);
    }

    @BeforeClass
    public void setUp() throws Exception
    {
        final ResourceConfig rc = new ResourceConfig(AlmApiStub.class);

        server = GrizzlyHttpServerFactory.createHttpServer(
                URI.create(String.format("http://%s:%s/", host, port)), rc);
    }

    @AfterClass
    public void tearDown() throws Exception
    {
        if (server != null)
        {
            server.shutdownNow();
        }
    }

    @Test(groups = { "authentication" })
    public void isAutheticatedUnauthorized() throws Exception
    {
        Assert.assertEquals(
                Dao.isAuthenticated(),
                AlmApiStub.authenticationPoint(host, port) + "/authenticate",
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

    @Test(groups = { "crud", "entity" })
    public void readTestInstanceEntities() throws Exception
    {
        TestInstance ti1 = createTestInstanceEntity("1");
        TestInstance ti2 = createTestInstanceEntity("2");

        TestInstances actual = Dao.readTestInstances("2");

        EntityAssert.assertEquals(actual.entities().get(0), ti1);
        EntityAssert.assertEquals(actual.entities().get(1), ti2);
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

    @Test(groups = { "crud", "enity" })
    public void createRun() throws Exception
    {
        Run expected = createRunEntity();

        Run actual = Dao.createRun(expected);

        expected.id(actual.id());

        EntityAssert.assertEquals(actual, expected);
    }

    @Test(groups = { "crud", "enity" })
    public void updateRunEntity() throws Exception
    {
        Run expected = createRunEntity();

        Run actual = Dao.updateRun(expected);

        expected.id(actual.id());

        EntityAssert.assertEquals(actual, expected);
    }

    @Test(groups = { "crud", "entity" })
    public void readTestStepsEntities() throws Exception
    {
        String runId = "122";

        RunStep rs1 = createRunStepEntity(runId, "1");
        RunStep rs2 = createRunStepEntity(runId, "2");

        RunSteps actual = Dao.readRunSteps(runId);

        EntityAssert.assertEquals(actual.entities().get(0), rs1);
        EntityAssert.assertEquals(actual.entities().get(1), rs2);
    }

    @Test(groups = { "crud", "enity" })
    public void createRunStep() throws Exception
    {
        String runId = "123";

        RunStep expected = createRunStepEntity(runId, "3");

        RunStep actual = Dao.createRunStep(expected);

        EntityAssert.assertEquals(actual, expected);
    }

    @Test(groups = { "crud", "enity" })
    public void updateRunStep() throws Exception
    {
        String runId = "123";
        String runStepId = "3";

        RunStep expected = createRunStepEntity(runId, runStepId);

        RunStep actual = Dao.updateRunStep(expected);

        expected.runId(runId);
        expected.id(runStepId);

        EntityAssert.assertEquals(actual, expected);
    }
}
