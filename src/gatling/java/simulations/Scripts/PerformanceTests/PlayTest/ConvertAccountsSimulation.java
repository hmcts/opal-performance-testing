package simulations.Scripts.PerformanceTests.PlayTest;

import simulations.Scripts.Utilities.AppConfig;
import simulations.Scripts.Utilities.HttpProtocolConfig;
import simulations.Scripts.ScenarioBuilder.R1B.ConvertAccountsScenarioBuild;
import io.gatling.javaapi.core.*;
import java.util.concurrent.atomic.AtomicInteger;

import static io.gatling.javaapi.core.CoreDsl.*;

public class ConvertAccountsSimulation extends Simulation {   

    public static AtomicInteger global400ErrorCounter = new AtomicInteger(0);
    private static final String OPAL_LOGIN_TEST = "Opal Manual Account Creation Test";

    @Override
    public void before() {
        System.out.println("Simulation starting...");
        System.out.println("User Count: " + AppConfig.PerformanceConfig.PG_USERS_CSV);
        System.out.println("Ramp Duration: " + AppConfig.PerformanceConfig.getRampDuration());
    }    



    public ConvertAccountsSimulation() {
        setUp(
            ConvertAccountsScenarioBuild.build(OPAL_LOGIN_TEST)
                .injectOpen(
                     rampUsers(AppConfig.PerformanceConfig.PG_USERS_CSV)
                .during(AppConfig.PerformanceConfig.getRampDuration()))
                .protocols(HttpProtocolConfig.build()))
                .maxDuration(AppConfig.PerformanceConfig.getSimulationDuration());        
            //    .assertions(AssertionsConfig.getCreateAccountAssertions());
    }

    
}
