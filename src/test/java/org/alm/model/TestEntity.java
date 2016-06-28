package org.alm.model;

import static org.alm.EntityMarshallingUtils.marshal;
import static org.alm.EntityMarshallingUtils.unmarshal;

import org.alm.EntityAssert;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TestEntity
{
    Entity entity;

    @BeforeMethod
    public void initEntity()
    {
        entity = new Entity();
    }

    @Test(groups = { "entity" })
    public void initEntityWithParam()
    {
        entity.type("run");
        entity.name("Functional");

        Entity aux = new Entity(entity);

        Assert.assertEquals(aux.type(), "run");
        Assert.assertTrue(EqualsBuilder.reflectionEquals(aux.fields(), entity.fields()));
    }

    @Test(groups = { "entity" })
    public void fields()
    {
        Assert.assertTrue(entity.fields().isEmpty());

        entity.fieldValue("owner", "admin");
        entity.fieldValue("status", "Passed");

        Assert.assertEquals(entity.fields().size(), 2);
        Assert.assertEquals(entity.fieldValue("owner"), "admin");
        Assert.assertEquals(entity.fieldValue("status"), "Passed");

        Field owner = entity.field("owner");
        Field status = entity.field("status");

        Assert.assertEquals(owner.value(), "admin");
        Assert.assertEquals(status.value(), "Passed");

        entity.removeField("status");

        Assert.assertEquals(entity.fields().size(), 1);
        Assert.assertNull(entity.field("status"));
    }

    @Test(groups = { "entity" })
    public void type()
    {
        entity.type("run");

        Assert.assertEquals(entity.type(), "run");
    }

    @Test(groups = { "entity" })
    public void id()
    {
        entity.id("21");

        Assert.assertEquals(entity.id(), "21");
    }

    @Test(groups = { "entity" })
    public void parentId()
    {
        entity.parentId("2");

        Assert.assertEquals(entity.parentId(), "2");
    }

    @Test(groups = { "entity" })
    public void name()
    {
        entity.name("Functional");

        Assert.assertEquals(entity.name(), "Functional");
    }

    @Test(groups = { "entity" })
    public void objectToXml() throws Exception
    {
        String enityType = "run";
        Field fields[] = { new Field("name", "Functional"), new Field("id", "100") };

        Entity entity = createEntity(enityType, fields);

        String expectedXml = createXmlEntity(enityType, fields);

        String actualXml = unmarshal(Entity.class, entity);

        Assert.assertEquals(actualXml, expectedXml);
    }

    @Test(groups = { "entity" })
    public void xmlToObject() throws Exception
    {
        String enityType = "run";
        Field fields[] = { new Field("name", "Functional"), new Field("id", "100") };

        String xmlEntityTemplate = createXmlEntity(enityType, fields);

        Entity expectedEntity = createEntity(enityType, fields);

        Entity actualEntity = marshal(Entity.class, xmlEntityTemplate);

        EntityAssert.assertEquals(actualEntity, expectedEntity);
    }

    private static Entity createEntity(String type, Field... fields)
    {
        Entity entity = new Entity();
        entity.type(type);

        for (Field field : fields)
        {
            entity.fieldValue(field.name(), field.value());
        }

        return entity;
    }

    private static String createXmlEntity(String type, Field... fields)
    {
        StringBuilder xmlFieldEntity = new StringBuilder();

        xmlFieldEntity.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
        xmlFieldEntity.append(String.format("<Entity Type=\"%s\">", type));
        xmlFieldEntity.append("<Fields>");

        for (Field field : fields)
        {
            xmlFieldEntity.append(
                    String.format("<Field Name=\"%s\"><Value>%s</Value></Field>", field.name(), field.value()));
        }

        xmlFieldEntity.append("</Fields>");
        xmlFieldEntity.append("</Entity>");

        return xmlFieldEntity.toString();
    }
}
