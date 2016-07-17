package org.alm.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Entities")
@XmlAccessorType(XmlAccessType.FIELD)
public class Entities<T>
{
    @XmlElement(name = "Entity", type = Entity.class)
    private List<T> entities;

    public Entities()
    {
        this(new ArrayList<T>());
    }

    public Entities(Collection<T> entities)
    {
        if (entities instanceof List)
        {
            this.entities = (List<T>) entities;
        }
        else
        {
            this.entities = new ArrayList<T>(entities);
        }
    }

    public List<T> entities()
    {
        return entities;
    }

    public void entities(List<T> entities)
    {
        this.entities = entities;
    }

    public void addEntity(T entity)
    {
        entities.add(entity);
    }
}
