package simulations.Scripts.PerformanceTests.PlayTest;


import simulations.Scripts.Utilities.AppConfig;
import simulations.Scripts.Utilities.HttpProtocolConfig;
import simulations.Scripts.ScenarioBuilder.R1B.R1bMajorCreditorSearchBuild;
import io.gatling.javaapi.core.*;
import static io.gatling.javaapi.core.CoreDsl.*;

public class R1bSearchMajorCreditorSimulation extends Simulation {

    private static final String SCENARIO_NAME = "R1b Search and Amend Major Creditor";

    @Override
    public void before() {
        System.out.println("Simulation starting...");
        System.out.println("User Count: " + AppConfig.PerformanceConfig.CHECKER_USERS);
        System.out.println("Ramp Duration: " + AppConfig.PerformanceConfig.getRampDuration());
        System.out.println("Simulation Duration: " + AppConfig.PerformanceConfig.getSimulationDuration());
    }

    public R1bSearchMajorCreditorSimulation() {
        setUp(
            R1bMajorCreditorSearchBuild.build(SCENARIO_NAME)
                .injectOpen(
                    rampUsers(AppConfig.PerformanceConfig.MAJOR_CREDITOR_USERS)
                        .during(AppConfig.PerformanceConfig.getRampDuration())
                )
                .protocols(HttpProtocolConfig.build())
        ).maxDuration(AppConfig.PerformanceConfig.getSimulationDuration());
    }
}