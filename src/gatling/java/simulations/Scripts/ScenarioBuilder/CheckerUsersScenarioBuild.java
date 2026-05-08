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
                                    //MH adjusting to 90/10 split per advice from David W
                                    //adding the repeat as well since the first test I did only did 5 at all. 
                                    //MH changing the repeat to a forever because I understand the assignment now
                                    //I don't underdtand the assignment, it didn't work, going to up the repeat to 50 to see if that gets me to 20 min
                .repeat(100).on(  
                //.forever().on(                
                randomSwitch()
                
                    .on(
                        percent(90).then(exec(ApproveAccountScenario.ApproveAccountRequest())),
                        percent(10).then(exec(RejectAccountScenario.RejectAccountRequest()))
                    )
                ))
                );
    }
}
