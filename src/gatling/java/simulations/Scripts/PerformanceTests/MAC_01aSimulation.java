package simulations.Scripts.PerformanceTests;

import simulations.Scripts.Utilities.AppConfig;
import simulations.Scripts.Utilities.AssertionsConfig;
import simulations.Scripts.Utilities.HttpProtocolConfig;
import simulations.Scripts.Utilities.PerformanceRunInfo;
import simulations.Scripts.Utilities.SimulationNames;
import simulations.Scripts.ScenarioBuilder.CheckerUsersScenarioBuild;
import simulations.Scripts.ScenarioBuilder.ExistingUsersScenarioBuild;
import simulations.Scripts.ScenarioBuilder.InputterUsersScenarioBuild;
import io.gatling.javaapi.core.*;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import static io.gatling.javaapi.core.CoreDsl.*;

public class MAC_01aSimulation extends Simulation {


    public static AtomicInteger global400ErrorCounter = new AtomicInteger(0);

    @Override
    public void before() {
        System.out.println("Simulation starting...");
        PerformanceRunInfo.logRunConfig();
    }


    public MAC_01aSimulation() {

        // Configure the simulation setup with multiple user scenarios
        setUp(

            // ===================== Inputter Users Scenario =====================
            // Builds the Inputter scenario and applies an open workload model
            // Users ramp up from 0 to INPUTTER_USERS over the configured ramp duration
            InputterUsersScenarioBuild.build(SimulationNames.MAC_01A_TEST + " - Inputter")
                .injectOpen(
                    rampUsers(AppConfig.PerformanceConfig.INPUTTER_USERS)
                        .during(AppConfig.PerformanceConfig.getRampDuration())
                ),

            // ===================== Checker Users Scenario =====================
            // Builds the Checker scenario and ramps up checker users
            CheckerUsersScenarioBuild.build(SimulationNames.MAC_01A_TEST + " - Checker")
                .injectOpen(
                    rampUsers(AppConfig.PerformanceConfig.CHECKER_USERS)
                        .during(AppConfig.PerformanceConfig.getRampDuration())

                ),

            // ===================== Existing Users Scenario =====================
            // Builds a scenario for existing users and passes the total simulation duration
            ExistingUsersScenarioBuild.build(SimulationNames.MAC_01A_TEST + " - Existing User")
                .injectOpen(
                    rampUsers(AppConfig.PerformanceConfig.EXISTING_USERS)
                        .during(AppConfig.PerformanceConfig.getRampDuration())
                )
        )
        // ===================== Duration Configuration =====================
        // Applies simulation max duration in seconds.
        .maxDuration(AppConfig.PerformanceConfig.getSimulationDuration())

        // ===================== HTTP Protocol Configuration =====================
        // Applies shared HTTP protocol settings (base URL, headers, etc.)
        .protocols(HttpProtocolConfig.build())

        //test

        // ===================== Assertions =====================
        // Adds performance assertions specific to MAC_01A (e.g. response time, error rate)
        .assertions(AssertionsConfig.getMac01Assertions());
    }
}
