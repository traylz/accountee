package com.gsobko.act.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class TransferResponse {
    final String transferId;
    final Long fromId;
    final Long toId;
    final BigDecimal amount;

    public TransferResponse(String transferId, Long fromId, Long toId, BigDecimal amount) {
        this.transferId = transferId;
        this.fromId = fromId;
        this.toId = toId;
        this.amount = amount;
    }

    @JsonProperty
    public String getTransferId() {
        return transferId;
    }

    @JsonProperty
    public Long getFromId() {
        return fromId;
    }

    @JsonProperty
    public Long getToId() {
        return toId;
    }

    @JsonProperty
    public BigDecimal getAmount() {
        return amount;
    }
}
