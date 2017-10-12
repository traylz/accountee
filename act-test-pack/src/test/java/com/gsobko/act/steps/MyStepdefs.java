package com.gsobko.act.steps;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.gsobko.act.db.Database;
import com.gsobko.act.model.Account;
import com.gsobko.act.model.Transfer;
import com.gsobko.act.rest.model.CreateAccountRequest;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sun.xml.internal.ws.transport.http.client.HttpClientTransport;
import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.message.BasicHttpRequest;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class MyStepdefs {


    @Inject
    private MyScenarioScope scenarioScope;

    @Inject
    @Named("baseUrl")
    private String baseUrl;


    @Given("^Creates account (\\w+) with initial amount (\\d+(?:\\.\\d+))$")
    public void userCreateAccount(String alias, BigDecimal amount) throws UnirestException {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest(amount);
        HttpResponse<Account> accountHttpResponse = Unirest.post(baseUrl + "/account/create")
                .body(createAccountRequest)
                .asObject(Account.class);
        assertEquals(200, accountHttpResponse.getStatus());
        accountHttpResponse.getBody();
    }
}
