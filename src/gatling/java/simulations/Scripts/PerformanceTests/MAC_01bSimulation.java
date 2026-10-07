package simulations.Scripts.PerformanceTests;

import simulations.Scripts.Utilities.AccountCounters;
import simulations.Scripts.Utilities.AppConfig;
import simulations.Scripts.Utilities.AssertionsConfig;
import simulations.Scripts.Utilities.HttpProtocolConfig;
import simulations.Scripts.Utilities.PerformanceRunInfo;
import simulations.Scripts.Utilities.SimulationNames;
import simulations.Scripts.ScenarioBuilder.R1A.CheckerUsersScenarioBuild;
import simulations.Scripts.ScenarioBuilder.R1A.InputterUsersScenarioBuild;
import simulations.Scripts.ScenarioBuilder.R1B.ParentGuardianAccountSearchScenarioBuild;
import simulations.Scripts.ScenarioBuilder.R1B.R1BAmendingEnforcementsToAccountsBuild;
import simulations.Scripts.ScenarioBuilder.R1B.R1bMajorCreditorSearchBuild;
import simulations.Scripts.ScenarioBuilder.R1B.R1bMinorCreditorSearchBuild;
import simulations.Scripts.ScenarioBuilder.R1B.R1bSearchandViewDefendantScenarioBuild;
import io.gatling.javaapi.core.*;

import java.util.concurrent.atomic.AtomicInteger;

import static io.gatling.javaapi.core.CoreDsl.*;

public class MAC_01bSimulation extends Simulation {


    public static AtomicInteger global400ErrorCounter = new AtomicInteger(0);

    @Override
    public void before() {
        System.out.println("Simulation starting...");
        PerformanceRunInfo.logRunConfig();
    }
   
    @Override
    public void after() {

        System.out.println();
        System.out.println("========================================");
        System.out.println("ACCOUNT CREATION SUMMARY");
        System.out.println("========================================");

        System.out.println("Total Accounts Created: "
            + AccountCounters.TOTAL_CREATED.get());

        System.out.println();

        System.out.println("Fixed Accounts Created: "
            + AccountCounters.FIXED_CREATED.get());

        System.out.println();

        System.out.println("Fine Accounts Created: "
            + AccountCounters.FINE_CREATED.get());

        System.out.println();

        System.out.println("Conditional Accounts Created: "
            + AccountCounters.CONDITIONAL_CREATED.get());

        System.out.println();

        System.out.println("Approved Accounts: "
            + AccountCounters.APPROVED.get());

        System.out.println();

        System.out.println("Rejected Accounts: "
            + AccountCounters.REJECTED.get());

        System.out.println();
        System.out.println("========================================");
        System.out.println("PARENT & GUARDIAN ACCOUNT SUMMARY");
        System.out.println("========================================");

        System.out.println("Added:   "
            + AccountCounters.PG_ADDED.get());

        System.out.println("Removed: "
            + AccountCounters.PG_REMOVED.get());

        System.out.println("Changed: "
            + AccountCounters.PG_CHANGED.get());

        System.out.println("----------------------------------------");

        System.out.println("Total:   "
            + (
                AccountCounters.PG_ADDED.get()
                + AccountCounters.PG_REMOVED.get()
                + AccountCounters.PG_CHANGED.get()
            ));

        System.out.println("========================================");

        System.out.println();

        System.out.println("========================================");
        System.out.println("ENFORCEMENT ACTION SUMMARY");
        System.out.println("========================================");

        int enforcementAdded =
            AccountCounters.ENFORCEMENT_ADDED.get();

        int enforcementRemoved =
            AccountCounters.ENFORCEMENT_REMOVED.get();

        int enforcementChanged =
            AccountCounters.ENFORCEMENT_CHANGED.get();

        System.out.println(
            "Added:   " + enforcementAdded
        );

        System.out.println(
            "Removed: " + enforcementRemoved
        );

        System.out.println(
            "Changed: " + enforcementChanged
        );

        System.out.println("----------------------------------------");

        System.out.println(
            "Total:   "
            + (
                enforcementAdded
                + enforcementRemoved
                + enforcementChanged
            )
        );

        System.out.println("========================================");

        System.out.println();

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

    public MAC_01bSimulation() {

        // Configure the simulation setup with multiple user scenarios
        setUp(

            // ===================== Inputter Users Scenario =====================
            // Builds the Inputter scenario and applies an open workload model
            // Users ramp up from 0 to INPUTTER_USERS over the configured ramp duration
            // InputterUsersScenarioBuild.build(SimulationNames.MAC_01A_TEST + " - Created fixed, fine and conditonal accounts")
            //     .injectOpen(
            //         rampUsers(AppConfig.PerformanceConfig.INPUTTER_USERS)
            //             .during(AppConfig.PerformanceConfig.getRampDuration())
            //     ),

            // // ===================== Checker Users Scenario =====================
            // //Builds the Checker scenario and ramps up checker users
            // CheckerUsersScenarioBuild.build(SimulationNames.MAC_01A_TEST + " - Approve and reject accounts")
            //     .injectOpen(
            //         rampUsers(AppConfig.PerformanceConfig.CHECKER_USERS)
            //             .during(AppConfig.PerformanceConfig.getRampDuration())

            //     ),
 
            // R1bSearchandViewDefendantScenarioBuild.build(SimulationNames.MAC_01B_TEST + " - Search and View accounts")
            //     .injectOpen(
            //         rampUsers(AppConfig.PerformanceConfig.SEARCH_VIEW_USERS)
            //             .during(AppConfig.PerformanceConfig.getRampDuration())
            //     ),

            // ParentGuardianAccountSearchScenarioBuild.build(SimulationNames.MAC_01B_TEST + " - Search for Youth accounts with, without Parent and Guardian")
            //     .injectOpen(
            //         rampUsers(AppConfig.PerformanceConfig.PG_USERS_CSV)
            //             .during(AppConfig.PerformanceConfig.getRampDuration())
            //     ),

            // R1BAmendingEnforcementsToAccountsBuild.build(SimulationNames.MAC_01B_TEST + " - Add, remove and amend accounts with enforcements")
            //     .injectOpen(
            //         rampUsers(AppConfig.PerformanceConfig.ENFORCEMENT_USERS)
            //             .during(AppConfig.PerformanceConfig.getRampDuration())
            //     ),
            
            // R1bMinorCreditorSearchBuild.build(SimulationNames.MAC_01B_TEST + " - Search for minor creditor accounts")
            //     .injectOpen(
            //         rampUsers(AppConfig.PerformanceConfig.MINOR_CREDITOR_USERS)
            //             .during(AppConfig.PerformanceConfig.getRampDuration())
            //     ),
            
            R1bMajorCreditorSearchBuild.build(SimulationNames.MAC_01B_TEST + " - Search for major creditor accounts")
                .injectOpen(
                    rampUsers(AppConfig.PerformanceConfig.MAJOR_CREDITOR_USERS)
                        .during(AppConfig.PerformanceConfig.getRampDuration())
                )
         
            // R1bMajorCreditorSearchBuild.build(SimulationNames.MAC_01B_TEST + " - Search for major creditor accounts")
            //     .injectOpen(
            //         rampUsers(AppConfig.PerformanceConfig.MAJOR_CREDITOR_USERS)
            //             .during(AppConfig.PerformanceConfig.getRampDuration())
            //     )
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
