package simulations.Scripts.PerformanceTests;

import simulations.Scripts.Utilities.AppConfig;
import simulations.Scripts.Utilities.HttpProtocolConfig;
import simulations.Scripts.ScenarioBuilder.ExistingUsersScenarioBuild;
import io.gatling.javaapi.core.*;

import static io.gatling.javaapi.core.CoreDsl.*;

public class UserExistsSimulation extends Simulation {   

    @Override
    public void before() {
        System.out.println("Simulation starting...");
        System.out.println("User Count: " + AppConfig.PerformanceConfig.EXISTING_USERS);
        System.out.println("Ramp Duration: " + AppConfig.PerformanceConfig.getRampDuration());
    }    

    public UserExistsSimulation() {

        setUp(
            ExistingUsersScenarioBuild.build("User Exists Test")
                .injectOpen(
                    rampUsers(AppConfig.PerformanceConfig.EXISTING_USERS)
                    .during(AppConfig.PerformanceConfig.getRampDuration())
                )
                .protocols(HttpProtocolConfig.build())
        )
        .assertions(
            global().responseTime().max().lt(60000)
        );
    } 
}
