package com.gsobko.act.injector;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import com.gsobko.act.model.Account;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import cucumber.api.guice.CucumberModules;
import cucumber.runtime.java.guice.InjectorSource;

import java.io.IOException;

public class MyInjectorSource implements InjectorSource {

    @Override
    public Injector getInjector() {
        Unirest.setObjectMapper(new ObjectMapper() {
            private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper;

            {
                jacksonObjectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
                SimpleModule simpleModule = new SimpleModule();
                simpleModule.addDeserializer(Account.class, new AccountDeserializer());
                jacksonObjectMapper.registerModule(simpleModule);
            }

            public <T> T readValue(String value, Class<T> valueType) {
                try {
                    return jacksonObjectMapper.readValue(value, valueType);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            public String writeValue(Object value) {
                try {
                    return jacksonObjectMapper.writeValueAsString(value);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        return Guice.createInjector(Stage.PRODUCTION, CucumberModules.SCENARIO, new TestModule());

    }
}
