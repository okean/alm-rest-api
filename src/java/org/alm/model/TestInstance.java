package org.alm.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TestInstance extends Entity
{
    public TestInstance(Entity entity)
    {
        super(entity);
    }

    public TestInstance()
    {
        type("test-instance");
    }

    public String testSetId()
    {
        return fieldValue("cycle-id");
    }

    public void testSetId(String value)
    {
        fieldValue("cycle-id", value);
    }

    public String testId()
    {
        return fieldValue("test-id");
    }

    public void testId(String value)
    {
        fieldValue("test-id", value);
    }

    public String iterations()
    {
        return fieldValue("iterations");
    }

    public void iterations(String value)
    {
        fieldValue("iterations", value);
    }
}
