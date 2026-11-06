package simulations.Scripts.ScenarioBuilder.Testing;

import simulations.Scripts.Scenario.Login.LoginScenario;
import simulations.Scripts.Scenario.ReviewAccounts.ApproveAccountScenario;
import simulations.Scripts.Utilities.Feeders;
import io.gatling.javaapi.core.*;
import static io.gatling.javaapi.core.CoreDsl.*;

public class ApproveAccountScenarioBuild {

    public static ScenarioBuilder build(String scenarioName) {
        return scenario(scenarioName)
            .group("OPAL Login Requests")
            .on(
                exec(feed(Feeders.checkerUsers()))
                .exec(LoginScenario.LoginRequest())
                .repeat(100).on(
                // // 50/50 split between approve and reject
                randomSwitch()
                    .on(
                        percent(100.0).then(exec(ApproveAccountScenario.ApproveAccountRequest()))
                    //    percent(100.0).then(exec(RejectAccountScenario.RejectAccountRequest()))
                    )

            ));
    }
}
