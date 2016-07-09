package org.alm.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Attachment extends Entity
{
    public Attachment(Entity entity)
    {
        super(entity);
    }

    public Attachment()
    {
        type("attachment");
    }

    public String description()
    {
        return fieldValue("description");
    }

    public void description(String value)
    {
        fieldValue("description", value);
    }

    public String fileSize()
    {
        return fieldValue("file-size");
    }

    public void fileSize(String value)
    {
        fieldValue("file-size", value);
    }

    public String refType()
    {
        return fieldValue("ref-type");
    }

    public void refType(String value)
    {
        fieldValue("ref-type", value);
    }

    public String parentType()
    {
        return fieldValue("parent-type");
    }

    public void parentType(String value)
    {
        fieldValue("parent-type", value);
    }
}
