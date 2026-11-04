package com.esg.bdd;

import io.cucumber.docstring.DocString;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.File;
import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApiSteps {
    private final TestContext context;

    public ApiSteps(TestContext context) {
        this.context = context;
    }

    @When("^envio (POST|GET|PUT|DELETE) para \"([^\"]+)\"$")
    public void sendWithoutBody(String method, String path) {
        send(method, "", path, null);
    }

    @When("^envio (POST|GET|PUT|DELETE) (sem autenticação|autenticado|público) para \"([^\"]+)\"$")
    public void sendModifiedWithoutBody(String method, String modifier, String path) {
        send(method, modifier, path, null);
    }

    @When("^envio (POST|GET|PUT|DELETE) para \"([^\"]+)\" com JSON:$")
    public void sendWithBody(String method, String path, DocString body) {
        send(method, "", path, body.getContent());
    }

    @When("^envio (POST|GET|PUT|DELETE) (sem autenticação|autenticado|público) para \"([^\"]+)\" com JSON:$")
    public void sendModifiedWithBody(String method, String modifier, String path, DocString body) {
        send(method, modifier, path, body.getContent());
    }

    @Then("^o status HTTP deve ser (\\d+)$")
    public void verifyStatus(int expected) {
        assertEquals(expected, context.response.statusCode(),
                "Resposta recebida: " + context.response.asString());
    }

    @Then("^a resposta deve obedecer ao schema \"([^\"]+)\"$")
    public void verifySchema(String schemaName) {
        File schema = new File("src/test/resources/schemas/" + schemaName);
        context.response.then().body(matchesJsonSchema(schema));
    }

    @Then("^guardo o campo \"([^\"]+)\" da resposta como \"([^\"]+)\"$")
    public void saveResponseField(String field, String name) {
        context.values.put(name, context.response.jsonPath().getString(field));
    }

    @Then("^o campo \"([^\"]+)\" da resposta deve ser ([0-9]+(?:\\.[0-9]+)?)$")
    public void verifyNumericField(String field, String expected) {
        BigDecimal actual = new BigDecimal(context.response.jsonPath().getString(field));
        assertEquals(0, actual.compareTo(new BigDecimal(expected)),
                "Valor inesperado para " + field + ": " + actual);
    }

    private void send(String method, String modifier, String rawPath, String rawBody) {
        String path = substitute(rawPath);
        var request = given().accept("application/json");
        if (!"sem autenticação".equals(modifier) && !"público".equals(modifier)) {
            request.header("Authorization", "Bearer " + context.token);
        }
        if (rawBody != null) {
            request.contentType("application/json").body(substitute(rawBody));
        }
        context.response = request.when().request(method, context.baseUrl + path);
    }

    private String substitute(String input) {
        String result = input;
        for (var entry : context.values.entrySet()) {
            result = result.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return result;
    }
}
