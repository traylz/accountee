package com.gsobko.act.model;

import com.gsobko.act.db.Entity;

import java.math.BigDecimal;

public class Account implements Entity<Long> {
    private final Long id;
    private BigDecimal amount;

    public Account(Long id, BigDecimal amount) {
        this.id = id;
        this.amount = amount;
    }

    @Override
    public Long getKey() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public Account clone() {
        return new Account(id, amount);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;

        Account account = (Account) o;

        if (id != null ? !id.equals(account.id) : account.id != null) return false;
        return amount != null ? amount.equals(account.amount) : account.amount == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", amount=" + amount +
                '}';
    }
}
