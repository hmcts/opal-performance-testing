package simulations.Scripts.ScenarioBuilder;


import simulations.Scripts.Scenario.Login.LoginScenario;
import simulations.Scripts.Scenario.SearchAccounts.SearchAccountScenario;
import simulations.Scripts.Utilities.Feeders;
import io.gatling.javaapi.core.*;

import static io.gatling.javaapi.core.CoreDsl.*;

public class SearchAccountScenarioBuild {

    public static ScenarioBuilder build(String scenarioName) {
        return scenario(scenarioName)
            .group("Search Account Workflow")
             .on(
                exec(exec(feed(Feeders.checkerUsers()))
                .exec(LoginScenario.LoginRequest())
                .exec(SearchAccountScenario.SearchAccountRequest())

            ));
    }
}

