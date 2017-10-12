package com.gsobko.act.model;

import com.gsobko.act.db.Entity;

import java.math.BigDecimal;

public class Transfer implements Entity<String> {
    private final String  id;
    private final Long  from;
    private final Long  to;
    private final BigDecimal amount;

    public Transfer(String id, BigDecimal amount, Long from, Long to) {
        this.id = id;
        this.amount = amount;
        this.from = from;
        this.to = to;
    }

    @Override
    public String getKey() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getId() {
        return id;
    }

    public Long getFrom() {
        return from;
    }

    public Long getTo() {
        return to;
    }

    @Override
    public Transfer clone() {
        return new Transfer(id, amount, from, to);
    }
}
