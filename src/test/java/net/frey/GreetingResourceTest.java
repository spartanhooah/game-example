package net.frey;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
class GreetingResourceTest {
    @Test
    void testHelloEndpoint() {
        given().when().get("/games").then().statusCode(200).body("size()", is(1));
    }
}
