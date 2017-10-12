package com.gsobko.act.cucumber;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        glue = "com.gsobko.act.cucumber.steps",
        features = "classpath:features"
)
public class FunctionalPackRunner {
}

