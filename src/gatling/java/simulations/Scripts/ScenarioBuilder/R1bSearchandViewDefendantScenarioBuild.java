package simulations.Scripts.ScenarioBuilder;


import simulations.Scripts.Scenario.Login.LoginScenario;
import simulations.Scripts.Scenario.SearchAccounts.R1bDefendantViewScenario;
import simulations.Scripts.Utilities.Feeders;
import io.gatling.javaapi.core.*;

import static io.gatling.javaapi.core.CoreDsl.*;

public class R1bSearchandViewDefendantScenarioBuild {

    public static ScenarioBuilder build(String scenarioName) {
        return scenario(scenarioName)
            .group("Defendant Search and View")
             .on(
                //MH Change this for the R1b users when they are set up! Should not be checkerUsers when run in anger!
                exec(exec(feed(Feeders.checkerUsers()))
                .exec(LoginScenario.LoginRequest())
                .forever().on(
                feed(csv("SearchViewNames.csv").random()) //MH here we feed in the names for the search, change as needed in varying scenarios
                .exec(R1bDefendantViewScenario.ViewDefendant())
            .pause(40,120))

            ));
    }
}