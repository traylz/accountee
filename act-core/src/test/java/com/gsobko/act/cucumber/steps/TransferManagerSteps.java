package com.gsobko.act.cucumber.steps;

import com.google.inject.Inject;
import com.gsobko.act.TransferManager;
import com.gsobko.act.cucumber.TestScenarioScope;
import com.gsobko.act.model.Transfer;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class TransferManagerSteps {
    @Inject
    private TransferManager transferManager;

    @Inject
    private TestScenarioScope testScenarioScope;

    @When("^Transfer from account #(\\d+) to account #(\\d+) for amount ([\\d\\.-]+) is performed(?: with transferId=\"([^\"]*)\")?$")
    public void transferFromAccountToAccountForAmountIsPerformedWithTransferId(long fromId, long toId, BigDecimal amount,
                                                                               String transferId) throws Throwable {
        try {
            Transfer transfer = transferManager.doTransfer(fromId, toId, amount, transferId);
            testScenarioScope.setTransferException(null);
        } catch (Exception e) {
            testScenarioScope.setTransferException(e);
        }
    }

    @Then("^Transfer should be performed successfully$")
    public void transferIsOk() throws Throwable {
        assertNull("Transfer method shouldn't throw exceptions", testScenarioScope.getTransferException());
    }

    @Then("^Transfer should fail with exception ([\\w\\.]+) and message:$")
    public void transferShouldFailWithExceptionActUserExceptionAndMessage(Class<?> clazz, String message) throws Throwable {
        Exception transferException = testScenarioScope.getTransferException();
        assertNotNull("Transfer should fail", transferException);
        assertEquals(clazz, transferException.getClass());
        assertEquals(message, transferException.getMessage());
    }
}
