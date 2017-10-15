package com.gsobko.act.rest.model.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.gsobko.act.model.Account;

import java.io.IOException;

class AccountSerializer extends StdSerializer<Account> {

    AccountSerializer() {
        super(Account.class);
    }

    @Override
    public void serialize(Account account, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException {
        jgen.writeStartObject();
        if (account.getKey() != null) {
            jgen.writeNumberField("id", account.getKey());
        }
        if (account.getAmount() != null) {
            jgen.writeNumberField("amount", account.getAmount());
        }
        jgen.writeEndObject();
    }

}