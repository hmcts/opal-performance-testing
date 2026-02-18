package simulations.Scripts.Utilities;

public class PerformanceRunInfo {

    public static void logRunConfig() {

        int inputters = AppConfig.PerformanceConfig.INPUTTER_USERS;
        int checkers  = AppConfig.PerformanceConfig.CHECKER_USERS;
        int existing  = AppConfig.PerformanceConfig.EXISTING_USERS;

        int totalUsers = inputters + checkers + existing;

        System.out.println("==============================================");
        System.out.println("         GATLING PERFORMANCE TEST CONFIG      ");
        System.out.println("==============================================");

        System.out.println("Inputter Users : " + inputters);
        System.out.println("Checker Users  : " + checkers);
        System.out.println("Existing Users : " + existing);
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
