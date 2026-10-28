package simulations.Scripts.PerformanceTests;

import simulations.Scripts.Utilities.AccountCounters;
import simulations.Scripts.Utilities.AppConfig;
import simulations.Scripts.Utilities.HttpProtocolConfig;
import simulations.Scripts.Utilities.PerformanceRunInfo;
import simulations.Scripts.Utilities.SimulationNames;
import simulations.Scripts.ScenarioBuilder.R1B.R1bMajorCreditorSearchBuild;
import simulations.Scripts.ScenarioBuilder.R1B.R1bMinorCreditorSearchBuild;
import io.gatling.javaapi.core.*;

import java.util.concurrent.atomic.AtomicInteger;

import static io.gatling.javaapi.core.CoreDsl.*;

public class MAC_01bDrop2Simulation extends Simulation {


    public static AtomicInteger global400ErrorCounter = new AtomicInteger(0);

    @Override
    public void before() {
        System.out.println("Simulation starting...");
        PerformanceRunInfo.logRunConfig();
    }
   
    @Override
    public void after() {  

        System.out.println("========================================");
        System.out.println("MINOR CREDITOR ACTION SUMMARY");
        System.out.println("========================================");

        int minorCreditorUpdated =
            AccountCounters.MINOR_CREDITOR_UPDATED.get();

        int minorCreditorChanged =
            AccountCounters.MINOR_CREDITOR_CHANGED.get();

        System.out.println(
            "Updated: " + minorCreditorUpdated
        );

        System.out.println(
            "Changed: " + minorCreditorChanged
        );

        System.out.println("----------------------------------------");

        System.out.println(
            "Total:   "
            + (
                minorCreditorUpdated
                + minorCreditorChanged
            )
        );

        System.out.println("========================================");       

    }
       

    public MAC_01bDrop2Simulation() {

        // Configure the simulation setup with multiple user scenarios
        setUp(

            R1bMinorCreditorSearchBuild.build(SimulationNames.MAC_01B_TEST + " - Search for minor creditor accounts")
                .injectOpen(
                    rampUsers(AppConfig.PerformanceConfig.MINOR_CREDITOR_USERS)
                        .during(AppConfig.PerformanceConfig.getRampDuration())
                ),
            
            R1bMajorCreditorSearchBuild.build(SimulationNames.MAC_01B_TEST + " - Search for major creditor accounts")
                .injectOpen(
                    rampUsers(AppConfig.PerformanceConfig.MAJOR_CREDITOR_USERS)
                        .during(AppConfig.PerformanceConfig.getRampDuration())
                )         

        )
        // ===================== Duration Configuration =====================
        // Applies simulation max duration in seconds.
        .maxDuration(AppConfig.PerformanceConfig.getSimulationDuration())

        // ===================== HTTP Protocol Configuration =====================
        // Applies shared HTTP protocol settings (base URL, headers, etc.)
        .protocols(HttpProtocolConfig.build());

        // ===================== Assertions =====================
        // Adds performance assertions specific to MAC_01A (e.g. response time, error rate)
    //    .assertions(AssertionsConfig.getMac01Assertions());        
    }
}
