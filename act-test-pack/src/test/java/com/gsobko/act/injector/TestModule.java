package com.gsobko.act.injector;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.gsobko.act.steps.MyScenarioScope;
import cucumber.runtime.java.guice.ScenarioScope;
import cucumber.runtime.java.guice.impl.SequentialScenarioScope;

import static cucumber.api.guice.CucumberScopes.SCENARIO;

public class TestModule extends AbstractModule {

    @Override
    protected void configure() {
        bindConstant().annotatedWith(Names.named("baseUrl")).to("http://localhost:8080");
        bind(MyScenarioScope.class).in(SCENARIO);
    }
}
