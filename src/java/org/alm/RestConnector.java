package org.alm;

public class RestConnector
{
    private String host;
    private String port;
    private String domain;
    private String project;
    
    private RestConnector()
    {
    }
    
    private static class RestConnectorHolder
    {
        private static final RestConnector Instance = new RestConnector(); 
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
}
