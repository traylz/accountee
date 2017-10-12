package com.gsobko.act.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import javax.validation.constraints.NotNull;

public class AccounteeConfiguration extends Configuration {
    @NotNull
    private Long amountPrecision;

    private Boolean startWithInitialState = false;

    private String initialStatePath;

    @JsonProperty
    public Long getAmountPrecision() {
        return amountPrecision;
    }

    @JsonProperty
    public void setAmountPrecision(Long amountPrecision) {
        this.amountPrecision = amountPrecision;
    }

    @JsonProperty
    public Boolean getStartWithInitialState() {
        return startWithInitialState;
    }

    @JsonProperty
    public void setStartWithInitialState(Boolean startWithInitialState) {
        this.startWithInitialState = startWithInitialState;
    }

    @JsonProperty
    public String getInitialStateUri() {
        return initialStatePath;
    }

    @JsonProperty
    public void setInitialStatePath(String initialStatePath) {
        this.initialStatePath = initialStatePath;
    }
}
