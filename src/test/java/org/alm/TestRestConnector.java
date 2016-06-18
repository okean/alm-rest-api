package org.alm;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlRootElement;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestRestConnector
{
    private static final String Host = "localhost";
    private static final String Port = "8181";
    private static final String Domain = "b2b";
    private static final String Project = "rx";

    private HttpServer server;

    public TestRestConnector()
    {
        RestConnector.instance().init(Host, Port, Domain, Project);
    }

    @BeforeClass
    public void setUp() throws Exception
    {
        final ResourceConfig rc = new ResourceConfig(Resource.class);

        server = GrizzlyHttpServerFactory.createHttpServer(
                URI.create(String.format("http://%s:%s/api", Host, Port)), rc);
    }

    @AfterClass
    public void tearDown() throws Exception
    {
        server.shutdownNow();
    }

    @Test
    public void accessors()
    {
        RestConnector connector = RestConnector.instance();

        Assert.assertEquals(connector.host(), Host);
        Assert.assertEquals(connector.port(), Port);
        Assert.assertEquals(connector.domain(), Domain);
        Assert.assertEquals(connector.project(), Project);
    }

    @Test void buildEntityUrl() throws Exception
    {
        RestConnector connector = RestConnector.instance();

        Assert.assertEquals(
                connector.buildUrl("api/users"),
                String.format("http://%s:%s/api/users", Host, Port));

        Assert.assertEquals(
                connector.buildEntityCollectionUrl("run"),
                String.format("/qcbin/rest/domains/%s/projects/%s/runs", Domain, Project));

        Assert.assertEquals(
                connector.buildEntityUrl("run", "1"),
                String.format("/qcbin/rest/domains/%s/projects/%s/runs/1", Domain, Project));
    }

    @Test
    public void get() throws Exception
    {
        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("name", "user");
        queryParams.put("type", "delegate");

        String get = RestConnector.instance().get("api/", String.class, null, queryParams);

        Assert.assertEquals(get, "GET name=user; type=delegate");
    }

    @Test
    public void getXmlContent() throws Exception
    {
        Entity entity = RestConnector.instance().get("api/entity", Entity.class, null, null);

        Assert.assertEquals(entity.getName(), "user");
        Assert.assertEquals(entity.getType(), "delegate");
    }

    @Test
    public void post() throws Exception
    {
        Entity entity = new Entity();
        entity.setName("user");
        entity.setType("delegate");

        String post = RestConnector.instance().post("api/", String.class, null, null, entity);

        Assert.assertEquals(post, "POST name=user; type=delegate");
    }

    @Test
    public void put() throws Exception
    {
        Entity entity = new Entity();
        entity.setName("user");
        entity.setType("delegate");

        String put = RestConnector.instance().put("api/", String.class, null, null, entity);

        Assert.assertEquals(put, "PUT name=user; type=delegate");
    }

    @Test
    public void delete() throws Exception
    {
        Response delete = RestConnector.instance().delete("api/", null, null);

        Assert.assertEquals(delete.getStatus(), Status.NO_CONTENT.getStatusCode());
    }

    @Path("/")
    public static class Resource
    {
        @GET
        @Produces("text/plain")
        public String get(@QueryParam("name") String name, @QueryParam("type") String type)
        {
            return String.format("GET name=%s; type=%s", name, type);
        }

        @GET
        @Path("/entity")
        @Produces("application/xml")
        public Entity getEntity()
        {
            Entity entity = new Entity();
            entity.setName("user");
            entity.setType("delegate");

            return entity;
        }

        @POST
        @Produces("text/plain")
        public String post(Entity entity)
        {
            return String.format("POST name=%s; type=%s", entity.getName(), entity.getType());
        }

        @PUT
        @Produces("text/plain")
        public String put(Entity entity)
        {
            return String.format("PUT name=%s; type=%s", entity.getName(), entity.getType());
        }

        @DELETE
        public void delete()
        {
        }
    }

    @XmlRootElement()
    private static class Entity
    {
        private String name;
        private String type;

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public String getType()
        {
            return type;
        }

        public void setType(String type)
        {
            this.type = type;
        }
    }
}
