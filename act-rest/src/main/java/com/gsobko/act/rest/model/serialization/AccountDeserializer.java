package com.gsobko.act.rest.model.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.gsobko.act.model.Account;

import java.io.IOException;
import java.math.BigDecimal;

class AccountDeserializer extends StdDeserializer<Account> {

    AccountDeserializer() {
        super(Account.class);
    }

    @Override
    public Account deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        String amount = node.get("amount").asText();
        if (node.has("id")) {
            JsonNode key = node.get("id");
            Long id = key.isNull() ? null : key.asLong();
            return new Account(id, new BigDecimal(amount));
        } else {
            return new Account(null, new BigDecimal(amount));
        }
    }
}