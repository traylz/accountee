package com.gsobko.act.injector;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.LongNode;
import com.gsobko.act.model.Account;

import java.io.IOException;
import java.math.BigDecimal;

public class AccountDeserializer extends StdDeserializer<Account> {

    public AccountDeserializer() {
        this(null);
    }

    public AccountDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Account deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        long id = node.get("key").longValue();
        String amount = node.get("amount").asText();

        return new Account(id, new BigDecimal(amount));
    }
}