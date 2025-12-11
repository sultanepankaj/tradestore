package com.tradestore.contract;

import com.atlassian.oai.validator.restassured.OpenApiValidationFilter;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class TradeStoreContractTest extends BaseContractTest {

    private final OpenApiValidationFilter validationFilter =
            new OpenApiValidationFilter("src/test/resources/tradestoreapi.yml");


    @Disabled("Test is currently broken")
    @Test
    void testCreateTradeNewVersionContract() {
        String requestBody = """
                {
                  "tradeId": "T1",
                  "version": 5,
                  "counterPartyId": "CP-1",
                  "bookId": "B1",
                  "maturityDate": "2025-12-31",
                  "expired": false
                }
                """;

        given()
                .filter(validationFilter)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/v1/trades")
                .then()
                .statusCode(201);
    }

    @Disabled("Test is currently broken")
    @Test
    void testCreateTradeSameVersionContract() throws Exception {

        String requestBody = """
                {
                  "tradeId": "T1",
                  "version": 1,
                  "counterPartyId": "CP-1",
                  "bookId": "B1",
                  "maturityDate": "2025-12-31",
                  "expired": false
                }
                """;

        given()
                .filter(validationFilter)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/v1/trades")
                .then()
                .statusCode(200);
    }

    @Disabled("Test is currently broken")
    @Test
    void testLowerVersionContract() {
        String requestBody = """
                {
                  "tradeId": "T1",
                  "version": 0,
                  "counterPartyId": "CP-1",
                  "bookId": "B1",
                  "maturityDate": "2025-12-31",
                  "expired": false
                }
                """;

        given()
                .filter(validationFilter)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/v1/trades")
                .then()
                .statusCode(409);
    }

    @Disabled("Test is currently broken")
    @Test
    void testMaturityDateIsInPast() {
        String requestBody = """
                {
                  "tradeId": "T1",
                  "version": 0,
                  "counterPartyId": "CP-1",
                  "bookId": "B1",
                  "maturityDate": "2024-12-31",
                  "expired": false
                }
                """;

        given()
                .filter(validationFilter)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/v1/trades")
                .then()
                .statusCode(422);
    }


}
