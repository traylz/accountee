package com.gsobko.act.rest;

import com.gsobko.act.TransferManager;
import com.gsobko.act.model.Transfer;
import com.gsobko.act.rest.model.TransferRequest;
import com.gsobko.act.rest.model.TransferResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Path("transfer")
public class TransferEndpoint {

    private final TransferManager transferManager;

    public TransferEndpoint(TransferManager transferManager) {
        this.transferManager = transferManager;
    }

    @POST
    @Path("/perform")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public TransferResponse doTransfer(TransferRequest params) {

        String transferId = params.getTransferId();
        if (transferId == null) {
            transferId = UUID.randomUUID().toString();
        }
        transferManager.doTransfer(params.getFromId(), params.getToId(), params.getAmount(), transferId);
        return new TransferResponse(transferId, params.getFromId(), params.getToId(), params.getAmount());
    }

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Transfer> listTransfers() {
        return transferManager.allTransfers();
    }

}
