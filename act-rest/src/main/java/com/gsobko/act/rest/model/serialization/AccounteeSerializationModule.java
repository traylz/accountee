package com.gsobko.act.rest.model.serialization;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.gsobko.act.model.Account;

public final class AccounteeSerializationModule {
    private AccounteeSerializationModule() {}

    public static SimpleModule create() {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Account.class, new AccountSerializer());
        simpleModule.addDeserializer(Account.class, new AccountDeserializer());
        return simpleModule;
    }
}
