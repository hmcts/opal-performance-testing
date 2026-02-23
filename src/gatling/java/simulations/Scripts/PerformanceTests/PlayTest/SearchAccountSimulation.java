package simulations.Scripts.PerformanceTests.PlayTest;

import simulations.Scripts.Utilities.AppConfig;
import simulations.Scripts.Utilities.HttpProtocolConfig;
import simulations.Scripts.ScenarioBuilder.SearchAccountScenarioBuild;
import io.gatling.javaapi.core.*;

import static io.gatling.javaapi.core.CoreDsl.*;

public class SearchAccountSimulation extends Simulation {   

    @Override
    public void before() {
        System.out.println("Simulation starting...");
        System.out.println("User Count: " + AppConfig.PerformanceConfig.EXISTING_USERS);
        System.out.println("Ramp Duration: " + AppConfig.PerformanceConfig.getRampDuration());
    }    

    public SearchAccountSimulation() {

        setUp(
            SearchAccountScenarioBuild.build("Search Account Test")
                .injectOpen(
                    rampUsers(AppConfig.PerformanceConfig.CHECKER_USERS)
                    .during(AppConfig.PerformanceConfig.getRampDuration())
                )
                .protocols(HttpProtocolConfig.build())
        )
        .assertions(
            global().responseTime().max().lt(60000)
        );
    } 
}
