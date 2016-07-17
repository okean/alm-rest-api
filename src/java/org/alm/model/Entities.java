package org.alm.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Entities")
@XmlAccessorType(XmlAccessType.FIELD)
public class Entities
{
    @XmlElement(name="Entity")
    private List<Entity> entities;

    public List<Entity> list()
    {
        return entities;
    }

    public void list(List<Entity> entities)
    {
        this.entities = entities;
    }
}
