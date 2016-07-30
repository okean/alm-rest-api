package org.alm;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.alm.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client
{
    private final static Logger Log = LoggerFactory.getLogger(Client.class);

    private final Config config;

    public Client(Config almConfig)
    {
        config = almConfig;

        RestConnector.instance().init(
                config.host(), config.port(), config.domain(), config.project());
    }

    public void login() throws Exception
    {
        Log.info(String.format("Logging in as '%s' ...", config.username()));

        Dao.login(config.username(), config.password());

        Log.info(String.format("Successfully authenticated as '%s'", config.username()));
    }

    public void logout() throws Exception
    {
        Dao.logout();
    }

    public TestSet loadTestSet(String testSetId) throws Exception
    {
        Log.info(String.format("Loading TestSet ... (test-set-id = %s)", testSetId));

        TestSet testSet = Dao.readTestSet(testSetId);

        Log.info(String.format("Loaded TestSet (test-set-id = %s, '%s')", testSet.id(), testSet.name()));

        return testSet;
    }

    public TestInstances loadTestInstances(TestSet testSet) throws Exception
    {
        Log.info(String.format("Loading TestInstances ... (test-set-id = %s)", testSet.id()));

        TestInstances testInstances = Dao.readTestInstances(testSet.id());

        Log.info(String.format("Loaded %d TestInstances", testInstances.entities().size()));

        return testInstances;
    }

    public Test loadTest(TestInstance testInstance) throws Exception
    {
        Log.info(String.format("Loading Test ... (test-id = %s)", testInstance.testId()));

        Test test = Dao.readTest(testInstance.testId());

        Log.info(String.format("Loaded Test (test-id = %s, '%s'", test.id(), test.name()));

        return test;
    }

    public Run createRun(TestInstance testInstance, Test test) throws Exception
    {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        final String runName = "Run " + dateFormat.format(new Date());

        Run prepRun = new Run();
        prepRun.testInstanceId(testInstance.id());
        prepRun.name(runName);
        prepRun.testId(testInstance.testId());
        prepRun.owner(config.username());
        prepRun.testSetId(testInstance.testSetId());
        prepRun.testType(Run.TEST_TYPE_MANUAL);
        prepRun.status(Run.STATUS_NOT_COMPLETED);
        prepRun.host(hostName().toUpperCase());
        prepRun.comments(test.description());

        Log.info(String.format("Creating Run ... ('%s', %s)", prepRun.name(), prepRun.status()));

        Run run = Dao.createRun(prepRun);

        Log.info(String.format("Run has been created (run-id = %s, '%s', %s)", run.id(), run.name(), run.status()));

        return run;
    }

    public void createRunSteps(Run run, RunSteps runSteps) throws Exception
    {
        Log.info("Creating RunSteps ...");

        for(RunStep runStep : runSteps.entities())
        {
            runStep.runId(run.id());

            Log.info(String.format("Creating RunStep ... ('%s', %s)", runStep.name(), runStep.status()));

            Dao.createRunStep(runStep);

            Log.info(String.format("RunStep has been created (run-step-id = %s, '%s', %s)",
                    runStep.id(), runStep.name(),runStep.status()));
        }
    }

    private static String hostName() throws UnknownHostException
    {
        try
        {
            return InetAddress.getLocalHost().getHostName();
        }
        catch(UnknownHostException e)
        {
            return "Unknown hostname";
        }
    }
}
