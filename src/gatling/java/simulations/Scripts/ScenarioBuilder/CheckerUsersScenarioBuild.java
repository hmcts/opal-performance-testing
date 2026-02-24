package simulations.Scripts.ScenarioBuilder;


import simulations.Scripts.Scenario.Login.LoginScenario;
import simulations.Scripts.Scenario.ReviewAccounts.ApproveAccountScenario;
import simulations.Scripts.Scenario.ReviewAccounts.RejectAccountScenario;
import simulations.Scripts.Utilities.Feeders;
import io.gatling.javaapi.core.*;

import static io.gatling.javaapi.core.CoreDsl.*;

public class CheckerUsersScenarioBuild {

    public static ScenarioBuilder build(String scenarioName) {
        return scenario(scenarioName)
            .group("Checker Workflow")
            .on(
                exec(feed(Feeders.checkerUsers())
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
