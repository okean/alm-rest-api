package org.alm;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

/*
 * # ALM connection properties
 *
 * host=localhost
 * port=8181
 * domain=b2b
 * project=rx
 * username=admin
 * password=admin
 */

public class Config
{
    private Properties properties;

    public Config(Properties properties) throws Exception
    {
        this.properties = properties;

        if (!check())
        {
            throw new Exception("Invalid ALM config.");
        }
    }

    public Config(String content) throws Exception
    {
        this(createPropertyFile(content));
    }

    public String host()
    {
        return properties.getProperty("host", "");
    }

    public String port()
    {
        return properties.getProperty("port", "");
    }

    public String domain()
    {
        return properties.getProperty("domain", "");
    }

    public String project()
    {
        return properties.getProperty("project", "");
    }

    public String username()
    {
        return properties.getProperty("username", "");
    }

    public String password()
    {
        return properties.getProperty("password", "");
    }

    private static Properties createPropertyFile(String content) throws IOException
    {
        Properties almProperties = new Properties();

        almProperties.load(new StringReader(content));

        return almProperties;
    }

    private boolean check()
    {
        return properties.containsKey("host")
            && properties.containsKey("port")
            && properties.containsKey("domain")
            && properties.containsKey("project")
            && properties.containsKey("username")
            && properties.containsKey("password");
    }
}
