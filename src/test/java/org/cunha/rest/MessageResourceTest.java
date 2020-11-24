package org.cunha.rest;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import org.cunha.config.PostgresContainer;
import org.cunha.domain.Message;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

@QuarkusTest
@QuarkusTestResource(PostgresContainer.class)
public class MessageResourceTest {

    private static final String MESSAGE_PAYLOAD = "{\"sendDate\": \"2031-05-03T07:05:03\",\"recipient\": \"teste12345\",\"content\": \"teste\",\"channel\": \"SMS\"}";
    private static final String INVALID_DATE_MESSAGE_PAYLOAD = "{\"sendDate\": \"2001-05-03T07:05:03\",\"recipient\": \"teste12345\",\"content\": \"teste\",\"channel\": \"SMS\"}";

    @Test
    public void testMessageNotFound() {
        given()
                .when()
                .get("/messages/010101010101")
                .then()
                .statusCode(404);
    }

    @Test
    public void deleteMessageNotFound() {
        given()
                .when()
                .delete("/messages/010101010101")
                .then()
                .statusCode(404);
    }

    @Test
    public void deleteScheduledMessage() {
        Response response = postRequest(MESSAGE_PAYLOAD)
                .then()
                .statusCode(201)
                .extract()
                .response();
        given()
                .when()
                .pathParam("id", response.getBody().as(Message.class).getId())
                .delete("/messages/{id}")
                .then()
                .statusCode(204);
    }

    @Test
    public void testScheduleNewMessage() {
        Response response = postRequest(MESSAGE_PAYLOAD)
                .then()
                .statusCode(201)
                .extract()
                .response();
        Message message = response.getBody().as(Message.class);
        assertThat(message.getId(), greaterThan(0L));
        assertThat(response.getHeader("Location"), is(String.format("http://localhost:8081/messages/%s", message.getId())));
    }

    @Test
    public void testScheduleNewInvalidMessage() {
        Response response = postRequest(INVALID_DATE_MESSAGE_PAYLOAD)
                .then()
                .statusCode(400)
                .extract()
                .response();
        String body = response.getBody().asString();
        assertThat(body, equalTo("The date 2001-05-03T07:05:03 is invalid! The send date can't be before the current time!"));
    }

    @Test
    public void testFindMessage() {
        Message message = postRequest(MESSAGE_PAYLOAD)
                .body().as(Message.class);
        Response response = given()
                .when()
                .pathParam("id", message.getId())
                .get("/messages/{id}")
                .then()
                .statusCode(200)
                .extract()
                .response();
        assertThat(response.getBody().as(Message.class).getId(), equalTo(message.getId()));
    }

    private Response postRequest(String messagePayload) {
        return given()
                .contentType("application/json")
                .body(messagePayload)
                .post("/messages");
    }

}
