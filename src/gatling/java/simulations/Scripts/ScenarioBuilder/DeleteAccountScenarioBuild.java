package simulations.Scripts.ScenarioBuilder;


import simulations.Scripts.Scenario.OpalLogin.DeleteAccountScenario;
import simulations.Scripts.Scenario.OpalLogin.LoginScenario;
import simulations.Scripts.Utilities.Feeders;
import io.gatling.javaapi.core.*;

import static io.gatling.javaapi.core.CoreDsl.*;

public class DeleteAccountScenarioBuild {

    public static ScenarioBuilder build(String scenarioName) {
        return scenario(scenarioName)
            .group("OPAL Delete Requests")
            .on(
                exec(feed(Feeders.checkerUsers())
                    .exec(LoginScenario.LoginRequest())
                    .exec(DeleteAccountScenario.DeleteAccountRequest())
                )
            );
    }
}
