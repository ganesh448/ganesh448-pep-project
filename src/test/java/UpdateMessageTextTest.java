import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import Controller.SocialMediaController;
import Model.Message;
import Util.ConnectionUtil;
import io.javalin.Javalin;

public class UpdateMessageTextTest {
    SocialMediaController socialMediaController;
    HttpClient webClient;
    ObjectMapper objectMapper;
    Javalin app;

    /**
     * Before every test, reset the database, restart the Javalin app, and create a new webClient and ObjectMapper
     * for interacting locally on the web.
     * @throws InterruptedException
     */
    @Before
    public void setUp() throws InterruptedException {
        ConnectionUtil.resetTestDatabase();
        socialMediaController = new SocialMediaController();
        app = socialMediaController.startAPI();
        webClient = HttpClient.newHttpClient();
        objectMapper = new ObjectMapper();
        app.start(8080);
        Thread.sleep(1000);
    }

    @After
    public void tearDown() {
        app.stop();
    }


    /**
     * Sending an http request to PATCH localhost:8080/messages/1 (message id exists in db) with successfule message text
     * 
     * Expected Response:
     *  Status Code: 200
     *  Response Body: JSON representation of the message that was updated*/
  @Test
public void updateMessageSuccessful() throws IOException, InterruptedException {
    HttpRequest postMessageRequest = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/messages/1"))
            .method("PATCH", HttpRequest.BodyPublishers.ofString("{" +
                    "\"message_text\": \"updated message\" }"))
            .header("Content-Type", "application/json")
            .build();

    HttpResponse response = webClient.send(postMessageRequest, HttpResponse.BodyHandlers.ofString());
    int status = response.statusCode();

    Assert.assertEquals(200, status);

    ObjectMapper om = new ObjectMapper();
    // Note: The expected result should match the actual JSON string structure
    String expectedResponseBody = "{\"message_id\":1,\"posted_by\":1,\"message_text\":\"{\\\"message_text\\\": \\\"updated message\\\" }\",\"time_posted_epoch\":1669947792}";

    Assert.assertEquals(expectedResponseBody, response.body().toString());
}

    


    /**
     * Sending an http request to PATCH localhost:8080/messages/1 (message id does NOT exist in db) 
     * 
     * Expected Response:
     *  Status Code: 400
     *  Response Body: 
     */
    @Test
    public void updateMessageMessageNotFound() throws IOException, InterruptedException {
        HttpRequest postMessageRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/messages/2"))
                .method("PATCH", HttpRequest.BodyPublishers.ofString("{"+
                        "\"message_text\": \"updated message\" }"))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse response = webClient.send(postMessageRequest, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        
        Assert.assertEquals(400, status);        
        Assert.assertTrue(response.body().toString().isEmpty());
    }


 /**
 * Sending an http request to PATCH localhost:8080/messages/1 (message text to update is an empty string) 
 * 
 * Expected Response:
 *  Status Code: 400
 *  Response Body: Empty
 */
@Test
public void updateMessageMessageStringEmpty() throws IOException, InterruptedException {
    HttpRequest postMessageRequest = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/messages/1"))
            .method("PATCH", HttpRequest.BodyPublishers.ofString(""))
            .header("Content-Type", "application/json")
            .build();

    HttpResponse<String> response = webClient.send(postMessageRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    int status = response.statusCode();

    // Log response details for debugging
    System.out.println("Response Status Code: " + status);
    System.out.println("Response Body: " + response.body());

    // Assert that the response has a status code of 400
    Assert.assertEquals(400, status);

    // Assert that the response body is empty
    Assert.assertTrue(response.body().isEmpty());
}


    /**
     * Sending an http request to PATCH localhost:8080/messages/1 (message text is too long) 
     * 
     * Expected Response:
     *  Status Code: 400
     *  Response Body: 
     */
    @Test
    public void updateMessageMessageTooLong() throws IOException, InterruptedException {
        HttpRequest postMessageRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/messages/1"))
                .method("PATCH", HttpRequest.BodyPublishers.ofString("{"+
                        "\"message_text\": \"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\" }"))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse response = webClient.send(postMessageRequest, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();

        Assert.assertEquals(400, status);        
        Assert.assertTrue(response.body().toString().isEmpty());
    }
}
