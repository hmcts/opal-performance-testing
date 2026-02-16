package simulations.Scripts.PerformanceTests;

import simulations.Scripts.Utilities.AppConfig;
import simulations.Scripts.Utilities.AssertionsConfig;
import simulations.Scripts.Utilities.HttpProtocolConfig;
import simulations.Scripts.ScenarioBuilder.CreateAccountScenarioBuild;
import io.gatling.javaapi.core.*;
import java.util.concurrent.atomic.AtomicInteger;

import static io.gatling.javaapi.core.CoreDsl.*;

public class CreateAccountSimulation extends Simulation {   

    public static AtomicInteger global400ErrorCounter = new AtomicInteger(0);
    private static final String OPAL_LOGIN_TEST = "Opal Manual Account Creation Test";

    @Override
    public void before() {
        System.out.println("Simulation starting...");
        System.out.println("User Count: " + AppConfig.PerformanceConfig.INPUTTER_USERS);
        System.out.println("Ramp Duration: " + AppConfig.PerformanceConfig.getRampDuration());
    }    
// 2 and 6 simple
// 5 and 15 complex


    public CreateAccountSimulation() {
        setUp(
            CreateAccountScenarioBuild.build(OPAL_LOGIN_TEST)
                .injectOpen(
                     rampUsers(AppConfig.PerformanceConfig.INPUTTER_USERS)
                .during(AppConfig.PerformanceConfig.getRampDuration()))
                .protocols(HttpProtocolConfig.build()))           
                .assertions(AssertionsConfig.getCreateAccountAssertions());
    } 
}
