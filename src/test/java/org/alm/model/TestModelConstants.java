package org.alm.model;

import junit.framework.Assert;

import org.testng.annotations.Test;

public class TestModelConstants
{
    @Test(groups = { "const", "run" })
    public void runConstants()
    {
        Assert.assertEquals("Blocked", Run.STATUS_BLOCKED);
        Assert.assertEquals("Failed", Run.STATUS_FAILED);
        Assert.assertEquals("N/A", Run.STATUS_NA);
        Assert.assertEquals("No Run", Run.STATUS_NO_RUN);
        Assert.assertEquals("Not Completed", Run.STATUS_NOT_COMPLETED);
        Assert.assertEquals("Passed", Run.STATUS_PASSED);
        Assert.assertEquals("hp.qc.run.MANUAL", Run.TEST_TYPE_MANUAL);
    }
}
