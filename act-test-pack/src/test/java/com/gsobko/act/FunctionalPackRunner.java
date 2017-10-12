package com.gsobko.act;

import org.junit.runner.RunWith;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:features", glue={"com.gsobko.act.steps"}
)
public class FunctionalPackRunner {
}
