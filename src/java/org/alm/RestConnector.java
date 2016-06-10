package org.alm;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestConnector
{
    private final static Logger Log = LoggerFactory.getLogger(RestConnector.class);
    
    private String host;
    private String port;
    private String domain;
    private String project;
    
    private Map<String, Cookie> cookies;
    
    private RestConnector()
    {
    }
    
    private static class RestConnectorHolder
    {
        static final RestConnector Instance = new RestConnector(); 
    }
    
    public static RestConnector instance()
    {
        return RestConnectorHolder.Instance;
    }
    
    public void init(String host, String port, String domain, String project)
    {
        this.host = host;
        this.port = port;
        this.domain = domain;
        this.project = project;
    }
   
    public String host()
    {
        return host;
    }
    
    public String port()
    {
        return port;
    }

    public String domain()
    {
        return domain;
    }    
    
    public String project()
    {
        return project;
    }
        
    public String buildUrl(String path) throws Exception
    {
        if(StringUtils.isNotBlank(host) && StringUtils.isNotBlank(port))
        {
            return String.format("http://%s:%s/%s", host, port, path);   
        }
        
        throw new Exception("Host/Port are invalid. Call init() to initialize them properly.");
    }    
         
    public <T> T get(String path, Class<T> entityType, MultivaluedMap<String, Object> headers, Map<String, String> queryParams) throws Exception
    {
        return call(HttpMethod.GET, path, headers, queryParams, null, entityType);
    }
    
    public Response post(String path, MultivaluedMap<String, Object> headers, Map<String, String> queryParams, Object payload) throws Exception
    {
        return call(HttpMethod.POST, path, headers, queryParams, payload);
    }
    
    public Response put(String path, MultivaluedMap<String, Object> headers, Map<String, String> queryParams, Object payload) throws Exception
    {
        return call(HttpMethod.PUT, path, headers, queryParams, payload);
    }
    
    public Response delete(String path, MultivaluedMap<String, Object> headers, Map<String, String> queryParams) throws Exception
    {
        return call(HttpMethod.DELETE, path, headers, queryParams, null);
    }      
    
    private static WebTarget createWebTarget(String uri, Map<String, String> queryParams) throws URISyntaxException
    {
        WebTarget webTarget = null;
        
        URI u = new URI(uri);
        Client client = ClientBuilder.newClient();
        
        webTarget = client.target(u);
        
        if (MapUtils.isNotEmpty(queryParams))
        {
            for (Entry<String, String> entry : queryParams.entrySet())
            {
                if (StringUtils.isNotBlank(entry.getKey()) && StringUtils.isNotBlank(entry.getValue()))
                {
                    webTarget = webTarget.queryParam(entry.getKey(), entry.getValue());
                }
            }
        }

        return webTarget;
    }
    
    private static boolean isStatusCodeOK(int statusCode)
    {
        return statusCode >= Status.OK.getStatusCode() && 
               statusCode <= Status.PARTIAL_CONTENT.getStatusCode();
    }
    
    private <T> T call(
            String methodName, 
            String restPath, 
            MultivaluedMap<String, Object> headers, 
            Map<String, String> queryParams, 
            Object payload,
            Class<T> entityType) throws Exception
            {
                Response res = call(methodName, restPath, headers, queryParams, payload);
                
                if(!res.hasEntity())
                {
                    return null;
                }
                
                return (T) res.readEntity(entityType);
            }
    
    private Response call(
            String methodName, 
            String path, 
            MultivaluedMap<String, Object> headers, 
            Map<String, String> queryParams, 
            Object payload) throws Exception
    {
        WebTarget webTarget = createWebTarget(buildUrl(path), queryParams);
        
        Builder result = webTarget.request().headers(headers);
        
        if (MapUtils.isNotEmpty(cookies))
        {
            for (Entry<String, Cookie> cookie : cookies.entrySet())
            {
                result = result.cookie(cookie.getValue());
            }
        }
        
        Response res = result.method(
                methodName, Entity.entity(payload, MediaType.APPLICATION_XML), Response.class);
        
        int statusCode = res.getStatus();
        
        if(!isStatusCodeOK(statusCode))
        {   
            throw new ResponseException(res, path);
        }
        
        updateCookies(res.getCookies());
        
        return res;
    }
    
    private void updateCookies(Map<String, NewCookie> newCookies)
    {
        if (MapUtils.isNotEmpty(newCookies))
        {
            cookies.putAll(newCookies);
        }        
    }

}
