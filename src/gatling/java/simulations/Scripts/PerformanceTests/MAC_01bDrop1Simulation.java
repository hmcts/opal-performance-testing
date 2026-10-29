package simulations.Scripts.PerformanceTests;

import simulations.Scripts.Utilities.AccountCounters;
import simulations.Scripts.Utilities.AppConfig;
import simulations.Scripts.Utilities.HttpProtocolConfig;
import simulations.Scripts.Utilities.PerformanceRunInfo;
import simulations.Scripts.Utilities.SimulationNames;
import simulations.Scripts.ScenarioBuilder.R1B.ConvertAccountsScenarioBuild;
import simulations.Scripts.ScenarioBuilder.R1B.ParentGuardianAccountSearchScenarioBuild;
import simulations.Scripts.ScenarioBuilder.R1B.R1BAmendingEnforcementsToAccountsBuild;
import simulations.Scripts.ScenarioBuilder.R1B.R1bSearchandViewDefendantScenarioBuild;
import io.gatling.javaapi.core.*;

import java.util.concurrent.atomic.AtomicInteger;

import static io.gatling.javaapi.core.CoreDsl.*;

public class MAC_01bDrop1Simulation extends Simulation {


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
        
        int paymentChanged =
            AccountCounters.PAYMENT_CHANGE_ADDED.get();

        System.out.println(
            "Added:   " + enforcementAdded
        );

        System.out.println(
            "Removed: " + enforcementRemoved
        );

        System.out.println(
            "Changed: " + enforcementChanged
        );

        System.out.println(
            "Payment Changed: " + paymentChanged
        );

        System.out.println("----------------------------------------");

        System.out.println(
            "Total:   "
            + (
                enforcementAdded
                + enforcementRemoved
                + enforcementChanged
                + paymentChanged

            )
        );

        System.out.println("========================================");

        System.out.println("========================================");

        System.out.println();
        System.out.println("========================================");
        System.out.println("SEARCH DEFENDANT ACCOUNT SUMMARY");
        System.out.println("========================================");

        System.out.println("Searched: "
            + AccountCounters.SEARCH_ACCOUNT.get());
        
        System.out.println("Searched Fixed: "
            + AccountCounters.FIXED_ACCOUNT.get());
        System.out.println("----------------------------------------");

    }
       

    public MAC_01bDrop1Simulation() {

        // Configure the simulation setup with multiple user scenarios
        setUp(           
 
            R1bSearchandViewDefendantScenarioBuild.build(SimulationNames.MAC_01B_TEST + " - Search and View accounts")
                .injectOpen(
                    rampUsers(AppConfig.PerformanceConfig.SEARCH_VIEW_USERS)
                        .during(AppConfig.PerformanceConfig.getRampDuration())
                ),

            ParentGuardianAccountSearchScenarioBuild.build(SimulationNames.MAC_01B_TEST + " - Search for Youth accounts")
                .injectOpen(
                    rampUsers(AppConfig.PerformanceConfig.PG_USERS_CSV)
                        .during(AppConfig.PerformanceConfig.getRampDuration())
                ),

            R1BAmendingEnforcementsToAccountsBuild.build(SimulationNames.MAC_01B_TEST + " - Add, remove and amend accounts with enforcements")
                .injectOpen(
                    rampUsers(AppConfig.PerformanceConfig.ENFORCEMENT_USERS)
                        .during(AppConfig.PerformanceConfig.getRampDuration())
                ),

            ConvertAccountsScenarioBuild.build(SimulationNames.MAC_01B_TEST + " - Convert Company and Defendant accounts")
                .injectOpen(
                    rampUsers(AppConfig.PerformanceConfig.CONVERT_ACCOUNT_USERS)
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
