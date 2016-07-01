package org.alm.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TestSet extends Entity
{
    public TestSet(Entity entity)
    {
        super(entity);
    }

    public TestSet()
    {
        type("test-set");
    }

    public String status()
    {
        return fieldValue("status");
    }

    public void status(String value)
    {
        fieldValue("status", value);
    }

    public String subtypeId()
    {
        return fieldValue("subtype-id");
    }

    public void subtypeId(String value)
    {
        fieldValue("subtype-id", value);
    }

    public String comment()
    {
        return fieldValue("comment");
    }

    public void comment(String value)
    {
        fieldValue("comment", value);
    }

    public String linkage()
    {
        return fieldValue("linkage");
    }

    public void linkage(String value)
    {
        fieldValue("linkage", value);
    }
}
