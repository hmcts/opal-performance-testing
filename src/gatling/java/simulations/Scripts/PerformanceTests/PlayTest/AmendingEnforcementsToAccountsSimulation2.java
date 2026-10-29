package simulations.Scripts.PerformanceTests.PlayTest;

import simulations.Scripts.ScenarioBuilder.R1B.R1BAmendingEnforcementsToAccountsBuild;
import simulations.Scripts.Utilities.AppConfig;
import simulations.Scripts.Utilities.HttpProtocolConfig;
import io.gatling.javaapi.core.*;
import java.util.concurrent.atomic.AtomicInteger;

import static io.gatling.javaapi.core.CoreDsl.*;

public class AmendingEnforcementsToAccountsSimulation2 extends Simulation {   

    public static AtomicInteger global400ErrorCounter = new AtomicInteger(0);
    private static final String OPAL_LOGIN_TEST = "Opal Manual Account Creation Test";

    @Override
    public void before() {
        System.out.println("Simulation starting...");
        System.out.println("User Count: " + AppConfig.PerformanceConfig.ENFORCEMENT_USERS);
        System.out.println("Ramp Duration: " + AppConfig.PerformanceConfig.getRampDuration());
    }   
   
      

    public AmendingEnforcementsToAccountsSimulation2() {
        setUp(
            R1BAmendingEnforcementsToAccountsBuild.build(OPAL_LOGIN_TEST)
                .injectOpen(
                     rampUsers(AppConfig.PerformanceConfig.ENFORCEMENT_USERS)
                .during(AppConfig.PerformanceConfig.getRampDuration()))
                .protocols(HttpProtocolConfig.build()))
                .maxDuration(AppConfig.PerformanceConfig.getSimulationDuration());      
            //    .assertions(AssertionsConfig.getCreateAccountAssertions());
    } 
}
