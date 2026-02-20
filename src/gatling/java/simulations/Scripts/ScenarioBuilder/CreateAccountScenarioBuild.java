package simulations.Scripts.ScenarioBuilder;


import simulations.Scripts.Scenario.OpalLogin.CreateAccountConditionalCautionScenario;
import simulations.Scripts.Scenario.OpalLogin.CreateAccountFineScenario;
import simulations.Scripts.Scenario.OpalLogin.CreateAccountFixedScenario;
import simulations.Scripts.Scenario.OpalLogin.LoginScenario;
import simulations.Scripts.Utilities.Feeders;
import io.gatling.javaapi.core.*;

import static io.gatling.javaapi.core.CoreDsl.*;

public class CreateAccountScenarioBuild {

    public static ScenarioBuilder build(String scenarioName) {
        return scenario(scenarioName)
            .group("OPAL Login Requests")
            .on(
                exec(feed(Feeders.inputterUsers())
                    .exec(LoginScenario.LoginRequest())
                    .repeat(10)
                    .on(exec(CreateAccountFineScenario.CreateAccountFineRequest()))
               //     .exec(CreateAccountFixedScenario.CreateAccountFixedRequest())
               //     .exec(CreateAccountFineScenario.CreateAccountFineRequest())
               //     .exec(CreateAccountConditionalCautionScenario.CreateAccountConditionalCautionRequest())
                )
            );
    }
}
