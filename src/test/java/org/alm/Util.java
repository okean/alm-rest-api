package org.alm;

import java.util.Arrays;
import java.util.List;

import org.alm.model.*;
import org.apache.commons.lang.StringEscapeUtils;

public class Util
{
    public static org.alm.model.Test createTestEntity(String id)
    {
        org.alm.model.Test test = new org.alm.model.Test();

        test.execStatus("No Run");
        test.owner("admin");
        test.status("Design");
        test.subtypeId("MANUAL");
        test.parentId("100");
        test.id(id);
        test.name("CreatePITest");
        test.description("Verify Participating Individuals are properly creted");

        return test;
    }

    public static TestSet createTestSetEntity(String id)
    {
        TestSet testSet = new TestSet();

        testSet.status("Open");
        testSet.subtypeId("101F3974-AD91-49d8-97EF-B3DEC6F0AEA3");
        testSet.comment("<html>"
                + "<body>"
                + "<div align=\"left\"><font face=\"Arial\"><span style=\"font-size:8pt\">"
                + "For running tests on Mansfield Park planning tests</span></font></div>"
                + "</body>"
                + "</html>");
        testSet.linkage("N");
        testSet.id(id);
        testSet.parentId("1");
        testSet.name("Mansfield Testing");

        return testSet;
    }

    public static TestInstance createTestInstanceEntity(String id)
    {
        TestInstance testInstance = new TestInstance();

        testInstance.testSetId("12");
        testInstance.testId("1");
        testInstance.iterations("5");
        testInstance.name("1");
        testInstance.id(id);

        return testInstance;
    }

    public static Attachment createAttachment(String name, String fileSize, String parentId, String parentType)
    {
        Attachment attachment = new Attachment();

        attachment.name(name);
        attachment.fileSize(fileSize);
        attachment.parentId(parentId);
        attachment.parentType(parentType);

        return attachment;
    }

    public static Run createRunEntity()
    {
        Run run = new Run();

        run.id("2");
        run.testInstanceId("42");
        run.testSetId("7");
        run.testId("133");
        run.testConfigId("1133");
        run.status(Run.STATUS_NOT_COMPLETED);
        run.owner("keano");
        run.testType(Run.TEST_TYPE_MANUAL);
        run.host("KITE");
        run.comments("Blocked by 181156");
        run.duration("139");

        return run;
    }

    public static RunStep createRunStepEntity(String runId, String id)
    {
        RunStep runStep = new RunStep();

        runStep.id(id);
        runStep.runId(runId);
        runStep.description("Navigat to HomePage");
        runStep.status("Failed");
        runStep.testId("136");
        runStep.actual("Unauthenticated user");
        runStep.expected("Welcome message should be displayed");
        runStep.executionTime("12:24:11");

        return runStep;
    }

    public static String wrapToHtml(String line)
    {
        return wrapToHtml(Arrays.asList(line));
    }

    public static String wrapToHtml(List<String> lines)
    {
        StringBuilder sb = new StringBuilder();

        sb.append("<html><body>");

        for(String line : lines)
        {
            line = StringEscapeUtils.escapeHtml(line);
            sb.append(String.format("<div align=\"left\"><font face=\"Arial\"><span style=\"font-size:8pt\">%s</span></font></div>", line));
        }

        sb.append("</body></html>");

        return sb.toString();
    }
}
