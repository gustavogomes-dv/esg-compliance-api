package com.esg.bdd;

import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class TestContext {
    String baseUrl = System.getenv().getOrDefault("ESG_API_URL", "http://localhost:8080").replaceAll("/$", "");
    String token;
    Response response;
    final Map<String, String> values = new HashMap<>();
}
