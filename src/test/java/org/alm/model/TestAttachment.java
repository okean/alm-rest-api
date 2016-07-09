package org.alm.model;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TestAttachment
{
    Attachment attachment;

    @BeforeMethod
    public void initAttachmentEntity()
    {
        attachment = new Attachment();
    }

    @Test(groups = { "attachment" })
    public void type()
    {
        Assert.assertEquals(attachment.type(), "attachment");
    }

    @Test(groups = { "attachment" })
    public void description()
    {
        attachment.description("raw results");

        Assert.assertEquals(attachment.description(), "raw results");
    }

    @Test(groups = { "attachment" })
    public void fileSize()
    {
        attachment.fileSize("412");

        Assert.assertEquals(attachment.fileSize(), "412");
    }

    @Test(groups = { "attachment" })
    public void refType()
    {
        attachment.refType("File");

        Assert.assertEquals(attachment.refType(), "File");
    }

    @Test(groups = { "attachment" })
    public void parentType()
    {
        attachment.parentType("defect");

        Assert.assertEquals(attachment.parentType(), "defect");
    }
}
