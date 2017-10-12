package com.gsobko.act.rest;

import com.gsobko.act.AccountManager;
import com.gsobko.act.TransferManager;
import com.gsobko.act.TransferManagerImpl;
import com.gsobko.act.model.Account;
import com.gsobko.act.rest.model.CreateAccountRequest;
import com.gsobko.act.rest.model.TransferRequest;
import com.gsobko.act.rest.model.TransferResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Path("account")
public class AccountsEndpoint {

    private final AtomicLong sequence = new AtomicLong(1);

    private final AccountManager accountManager;

    public AccountsEndpoint(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Account> listAccounts() {
        return accountManager.allAccounts().stream().sorted(Comparator.comparing(Account::getKey)).collect(Collectors.toList());
    }


    // Remove this
    @POST
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Account createAccount(CreateAccountRequest request) {
        return accountManager.createAccount(request.getAmount());
    }


}
