package simulations.Scripts.ScenarioBuilder;

import simulations.Scripts.Scenario.OpalLogin.CreateAccountConditionalCautionScenario;
import simulations.Scripts.Scenario.OpalLogin.CreateAccountFineScenario;
import simulations.Scripts.Scenario.OpalLogin.CreateAccountFixedScenario;
import simulations.Scripts.Scenario.OpalLogin.LoginScenario;
import simulations.Scripts.Utilities.Feeders;
import io.gatling.javaapi.core.ScenarioBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;

public class InputterUsersScenarioBuild {

    public static ScenarioBuilder build(String scenarioName) {
        return scenario(scenarioName)
            .group("Inputter Workflow")
            .on(
                // Login user once
                feed(Feeders.inputterUsers())
                .exec(LoginScenario.LoginRequest())

                // Create 10 accounts per user
                .repeat(10).on(
                    // Pull NEXT row from SAME CSV
                    feed(Feeders.inputterUsers())

                    .doIf(session -> "FIXED".equals(session.getString("Account"))).then(
                        exec(CreateAccountFixedScenario.CreateAccountFixedRequest())
                    )
                    .doIf(session -> "FINE".equals(session.getString("Account"))).then(
                        exec(CreateAccountFineScenario.CreateAccountFineRequest())
                    )
                    .doIf(session -> "CONDITIONAL".equals(session.getString("Account"))).then(
                        exec(CreateAccountConditionalCautionScenario.CreateAccountConditionalCautionRequest())
                    )
                )
            );
    }
}