package MyStepDefs;

import io.cucumber.java.en.Given;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Glue {

    /**
     * baseUri, body and path is required to be provided
     * @param callType
     */
    @Given("Do a {string} call")
    public void doAGETCall(String callType) {
        String body = "<json body>";
        String baseUri = "<input http baseuri>";
        String path = "<input path starting with />";
        System.out.println("Do a "+callType+" call");
        RestAssured.baseURI = baseUri;
        RequestSpecification httpRequest = RestAssured.given();
        Response response = httpRequest.request(Method.GET, path);
        System.out.println("Status : "+ response.getStatusCode());
    }
}
