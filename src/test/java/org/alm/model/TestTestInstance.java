package org.alm.model;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TestTestInstance
{
    TestInstance testInstance;

    @BeforeMethod
    public void initTestInstanceEntity()
    {
        testInstance = new TestInstance();
    }

    @Test(groups = { "test-instance" })
    public void type()
    {
        Assert.assertEquals(testInstance.type(), "test-instance");
    }

    @Test(groups = { "test-instance" })
    public void testSetId()
    {
        testInstance.testSetId("1");

        Assert.assertEquals(testInstance.testSetId(), "1");
    }

    @Test(groups = { "test-instance" })
    public void testId()
    {
        testInstance.testId("1");

        Assert.assertEquals(testInstance.testId(), "1");
    }

    @Test(groups = { "test-instance" })
    public void iterations()
    {
        testInstance.iterations("5");

        Assert.assertEquals(testInstance.iterations(), "5");
    }
}
