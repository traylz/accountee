package com.gsobko.act.injector;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.gsobko.act.AccounteeRestApp;
import com.gsobko.act.steps.MyScenarioScope;

import java.util.Properties;

import static cucumber.api.guice.CucumberScopes.SCENARIO;

class TestModule extends AbstractModule {

    private final Properties props;

    TestModule(Properties props) {

        this.props = props;
    }

    @Override
    protected void configure() {

        String url;
        if (props.getProperty("embedded", "true").equals("true")) {
            AccounteeRestApp accounteeRestApp = new AccounteeRestApp(props.getProperty("config"));
            try {
                accounteeRestApp.runAsServer();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            url = "http://localhost:" + accounteeRestApp.getPort() + "/";
        } else {
            url = props.getProperty("url");
        }


        bindConstant().annotatedWith(Names.named("baseUrl")).to(url);
        bind(MyScenarioScope.class).in(SCENARIO);
    }
}
