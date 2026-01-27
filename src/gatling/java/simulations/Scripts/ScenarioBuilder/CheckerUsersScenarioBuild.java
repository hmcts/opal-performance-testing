package simulations.Scripts.ScenarioBuilder;


import simulations.Scripts.Scenario.OpalLogin.ApproveAccountScenario;
import simulations.Scripts.Scenario.OpalLogin.LoginScenario;
import simulations.Scripts.Scenario.OpalLogin.RejectAccountScenario;
import simulations.Scripts.Utilities.Feeders;
import io.gatling.javaapi.core.*;

import static io.gatling.javaapi.core.CoreDsl.*;

public class CheckerUsersScenarioBuild {

    public static ScenarioBuilder build(String scenarioName) {
        return scenario(scenarioName)
            .group("OPAL Login Requests")
            .on(
                exec(feed(Feeders.inputterUsers())
                    .exec(LoginScenario.LoginRequest())
                                    // 50/50 split between approve and reject
                .randomSwitch()
                    .on(
                        percent(50.0).then(exec(ApproveAccountScenario.ApproveAccountRequest())),
                        percent(50.0).then(exec(RejectAccountScenario.RejectAccountRequest()))
                    )
                    
                )
            );
    }
}
