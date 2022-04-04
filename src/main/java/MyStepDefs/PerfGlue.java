package MyStepDefs;

import Utils.JMeterHelper;
import io.cucumber.java.en.Given;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.protocol.java.sampler.JavaSampler;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jorphan.collections.HashTree;

import java.io.IOException;

public class PerfGlue {

    @Given("Do a GET call for {int} times")
    public void testPerformanceOfRestCall(int count) throws IOException, InterruptedException {
        StandardJMeterEngine jMeterEngine = JMeterHelper.startJMeter(System.getProperty("user.dir"));
        System.out.println("User directory is "+ System.getProperty("user.dir"));
        JavaSampler sampler = JMeterHelper.createJavaSampler("MyStepDefs.JavaSamplerTest","GET");
        LoopController loopController = JMeterHelper.createLoopController(1);
        ThreadGroup threadGroup = JMeterHelper.createThreadGroup(loopController, count,1);
        HashTree testPlan = JMeterHelper.createTestPlanWithListeners(threadGroup, sampler, "Java Test");
        //JMeterHelper.saveResult(testPlan, System.getProperty("user.dir"), System.getProperty("user.dir"), System.getProperty("user.dir"));
        JMeterHelper.runTest(jMeterEngine, testPlan);
        JMeterHelper.saveResult(testPlan, System.getProperty("user.dir"), System.getProperty("user.dir"), System.getProperty("user.dir"));
        //Thread.sleep(60000);
    }
}
