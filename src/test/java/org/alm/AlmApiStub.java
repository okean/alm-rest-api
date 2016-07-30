package org.alm;

import static org.alm.Util.*;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import org.alm.model.Attachment;
import org.alm.model.Run;
import org.alm.model.RunStep;
import org.alm.model.RunSteps;
import org.alm.model.TestInstance;
import org.alm.model.TestInstances;
import org.alm.model.TestSet;

@Path("/qcbin")
public class AlmApiStub
{
    private static List<String> cookies = new ArrayList<String>();

    public static String authenticationPoint(String host, String port)
    {
        return String.format("http://%s:%s/qcbin/authentication-point", host, port);
    }

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

    @GET
    @Path("/rest/domains/{domain}/projects/{project}/test-instances")
    @Produces("application/xml")
    public TestInstances testInstances(@PathParam("domain") String domain,
            @PathParam("project") String project,
            @QueryParam("query") String query)
    {
        TestInstance ti1 = createTestInstanceEntity("1");
        TestInstance ti2 = createTestInstanceEntity("2");

        TestInstances testInstances = new TestInstances();
        testInstances.addEntity(ti1);
        testInstances.addEntity(ti2);

        return testInstances;
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

    @POST
    @Path("/rest/domains/{domain}/projects/{project}/runs")
    @Produces("application/xml")
    public Run createRun(Run run)
    {
        run.id("1");

        return run;
    }

    @PUT
    @Path("/rest/domains/{domain}/projects/{project}/runs/{id}")
    @Produces("application/xml")
    public Run updateRun(
            @PathParam("id") String id,
            Run run)
    {
        run.id(id);

        return run;
    }

    @GET
    @Path("/rest/domains/{domain}/projects/{project}/runs/{runId}/run-steps")
    @Produces("application/xml")
    public RunSteps testInstances(@PathParam("runId") String runId)
    {
        RunStep rs1 = createRunStepEntity(runId, "1");
        RunStep rs2 = createRunStepEntity(runId, "2");;

        RunSteps runSteps = new RunSteps();
        runSteps.addEntity(rs1);
        runSteps.addEntity(rs2);

        return runSteps;
    }

    @POST
    @Path("/rest/domains/{domain}/projects/{project}/runs/{runId}/run-steps")
    @Produces("application/xml")
    public RunStep createRunStep(
            @PathParam("runId") String runId,
            RunStep runStep)
    {
        runStep.runId(runId);

        return runStep;
    }

    @PUT
    @Path("/rest/domains/{domain}/projects/{project}/runs/{runId}/run-steps/{id}")
    @Produces("application/xml")
    public RunStep updateRunStep(
            @PathParam("runId") String runId,
            @PathParam("id") String id,
            RunStep runStep)
    {
        runStep.runId(runId);
        runStep.id(id);

        return runStep;
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
