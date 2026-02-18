package simulations.Scripts.ScenarioBuilder;


import simulations.Scripts.Scenario.OpalLogin.LoginScenario;
import simulations.Scripts.Scenario.OpalLogin.UserExistsScenario;
import simulations.Scripts.Utilities.Feeders;
import io.gatling.javaapi.core.*;

import static io.gatling.javaapi.core.CoreDsl.*;

public class ExistingUsersScenarioBuild {

    public static ScenarioBuilder build(String scenarioName) {
        return scenario(scenarioName)
            .group("Existing User Workflow")
            .on(
                exec(feed(Feeders.createUsers()))
                .exec(LoginScenario.LoginRequest())

                // Loop forever until the simulation stops
                .forever().on(
                    exec(UserExistsScenario.UserExistsRequest())
                    .pause(110)
                )
            );
    }
}

