package MyStepDefs;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

public class JavaSamplerTest extends AbstractJavaSamplerClient {

    public static int cnt = 0;

    @Override
    public void setupTest(JavaSamplerContext javaSamplerContext) {

    }
    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        SampleResult result = new SampleResult();
        result.setSampleLabel("GET_CALL");
        result.sampleStart();
        Glue aGlue = new Glue();
        try{
            aGlue.doAGETCall("GET");
            result.sampleEnd();
            System.out.println("*******Latency : "+(result.getEndTime()-result.getStartTime()));
            result.setResponseCode("200");
            result.setResponseMessage("OK");
            result.setSuccessful(true);
        } catch (NullPointerException e) {

            result.sampleEnd();
            result.setResponseCode("500");
            result.setResponseMessage("NO");
            result.setSuccessful(true);
            e.printStackTrace();

        } catch (Exception e) {

            result.sampleEnd();
            result.setResponseCode("500");
            result.setResponseMessage("NO");
            result.setSuccessful(true);
            e.printStackTrace();
        } catch (AssertionError e) {

            result.sampleEnd();
            result.setResponseCode("500");
            result.setResponseMessage("NO");
            result.setSuccessful(false);
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void teardownTest(JavaSamplerContext javaSamplerContext) {
        System.out.println("...........Completing the test with counting  cnt..........");

    }

    @Override
    public Arguments getDefaultParameters() {
        return null;

    }
}
