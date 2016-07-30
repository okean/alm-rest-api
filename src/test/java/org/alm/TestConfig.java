package org.alm;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TestConfig
{
    @Test
    public void accessors() throws Exception
    {
        String properties = propertiesContent(
                "localhost", "8181", "b2b", "rx", "admin", "password");

        Config config = new Config(properties);

        Assert.assertEquals(config.host(), "localhost");
        Assert.assertEquals(config.port(), "8181");
        Assert.assertEquals(config.domain(), "b2b");
        Assert.assertEquals(config.project(), "rx");
        Assert.assertEquals(config.username(), "admin");
        Assert.assertEquals(config.password(), "password");
    }

    @Test
    public void defaultValues() throws Exception
    {
        String properties = propertiesContent("", "", "", "", "", "");

        Config config = new Config(properties);

        Assert.assertEquals(config.host(), "");
        Assert.assertEquals(config.port(), "");
        Assert.assertEquals(config.domain(), "");
        Assert.assertEquals(config.project(), "");
        Assert.assertEquals(config.username(), "");
        Assert.assertEquals(config.password(), "");
    }

    private static String propertiesContent(
            String host, String port, String domain, String project, String username, String password)
    {
        return String.format(
                "host=%s\nport=%s\ndomain=%s\nproject=%s\nusername=%s\npassword=%s\n",
                host,
                port,
                domain,
                project,
                username,
                password);
    }
}
