package com.gsobko.act;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Stage;
import com.gsobko.act.config.AccounteeConfiguration;
import com.gsobko.act.config.AccounteeCoreModule;
import com.gsobko.act.config.DatabaseHealhcheck;
import com.gsobko.act.config.DatabaseModule;
import com.gsobko.act.rest.AccountsEndpoint;
import com.gsobko.act.rest.RuntimeExceptionMapper;
import com.gsobko.act.rest.TransferEndpoint;
import com.gsobko.act.rest.model.serialization.AccounteeSerializationModule;
import io.dropwizard.Application;
import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import ru.vyarus.dropwizard.guice.GuiceBundle;
import ru.vyarus.dropwizard.guice.module.installer.feature.health.HealthCheckInstaller;

public class AccounteeRestApp extends Application<AccounteeConfiguration> {

    private Environment environment;
    private final String configFile;

    public AccounteeRestApp() {
        this("accountee.yaml");
    }

    public AccounteeRestApp(String configFile) {
        this.configFile = configFile;
    }


    @Override
    public void initialize(Bootstrap<AccounteeConfiguration> bootstrap) {
        bootstrap.setConfigurationSourceProvider(new ResourceConfigurationSourceProvider());
        bootstrap.addBundle(GuiceBundle.<AccounteeConfiguration>builder()
                .modules(new DatabaseModule(), new AccounteeCoreModule())
                .extensions(AccountsEndpoint.class, TransferEndpoint.class,
                        RuntimeExceptionMapper.class, DatabaseHealhcheck.class)
                .build(Stage.PRODUCTION));
    }

    @Override
    public void run(AccounteeConfiguration accounteeConfiguration, Environment environment) throws Exception {
        ObjectMapper objectMapper = environment.getObjectMapper();
        objectMapper.registerModule(AccounteeSerializationModule.create());
        this.environment = environment;
    }

    public void runAsServer() throws Exception {
        run("server", configFile);
    }

    public void stop() throws Exception {
        environment.getApplicationContext().getServer().stop();
    }
}
