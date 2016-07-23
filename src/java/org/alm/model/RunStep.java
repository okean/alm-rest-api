package org.alm.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RunStep extends Entity
{
    public RunStep(Entity entity)
    {
        super(entity);
    }

    public RunStep()
    {
        type("run-step");
    }


    @Override
    public void clearBeforeUpdate()
    {
        removeField("parent-id");

        super.clearBeforeUpdate();
    }

    public String runId()
    {
        return fieldValue("parent-id");
    }

    public void runId(String value)
    {
        fieldValue("parent-id", value);
    }

    public String description()
    {
        return fieldValue("description");
    }

    public void description(String value)
    {
        fieldValue("description", value);
    }

    public String status()
    {
        return fieldValue("status");
    }

    public void status(String value)
    {
        fieldValue("status", value);
    }

    public String testId()
    {
        return fieldValue("test-id");
    }

    public void testId(String value)
    {
        fieldValue("test-id", value);
    }

    public String actual()
    {
        return fieldValue("actual");
    }

    public void actual(String value)
    {
        fieldValue("actual", value);
    }

    public String expected()
    {
        return fieldValue("expected");
    }

    public void expected(String value)
    {
        fieldValue("expected", value);
    }

    public String executionTime()
    {
        return fieldValue("execution-time");
    }

    public void executionTime(String value)
    {
        fieldValue("execution-time", value);
    }
}
