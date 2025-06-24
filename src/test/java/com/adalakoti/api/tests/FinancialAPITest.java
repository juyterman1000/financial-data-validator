package com.adalakoti.api.tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class FinancialAPITest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://api.example.com";
        RestAssured.basePath = "/v1";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test(priority = 1)
    public void testGetAccountBalance() {
        Response response = given()
            .header("Authorization", "Bearer " + getAuthToken())
            .pathParam("accountId", "12345")
        .when()
            .get("/accounts/{accountId}/balance")
        .then()
            .statusCode(200)
            .body("currency", equalTo("USD"))
            .body("available_balance", notNullValue())
            .extract().response();

        float balance = response.jsonPath().getFloat("available_balance");
        Assert.assertTrue(balance >= 0, "Balance should not be negative");
    }

    @Test(priority = 2)
    public void testCreatePaymentTransaction() {
        String requestBody = "{\n" +
            "  \"from_account\": \"12345\",\n" +
            "  \"to_account\": \"67890\",\n" +
            "  \"amount\": 100.00,\n" +
            "  \"currency\": \"USD\",\n" +
            "  \"type\": \"TRANSFER\"\n" +
            "}";

        given()
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + getAuthToken())
            .body(requestBody)
        .when()
            .post("/transactions")
        .then()
            .statusCode(201)
            .body("status", equalTo("PENDING"))
            .body("transaction_id", notNullValue());
    }

    @Test(priority = 3)
    public void testGetTransactionHistory() {
        given()
            .header("Authorization", "Bearer " + getAuthToken())
            .queryParam("account_id", "12345")
            .queryParam("limit", 10)
        .when()
            .get("/transactions")
        .then()
            .statusCode(200)
            .body("transactions.size()", lessThanOrEqualTo(10))
            .body("transactions[0].timestamp", notNullValue());
    }

    @Test(priority = 4)
    public void testValidatePaymentProtocol() {
        String isoMessage = buildISO8583Message();

        Response response = given()
            .header("Content-Type", "application/octet-stream")
            .body(isoMessage.getBytes())
        .when()
            .post("/protocol/validate")
        .then()
            .statusCode(200)
            .extract().response();

        Assert.assertEquals(response.jsonPath().getString("message_type"), "0200");
        Assert.assertTrue(response.jsonPath().getBoolean("is_valid"));
    }

    private String getAuthToken() {
        return "test-auth-token";
    }

    private String buildISO8583Message() {
        return "0200B23A800128A180180000000014000000";
    }
}
