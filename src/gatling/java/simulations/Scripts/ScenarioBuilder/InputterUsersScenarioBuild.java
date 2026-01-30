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
                feed(Feeders.inputterUsers())
                .exec(LoginScenario.LoginRequest())
                .doIf(session -> session.getString("Account").equals("FIXED")).then(
                    exec(CreateAccountFixedScenario.CreateAccountFixedRequest())
                )
                .doIf(session -> session.getString("Account").equals("FINE")).then(
                    exec(CreateAccountFineScenario.CreateAccountFineRequest())
                )
                .doIf(session -> session.getString("Account").equals("CONDITIONAL")).then(
                    exec(CreateAccountConditionalCautionScenario.CreateAccountConditionalCautionRequest())
                )
            );
    }
}
