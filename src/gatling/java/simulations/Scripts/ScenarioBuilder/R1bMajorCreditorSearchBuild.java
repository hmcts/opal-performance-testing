package simulations.Scripts.ScenarioBuilder;

import io.gatling.javaapi.core.ChainBuilder;
import simulations.Scripts.Scenario.Login.LoginScenario;
import simulations.Scripts.Scenario.SearchAccounts.R1bMajorCreditorSearchScenario;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class R1bMajorCreditorSearchBuild {

    public static ScenarioBuilder build(String scenarioName) {
        return scenario(scenarioName)
            .group("Major Creditor Search and View")
             .on(
                //MH Change this for the R1b users when they are set up! Should not be checkerUsers when run in anger!
                exec(exec(feed(Feeders.checkerUsers()))
                .exec(LoginScenario.LoginRequest())
                .forever().on(
                .exec(R1bMajorCreditorSearchScenario.build())
            .pause(40,120))

            ));
    }

    
}