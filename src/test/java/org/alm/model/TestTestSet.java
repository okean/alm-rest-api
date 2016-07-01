package org.alm.model;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TestTestSet
{
    TestSet testSet;

    @BeforeMethod
    public void initTestSetEntity()
    {
        testSet = new TestSet();
    }

    @Test(groups = { "test-set" })
    public void type()
    {
        Assert.assertEquals(testSet.type(), "test-set");
    }

    @Test(groups = { "test-set" })
    public void status()
    {
        testSet.status("Open");

        Assert.assertEquals(testSet.status(), "Open");
    }

    @Test(groups = { "test-set" })
    public void subtypeId()
    {
        testSet.subtypeId("101F3974-AD91-49d8-97EF-B3DEC6F0AEA3");

        Assert.assertEquals(testSet.subtypeId(), "101F3974-AD91-49d8-97EF-B3DEC6F0AEA3");
    }

    @Test(groups = { "test-set" })
    public void comment()
    {
        testSet.comment("<html>"
                + "<body>"
                + "<div align=\"left\"><font face=\"Arial\"><span style=\"font-size:8pt\">"
                + "For running tests on Mansfield Park planning tests</span></font></div>"
                + "</body>"
                + "</html>");

        Assert.assertEquals(testSet.comment(), "<html>"
                + "<body>"
                + "<div align=\"left\"><font face=\"Arial\"><span style=\"font-size:8pt\">"
                + "For running tests on Mansfield Park planning tests</span></font></div>"
                + "</body>"
                + "</html>");
    }

    @Test(groups = { "test-set" })
    public void linkage()
    {
        testSet.linkage("N");

        Assert.assertEquals(testSet.linkage(), "N");
    }
}
