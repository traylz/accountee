package com.gsobko.act.injector;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import com.gsobko.act.rest.model.serialization.AccounteeSerializationModule;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import cucumber.api.guice.CucumberModules;
import cucumber.runtime.java.guice.InjectorSource;

import java.io.IOException;

public class MyInjectorSource implements InjectorSource {

    @Override
    public Injector getInjector() {
        setupUnirest();
        return Guice.createInjector(Stage.PRODUCTION, CucumberModules.SCENARIO, new TestModule());

    }

    private void setupUnirest() {
        com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
        jacksonObjectMapper.registerModule(AccounteeSerializationModule.create());
        Unirest.setObjectMapper(new UnirestObjectMapper(jacksonObjectMapper));
        Unirest.setDefaultHeader("accept", "application/json");
        Unirest.setDefaultHeader("Content-Type", "application/json");
    }

    private static final class UnirestObjectMapper implements ObjectMapper {
        private final com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper;

        private UnirestObjectMapper(com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper) {
            this.jacksonObjectMapper = jacksonObjectMapper;
        }

        @Override
        public <T> T readValue(String value, Class<T> valueType) {
            try {
                return jacksonObjectMapper.readValue(value, valueType);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public String writeValue(Object value) {
            try {
                return jacksonObjectMapper.writeValueAsString(value);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
