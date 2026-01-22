package simulations.Scripts.ScenarioBuilder;

import simulations.Scripts.Scenario.OpalLogin.LoginScenario;
import simulations.Scripts.Scenario.OpalLogin.UserExistsScenario;
import simulations.Scripts.Utilities.Feeders;
import io.gatling.javaapi.core.*;

import static io.gatling.javaapi.core.CoreDsl.*;

public class UserExistsScenarioBuild {

    public static ScenarioBuilder build(String scenarioName, int durationInSeconds) {
        return scenario(scenarioName)
            .group("OPAL Login Requests")
            .on(
                exec(feed(Feeders.acceptorUsers()))
                .exec(LoginScenario.LoginRequest())

                // Keep the user alive for the duration of the simulation
                .during(durationInSeconds).on(
                    exec(UserExistsScenario.UserExistsRequest())
                    .pause(110) // pause between each keep-alive request
                )
            );
    }
}
