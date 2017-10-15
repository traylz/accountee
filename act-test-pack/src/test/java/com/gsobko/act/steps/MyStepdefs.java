package com.gsobko.act.steps;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.gsobko.act.model.Account;
import com.gsobko.act.model.Transfer;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.commons.lang3.text.StrSubstitutor;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class MyStepdefs {

    @Inject
    private MyScenarioScope scenarioScope;

    @Inject
    @Named("baseUrl")
    private String baseUrl;


    @Given("^account (\\w+) with initial amount (\\d+(?:\\.\\d+)?) is created$")
    public void userCreatesAccount(String alias, BigDecimal amount) throws UnirestException {
        Account createAccountRequest = new Account(null, amount);
        HttpResponse<Account> accountHttpResponse = Unirest.post(baseUrl + "/account/create")
                .body(createAccountRequest)
                .asObject(Account.class);
        assertEquals(200, accountHttpResponse.getStatus());
        Account createdAccount = accountHttpResponse.getBody();
        assertEquals(amount, createdAccount.getAmount());
        scenarioScope.registerAlias(alias, createdAccount);
    }


    @When("^user calls POST ([\\w/]+) with following body:$")
    public void userCallsPOSTTransferPerformWithFollowingBody(String endpoint, String json) throws Throwable {
        HttpResponse<String> response = Unirest.post(baseUrl + endpoint)
                .body(preprocess(json))
                .asString();
        scenarioScope.registerResponse(response);
    }


    @When("^user generates unique id (\\w+)$")
    public void userCallsPOSTTransferPerformWithFollowingBody(String alias) throws Throwable {
        scenarioScope.registerToken(alias, UUID.randomUUID().toString());
    }


    @Then("^response should have status (\\d+)$")
    public void responseShouldHaveStatus(int status) throws Throwable {
        assertEquals(status, scenarioScope.getResponse().getStatus());
    }

    @Then("^response should have body:$")
    public void responseShouldHaveBody(String body) throws Throwable {
        body = preprocess(body);
        assertEquals(body, scenarioScope.getResponse().getBody());
    }

    @And("^account (\\w+) should have balance = (\\d+)$")
    public void accountAccShouldHaveBalance(String alias, BigDecimal amount) throws Throwable {
        HttpResponse<Account> accountHttpResponse =
                Unirest.get(baseUrl + "/account/" + scenarioScope.getByAlias(alias).getKey())
                        .asObject(Account.class);
        Account foundAccount = accountHttpResponse.getBody();
        assertEquals(amount, foundAccount.getAmount());
    }

    private String preprocess(String json) {
        return new StrSubstitutor(scenarioScope.getTokens()).replace(json);
    }

}
