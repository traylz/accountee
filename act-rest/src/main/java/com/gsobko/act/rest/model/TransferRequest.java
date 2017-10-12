package com.gsobko.act.rest.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class TransferRequest {
    final String transferId;
    final Long fromId;
    final Long toId;
    final BigDecimal amount;

    @JsonCreator
    public TransferRequest(@JsonProperty("transferId") String transferId,
                           @JsonProperty("fromId") Long fromId,
                           @JsonProperty("toId") Long toId,
                           @JsonProperty("amount") BigDecimal amount) {
        this.transferId = transferId;
        this.fromId = fromId;
        this.toId = toId;
        this.amount = amount;
    }

    public String getTransferId() {
        return transferId;
    }

    public Long getFromId() {
        return fromId;
    }

    public Long getToId() {
        return toId;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
