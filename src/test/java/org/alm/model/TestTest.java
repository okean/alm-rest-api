package org.alm.model;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TestTest
{
    org.alm.model.Test test;

    @BeforeMethod
    public void initTestEntity()
    {
        test = new org.alm.model.Test();
    }

    @Test(groups = { "test" })
    public void type()
    {
        Assert.assertEquals(test.type(), "test");
    }

    @Test(groups = { "test" })
    public void execStatus()
    {
        test.execStatus("No Run");

        Assert.assertEquals(test.execStatus(), "No Run");
    }

    @Test(groups = { "test" })
    public void owner()
    {
        test.owner("admin");

        Assert.assertEquals(test.owner(), "admin");
    }

    @Test(groups = { "test" })
    public void status()
    {
        test.status("Design");

        Assert.assertEquals(test.status(), "Design");
    }

    @Test(groups = { "test" })
    public void subtypeId()
    {
        test.subtypeId("MANUAL");

        Assert.assertEquals(test.subtypeId(), "MANUAL");
    }

    @Test(groups = { "test" })
    public void description()
    {
        test.description("Verify XML Request message is valid");

        Assert.assertEquals(test.description(), "Verify XML Request message is valid");
    }
}
