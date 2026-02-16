package simulations.Scripts.ScenarioBuilder;


import simulations.Scripts.Scenario.OpalLogin.LoginScenario;
import simulations.Scripts.Scenario.OpalLogin.LoginScenarioCreate;
import simulations.Scripts.Utilities.Feeders;
import io.gatling.javaapi.core.*;

import static io.gatling.javaapi.core.CoreDsl.*;

public class LoginScenarioBuild {

    public static ScenarioBuilder build(String scenarioName) {
        return scenario(scenarioName)
            .group("OPAL Login Requests")
            .on(
                exec(feed(Feeders.createUsers())
                    .exec(LoginScenarioCreate.LoginRequest())

                    //.exec(LoginScenario.LoginRequest())
                )
            );
    }
}
