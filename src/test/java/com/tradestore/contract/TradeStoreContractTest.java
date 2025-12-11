package com.tradestore.contract;

import com.atlassian.oai.validator.restassured.OpenApiValidationFilter;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class TradeStoreContractTest extends BaseContractTest {

    private final OpenApiValidationFilter validationFilter =
            new OpenApiValidationFilter("src/test/resources/tradestoreapi.yml");

    @Test
    void testCreateTradeContract() {
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
                .statusCode(201);
    }
}
