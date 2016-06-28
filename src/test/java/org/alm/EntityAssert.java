package org.alm;

import java.util.ArrayList;
import java.util.List;

import org.alm.model.Entity;
import org.alm.model.Field;
import org.testng.Assert;

public final class EntityAssert
{
    private EntityAssert()
    {
    }

    public static void assertEquals(Entity lhs, Entity rhs)
    {
        if (lhs == rhs)
        {
            return;
        }

        if (lhs == null || rhs == null)
        {
            Assert.fail("Invalid entities");
        }

        Assert.assertEquals(
                lhs.type(), rhs.type(), "Entities are not of the same type");

        List<Field> lhsFields = lhs.fields();
        List<Field> rhsFields = rhs.fields();

        Assert.assertEquals(
                lhsFields.size(), rhsFields.size(), "Entities fields are not of the same length");

        List<String> errorMessages = new ArrayList<String>();

        for (Field lhsField : lhsFields)
        {
            try
            {
                String lhsFieldName = lhsField.name();

                Field rhsField = rhs.field(lhsFieldName);

                Assert.assertNotNull(
                        rhsField, String.format("Could not find '%s' field", lhsFieldName));

                Assert.assertEquals(
                        lhsField.value(),
                        rhsField.value(),
                        String.format("Actual and expected value of '%s' field does not match", lhsFieldName));
            }
            catch (AssertionError ex)
            {
                errorMessages.add(ex.getMessage());
                errorMessages.add(System.getProperty("line.separator"));
            }
        }

        if (!errorMessages.isEmpty())
        {
            Assert.fail(errorMessages.toString());
        }
    }
}
