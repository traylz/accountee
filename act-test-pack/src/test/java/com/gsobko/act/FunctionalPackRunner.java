package com.gsobko.act;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:features", glue={"com.gsobko.act.steps"}
)
public class FunctionalPackRunner {

    private static AccounteeRestApp accounteeRestApp;

    @BeforeClass
    public static void setUp() throws Exception {
        accounteeRestApp = new AccounteeRestApp();
        accounteeRestApp.runAsServer();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        accounteeRestApp.stop();
    }
}
