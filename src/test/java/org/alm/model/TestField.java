package org.alm.model;

import java.util.Arrays;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.alm.EntityMarshallingUtils.unmarshal;
import static org.alm.EntityMarshallingUtils.marshal;

public class TestField
{
    @Test(groups = { "field" })
    public void initFieldDefault()
    {
        Field field = new Field();

        Assert.assertNull(field.name());
        Assert.assertNull(field.value());
    }

    @Test(groups = { "field" })
    public void initFieldOneParam()
    {
        Field field = new Field("status");

        Assert.assertEquals(field.name(), "status");
        Assert.assertNull(field.value());
    }

    @Test(groups = { "field" })
    public void initFieldTwoParam()
    {
        Field field = new Field("status", "Design");

        Assert.assertEquals(field.name(), "status");
        Assert.assertEquals(field.value(), "Design");
    }

    @Test(groups = { "field" })
    public void name()
    {
        Field field = new Field();
        field.name("status");

        Assert.assertEquals(field.name(), "status");
    }

    @Test(groups = { "field" })
    public void value()

    {
        Field field = new Field();
        field.value("Design");
        field.value("Automated");

        Assert.assertEquals(field.value(), "Design");
        Assert.assertEquals(field.values(), Arrays.asList("Design", "Automated"));
    }

    @Test(groups = { "field" })
    public void objectToXml() throws Exception
    {
        String fieldName = "steps";
        String fieldValues[] = { "In the login Panel, enter the username", "Click 'Login' button" };

        Field field = createFieldEntity(fieldName, fieldValues);

        String expectedXml = createXmlFieldEntity(fieldName, fieldValues);

        String actualXml = unmarshal(Field.class, field);

        Assert.assertEquals(actualXml, expectedXml);
    }

    @Test(groups = { "field" })
    public void xmlToObject() throws Exception
    {
        String fieldName = "steps";
        String fieldValues[] = { "In the login Panel, enter the username", "Click 'Login' button" };

        String xmlFieldTemplate = createXmlFieldEntity(fieldName, fieldValues);

        Field expectedFieldEntity = createFieldEntity(fieldName, fieldValues);

        Field actualFieldEntity = marshal(Field.class, xmlFieldTemplate);

        Assert.assertTrue(EqualsBuilder.reflectionEquals(actualFieldEntity, expectedFieldEntity));
    }

    private static Field createFieldEntity(String name, String... values)
    {
        Field field = new Field(name);

        for (String value : values)
        {
            field.value(value);
        }

        return field;
    }

    private static String createXmlFieldEntity(String name, String... values)
    {
        StringBuilder xmlFieldEntity = new StringBuilder();

        xmlFieldEntity.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
        xmlFieldEntity.append(String.format("<Field Name=\"%s\">", name));

        for (String value : values)
        {
            xmlFieldEntity.append(String.format("<Value>%s</Value>", value));
        }

        xmlFieldEntity.append("</Field>");

        return xmlFieldEntity.toString();
    }
}
