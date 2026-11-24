package simulations.Scripts.ScenarioBuilder.R1B;

import io.gatling.javaapi.core.ScenarioBuilder;
import simulations.Scripts.Scenario.Login.LoginScenario;
import simulations.Scripts.Scenario.SearchAccounts.R1bMajorCreditorSearchScenario;
import simulations.Scripts.Utilities.Feeders;

import static io.gatling.javaapi.core.CoreDsl.*;

public class R1bMajorCreditorSearchBuild {

    public static ScenarioBuilder build(String scenarioName) {
        return scenario(scenarioName)
            .group("Major Creditor Search and View")
            .on(
                exec(exec(feed(Feeders.majorCreditorUsers()))
                .exec(LoginScenario.LoginRequest())
                
                .forever().on(
                //.repeat(5).on(
                exec(R1bMajorCreditorSearchScenario.MajorCreditorSearch())
            .pause(40,120))
            )
        );
    }    
}