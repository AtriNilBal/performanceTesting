package Utils;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.gui.ArgumentsPanel;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.control.TransactionController;
import org.apache.jmeter.control.gui.LoopControlPanel;
import org.apache.jmeter.control.gui.TestPlanGui;
import org.apache.jmeter.control.gui.TransactionControllerGui;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.protocol.java.control.gui.JavaTestSamplerGui;
import org.apache.jmeter.protocol.java.sampler.JavaSampler;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.reporters.Summariser;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.threads.gui.ThreadGroupGui;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.visualizers.backend.BackendListener;
import org.apache.jorphan.collections.HashTree;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class JMeterHelper {

    public JMeterHelper() {
    }

    public static StandardJMeterEngine startJMeter(String jmeterHomePath) {
        File jmeterHome = new File(jmeterHomePath);
        File jmeterProperties = new File(jmeterHome + "/jmeter.properties");
        StandardJMeterEngine jmeter = new StandardJMeterEngine();
        JMeterUtils.setJMeterHome(jmeterHomePath);
        JMeterUtils.loadJMeterProperties(jmeterProperties.getPath());
        JMeterUtils.initLogging();
        JMeterUtils.initLocale();
        return jmeter;
    }

    public static JavaSampler createJavaSampler(String className, String requestName) {
        JavaSampler javaSampler = new JavaSampler();
        javaSampler.setName(requestName);
        javaSampler.setClassname(className);
        javaSampler.setProperty("TestElement.test_class", JavaSampler.class.getName());
        javaSampler.setProperty("TestElement.gui_class", JavaTestSamplerGui.class.getName());
        return javaSampler;
    }

    public static LoopController createLoopController(int loops) {
        LoopController loopController = new LoopController();
        loopController.setLoops(loops);
        loopController.setFirst(true);
        loopController.setProperty("TestElement.test_class", LoopController.class.getName());
        loopController.setProperty("TestElement.gui_class", LoopControlPanel.class.getName());
        loopController.initialize();
        return loopController;
    }

    public static ThreadGroup createThreadGroup(LoopController loopController, int noOfThreads, int rampUp) {
        ThreadGroup threadGroup = new ThreadGroup();
        threadGroup.setName("Sample Thread Group");
        threadGroup.setNumThreads(noOfThreads);
        threadGroup.setRampUp(rampUp);
        threadGroup.setSamplerController(loopController);
        threadGroup.setProperty("TestElement.test_class", ThreadGroup.class.getName());
        threadGroup.setProperty("TestElement.gui_class", ThreadGroupGui.class.getName());
        return threadGroup;
    }

    public static ThreadGroup createThreadGroup(LoopController loopController, int noOfThreads, int rampUp, int duration) {
        ThreadGroup threadGroup = new ThreadGroup();
        threadGroup.setName("Sample Thread Group");
        threadGroup.setNumThreads(noOfThreads);
        threadGroup.setRampUp(rampUp);
        threadGroup.setDuration((long) duration);
        threadGroup.setSamplerController(loopController);
        threadGroup.setProperty("TestElement.test_class", ThreadGroup.class.getName());
        threadGroup.setProperty("TestElement.gui_class", ThreadGroupGui.class.getName());
        return threadGroup;
    }

    public static TransactionController createTransactionController(String name) {
        TransactionController transactionController = new TransactionController();
        transactionController.setGenerateParentSample(true);
        transactionController.isEnabled();
        transactionController.setName(name);
        transactionController.setProperty("TestElement.test_class", TransactionController.class.getName());
        transactionController.setProperty("TestElement.gui_class", TransactionControllerGui.class.getName());
        return transactionController;
    }

    public static HashTree createTestPlan(ThreadGroup threadGroup, JavaSampler sampler, String planName) {
        HashTree testPlanTree = new HashTree();
        TestPlan testPlan = new TestPlan(planName);
        testPlan.setProperty("TestElement.test_class", TestPlan.class.getName());
        testPlan.setProperty("TestElement.gui_class", TestPlanGui.class.getName());
        testPlan.setUserDefinedVariables((Arguments) (new ArgumentsPanel()).createTestElement());
        testPlanTree.add(testPlan);
        HashTree threadGroupHashTree = testPlanTree.add(testPlan, threadGroup);
        threadGroupHashTree.add(sampler);
        return testPlanTree;
    }

    public static TestPlan createTestPlan(String name) {
        TestPlan testPlan = new TestPlan(name);
        testPlan.setEnabled(true);
        testPlan.setSerialized(false);
        testPlan.setFunctionalMode(false);
        testPlan.setTearDownOnShutdown(true);
        testPlan.setProperty("TestElement.test_class", TestPlan.class.getName());
        testPlan.setProperty("TestElement.gui_class", TestPlanGui.class.getName());
        testPlan.setUserDefinedVariables((Arguments) (new ArgumentsPanel()).createTestElement());
        return testPlan;
    }

    public static HashTree createTestPlanWithListeners(ThreadGroup threadGroup, JavaSampler sampler, String planName, BackendListener grafanaListener, BackendListener qperfListener) {
        HashTree testPlanTree = new HashTree();
        TestPlan testPlan = new TestPlan(planName);
        testPlan.setProperty("TestElement.test_class", TestPlan.class.getName());
        testPlan.setProperty("TestElement.gui_class", TestPlanGui.class.getName());
        testPlan.setUserDefinedVariables((Arguments) (new ArgumentsPanel()).createTestElement());
        testPlanTree.add(testPlan);
        HashTree threadGroupHashTree = testPlanTree.add(testPlan, threadGroup);
        threadGroupHashTree.add(grafanaListener);
        threadGroupHashTree.add(qperfListener);
        threadGroupHashTree.add(sampler);
        return testPlanTree;
    }

    public static HashTree createTestPlanWithListeners(ThreadGroup threadGroup, JavaSampler sampler, String planName) {
        HashTree testPlanTree = new HashTree();
        TestPlan testPlan = new TestPlan(planName);
        testPlan.setProperty("TestElement.test_class", TestPlan.class.getName());
        testPlan.setProperty("TestElement.gui_class", TestPlanGui.class.getName());
        testPlan.setUserDefinedVariables((Arguments) (new ArgumentsPanel()).createTestElement());
        testPlanTree.add(testPlan);
        HashTree threadGroupHashTree = testPlanTree.add(testPlan, threadGroup);
        threadGroupHashTree.add(sampler);
        return testPlanTree;
    }

    public static void saveResult(HashTree testPlanTree, String jmeterHome, String jtlPath, String csvPath) throws IOException {
        SaveService.saveTree(testPlanTree, new FileOutputStream(jmeterHome + "/report/jmeter_api_sample.jmx"));
        Summariser summer = null;
        String summariserName = JMeterUtils.getPropDefault("summariser.name", "summary");
        if (summariserName.length() > 0) {
            summer = new Summariser(summariserName);
        }

        String reportFile = jtlPath + "/report/report.jtl";
        String csvFile = csvPath + "/report/report.csv";
        ResultCollector logger = new ResultCollector(summer);
        logger.setFilename(reportFile);
        ResultCollector csvlogger = new ResultCollector(summer);
        csvlogger.setFilename(csvFile);
        testPlanTree.add(testPlanTree.getArray()[0], logger);
        testPlanTree.add(testPlanTree.getArray()[0], csvlogger);
    }

    public static void runTest(StandardJMeterEngine jMeterEngine, HashTree testPlanTree) {
        jMeterEngine.configure(testPlanTree);
        jMeterEngine.run();
    }
}
