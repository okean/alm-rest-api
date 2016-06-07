package org.alm;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TestRestConnector
{
    private static final String Host = "test.com";
    private static final String Port = "8080";
    private static final String Domain = "b2b";
    private static final String Project = "rx";
    
    public TestRestConnector()
    {
        RestConnector.instance().init(Host, Port, Domain, Project);
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
}
