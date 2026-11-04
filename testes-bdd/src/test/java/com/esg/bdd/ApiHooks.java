package com.esg.bdd;

import io.cucumber.java.After;
import io.cucumber.java.Before;

import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;

public class ApiHooks {
    private final TestContext context;

    public ApiHooks(TestContext context) {
        this.context = context;
    }

    @Before
    public void prepareScenario() {
        context.values.clear();
        if (Boolean.getBoolean("cucumber.execution.dry-run")) {
            return;
        }
        String nonce = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        context.values.put("nonce", nonce);
        context.values.put("cnpj", uniqueCnpj());

        String email = System.getenv().getOrDefault("ESG_ADMIN_EMAIL", "admin@esg.com");
        String password = System.getenv().getOrDefault("ESG_ADMIN_PASSWORD", "admin123");
        context.token = given().contentType("application/json")
                .body(Map.of("email", email, "senha", password))
                .when().post(context.baseUrl + "/api/auth/login")
                .then().statusCode(200).extract().path("token");

        String fixtureCnpj = uniqueCnpj();
        long companyId = given().contentType("application/json").header("Authorization", "Bearer " + context.token)
                .body(Map.of("nome", "Empresa fixture " + nonce, "cnpj", fixtureCnpj,
                        "setor", "Serviços ESG", "email", "fixture-" + nonce + "@example.com", "ativa", true))
                .when().post(context.baseUrl + "/api/empresas")
                .then().statusCode(201).extract().path("id");
        context.values.put("empresa_id", Long.toString(companyId));
    }

    @After
    public void removeFixtureCompany() {
        String companyId = context.values.get("empresa_id");
        if (companyId != null && context.token != null) {
            given().header("Authorization", "Bearer " + context.token)
                    .when().delete(context.baseUrl + "/api/empresas/" + companyId);
        }
    }

    private String uniqueCnpj() {
        String digits = String.format("%012d", Math.abs(System.nanoTime() % 1_000_000_000_000L));
        return "91." + digits.substring(0, 3) + "." + digits.substring(3, 6) + "/"
                + digits.substring(6, 10) + "-" + digits.substring(10, 12);
    }
}
