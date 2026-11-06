package simulations.Scripts.Utilities;

public class PerformanceRunInfo {

    public static void logRunConfig() {

        int inputters = AppConfig.PerformanceConfig.INPUTTER_USERS;
        int checkers  = AppConfig.PerformanceConfig.CHECKER_USERS;
        int major  = AppConfig.PerformanceConfig.MAJOR_CREDITOR_USERS;
        int minor = AppConfig.PerformanceConfig.MINOR_CREDITOR_USERS;
        int pg  = AppConfig.PerformanceConfig.PG_USERS_CSV;
        int enforcement  = AppConfig.PerformanceConfig.ENFORCEMENT_USERS;
        int convert  = AppConfig.PerformanceConfig.CONVERT_ACCOUNT_USERS;
        int searchview  = AppConfig.PerformanceConfig.SEARCH_VIEW_USERS;

        int totalUsers = inputters + checkers + major + minor + pg + enforcement + convert + searchview;

        System.out.println("==============================================");
        System.out.println("         GATLING PERFORMANCE TEST CONFIG      ");
        System.out.println("==============================================");

        System.out.println("Inputter Users : " + inputters);
        System.out.println("Checker Users  : " + checkers);
        System.out.println("Major Creditor Users : " + major);
        System.out.println("Minor Creditor  Users : " + minor);
        System.out.println("PG Users  : " + pg);
        System.out.println("Enforcement Users : " + enforcement);
        System.out.println("Convert  Users : " + convert);
        System.out.println("Search Dependant Users  : " + searchview);
        System.out.println("----------------------------------------------");
        System.out.println("TOTAL USERS    : " + totalUsers);
        System.out.println("----------------------------------------------");

        System.out.println("Ramp Duration  : " + AppConfig.PerformanceConfig.getRampDuration());
        System.out.println("Ramp (minutes) : " + AppConfig.PerformanceConfig.getRampDuration().toMinutes());

        System.out.println("Simulation Duration  : " + AppConfig.PerformanceConfig.getSimulationDuration());
        System.out.println("Simulation (minutes) : " + AppConfig.PerformanceConfig.getSimulationDuration().toMinutes());

        System.out.println("==============================================");

        // Optional: Add to Gatling report metadata
        System.setProperty("gatling.simulation.description",
            "TotalUsers=" + totalUsers +
            ", Ramp=" + AppConfig.PerformanceConfig.getRampDuration().toMinutes() + "m" +
            ", Duration=" + AppConfig.PerformanceConfig.getSimulationDuration().toMinutes() + "m"
        );
    }
}
