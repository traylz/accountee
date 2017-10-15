package com.gsobko.act.rest;

import com.google.inject.Inject;
import com.gsobko.act.AccountManager;
import com.gsobko.act.model.Account;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Path("account")
public class AccountsEndpoint {

    private final AtomicLong sequence = new AtomicLong(1);

    private final AccountManager accountManager;

    @Inject
    public AccountsEndpoint(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Account> listAccounts() {
        return accountManager.allAccounts().stream().sorted(Comparator.comparing(Account::getKey)).collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    public Response getAccount(@PathParam("id") Long id) {
        Optional<Account> account = accountManager.findAccount(id);
        if (!account.isPresent()) {
            return Response.status(404).entity("No account found for id = " + id).build();
        }
        return Response.ok(account.get(), MediaType.APPLICATION_JSON_TYPE).build();
    }


    @POST
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    public Account createAccount(Account request) {
        return accountManager.createAccount(request.getAmount());
    }


}
