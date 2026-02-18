package simulations.Scripts.PerformanceTests;

import simulations.Scripts.Utilities.AppConfig;
import simulations.Scripts.Utilities.AssertionsConfig;
import simulations.Scripts.Utilities.HttpProtocolConfig;
import simulations.Scripts.ScenarioBuilder.CheckerUsersScenarioBuild;
import simulations.Scripts.ScenarioBuilder.ExistingUsersScenarioBuild;
import simulations.Scripts.ScenarioBuilder.InputterUsersScenarioBuild;
import io.gatling.javaapi.core.*;

import java.util.concurrent.atomic.AtomicInteger;

import static io.gatling.javaapi.core.CoreDsl.*;

public class MAC_01aSimulation extends Simulation {

    public static AtomicInteger global400ErrorCounter = new AtomicInteger(0);
    private static final String MAC_01A_TEST = "MAC 01a Test";

    @Override
    public void before() {
        System.out.println("Simulation starting...");
        System.out.println("User Count: " + AppConfig.PerformanceConfig.EXISTING_USERS);
        System.out.println("Ramp Duration: " + AppConfig.PerformanceConfig.getRampDuration());
    }

    public MAC_01aSimulation() {
        setUp(
            InputterUsersScenarioBuild.build(MAC_01A_TEST + " - Inputter")
                .injectOpen(
                    rampUsers(AppConfig.PerformanceConfig.INPUTTER_USERS)
                        .during(AppConfig.PerformanceConfig.getRampDuration())
                ),

            CheckerUsersScenarioBuild.build(MAC_01A_TEST + " - Checker")
                .injectOpen(
                    rampUsers(AppConfig.PerformanceConfig.CHECKER_USERS)
                        .during(AppConfig.PerformanceConfig.getRampDuration())
                ),

            ExistingUsersScenarioBuild.build(
                    MAC_01A_TEST + " - Existing User",
                    AppConfig.PerformanceConfig.getSimulationDuration()
                )
                .injectOpen(
                    rampUsers(AppConfig.PerformanceConfig.EXISTING_USERS)
                        .during(AppConfig.PerformanceConfig.getRampDuration())
                )
            )
            .protocols(HttpProtocolConfig.build())
            .assertions(AssertionsConfig.getMac01Assertions());
    }
}
