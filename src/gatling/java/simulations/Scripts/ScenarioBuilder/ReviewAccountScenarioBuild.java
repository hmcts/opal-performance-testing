package simulations.Scripts.ScenarioBuilder;


import simulations.Scripts.Scenario.OpalLogin.ApproveAccountScenario;
import simulations.Scripts.Scenario.OpalLogin.LoginScenario;
import simulations.Scripts.Utilities.Feeders;
import io.gatling.javaapi.core.*;

import static io.gatling.javaapi.core.CoreDsl.*;

public class ReviewAccountScenarioBuild {

    public static ScenarioBuilder build(String scenarioName) {
        return scenario(scenarioName)
            .group("OPAL Login Requests")
            .on(
                exec(feed(Feeders.acceptorUsers())
                    .exec(LoginScenario.LoginRequest())                    
                    .exec(ApproveAccountScenario.ApproveAccountRequest())
                )
            );
    }
}
