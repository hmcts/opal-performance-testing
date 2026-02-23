package simulations.Scripts.ScenarioBuilder;



import simulations.Scripts.Scenario.CreateAccounts.CreateAccountConditionalCautionScenario;
import simulations.Scripts.Scenario.CreateAccounts.CreateAccountFineScenario;
import simulations.Scripts.Scenario.CreateAccounts.CreateAccountFixedScenario;
import simulations.Scripts.Scenario.Login.LoginScenario;
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
                    .on(exec(CreateAccountFixedScenario.CreateAccountFixedRequest())
                    .exec(CreateAccountFineScenario.CreateAccountFineRequest())
                    .exec(CreateAccountConditionalCautionScenario.CreateAccountConditionalCautionRequest())
                
                    )
                    // .exec(CreateAccountFixedScenario.CreateAccountFixedRequest())
                    // .exec(CreateAccountFineScenario.CreateAccountFineRequest())
                    // .exec(CreateAccountConditionalCautionScenario.CreateAccountConditionalCautionRequest())
                )
            );
    }
}
