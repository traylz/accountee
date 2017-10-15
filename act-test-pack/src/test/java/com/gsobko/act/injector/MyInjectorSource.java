package com.gsobko.act.injector;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Throwables;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import com.gsobko.act.rest.model.serialization.AccounteeSerializationModule;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import cucumber.api.guice.CucumberModules;
import cucumber.runtime.java.guice.InjectorSource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MyInjectorSource implements InjectorSource {

    private static final String PROPS_NAME = "/testkit.properties";

    @Override
    public Injector getInjector() {
        setupUnirest();

        Properties prop = loadTestProperties();

        return Guice.createInjector(Stage.PRODUCTION, CucumberModules.SCENARIO, new TestModule(prop));

    }

    private void setupUnirest() {
        com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
        jacksonObjectMapper.registerModule(AccounteeSerializationModule.create());
        Unirest.setObjectMapper(new UnirestObjectMapper(jacksonObjectMapper));
        Unirest.setDefaultHeader("accept", "application/json");
        Unirest.setDefaultHeader("Content-Type", "application/json");
    }

    private static Properties loadTestProperties() {
        Properties prop = new Properties();
        try (InputStream inputStream = MyInjectorSource.class.getResourceAsStream(PROPS_NAME)) {

            prop.load(inputStream);
            return prop;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
