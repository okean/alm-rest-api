package org.alm;

import static org.alm.Util.readAlmProperties;

import java.net.URI;

import junit.framework.Assert;

import org.alm.model.*;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestClient
{
    private HttpServer server;

    private Config config;
    private Client client;
    private TestSet testSet;
    private TestInstances testInstances;
    private org.alm.model.Test test;
    private Run run;

    public TestClient() throws Exception
    {
        config = new Config(readAlmProperties());
        client = new Client(config);
    }

    @BeforeClass
    public void setUp() throws Exception
    {

        final ResourceConfig rc = new ResourceConfig(AlmApiStub.class);

        server = GrizzlyHttpServerFactory.createHttpServer(
                URI.create(String.format("http://%s:%s/", config.host(), config.port())), rc);
    }

    @AfterClass
    public void tearDown() throws Exception
    {
        if (server != null)
        {
            server.shutdownNow();
        }
    }

    @Test
    public void login() throws Exception
    {
        client.login();
    }

    @Test(dependsOnMethods = { "login" })
    public void loadTestSet() throws Exception
    {
        testSet = client.loadTestSet("1");

        Assert.assertNotNull(testSet);
    }

    @Test(dependsOnMethods = { "loadTestSet" })
    public void loadTestInstances() throws Exception
    {
        testInstances = client.loadTestInstances(testSet);

        Assert.assertNotNull(testInstances);
    }

    @Test(dependsOnMethods = { "loadTestInstances" })
    public void loadTest() throws Exception
    {
        test = client.loadTest(testInstances.entities().get(0));

        Assert.assertNotNull(test);
    }

    @Test(dependsOnMethods = { "loadTest" })
    public void createRun() throws Exception
    {
        run = client.createRun(testInstances.entities().get(0), test);

        Assert.assertNotNull(run);
    }

    @Test(dependsOnMethods = { "createRun" })
    public void createRunSteps() throws Exception
    {
        client.createRunSteps(run, Util.createRunStepEntities());
    }

    @Test(dependsOnMethods = { "createRunSteps" })
    public void logout() throws Exception
    {
        client.logout();
    }
}
