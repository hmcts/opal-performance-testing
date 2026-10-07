package simulations.Scripts.ScenarioBuilder.Testing;



import simulations.Scripts.Scenario.CreateAccounts.CreateAccountConditionalCautionScenario;
import simulations.Scripts.Scenario.CreateAccounts.CreateAccountFineMajorCreditScenario;
import simulations.Scripts.Scenario.CreateAccounts.CreateAccountFineMinorCreditScenario;
import simulations.Scripts.Scenario.CreateAccounts.CreateAccountFixedScenario;
import simulations.Scripts.Scenario.CreateAccounts.CreateAccountParentGuardianYouthScenario;
import simulations.Scripts.Scenario.Login.LoginScenario;
import simulations.Scripts.Utilities.AccountType;
import simulations.Scripts.Utilities.Feeders;
import io.gatling.javaapi.core.*;

import static io.gatling.javaapi.core.CoreDsl.*;

public class CreateAccountScenarioBuild {

    public static ScenarioBuilder build(String scenarioName) {
        return scenario(scenarioName)
            .group("OPAL Login Requests")
            .on(
                feed(Feeders.inputterUsers())
                
                .exec(session -> session
                    .set("username", session.getString("Username"))
                    .set("password", session.getString("Password"))
                    .set("accountType", session.getString("Account"))
                    .set("createdAccountCount", 0)
                )
                    .exec(LoginScenario.LoginRequest())
                    .repeat(1).on(
                        exec(CreateAccountFixedScenario.CreateAccountFixedRequest()),
                        exec(CreateAccountFineMajorCreditScenario.CreateAccountFineRequest()),
                        exec(CreateAccountFineMinorCreditScenario.CreateAccountMinorCreditRequest()),
                        exec(CreateAccountParentGuardianYouthScenario.createAccountRequest(AccountType.YOUTH)),
                        exec(CreateAccountParentGuardianYouthScenario.createAccountRequest(AccountType.PARENT_GUARDIAN)),
                        exec(CreateAccountConditionalCautionScenario.CreateAccountConditionalCautionRequest())

                    )
                //    exec(CreateAccountParentGuardianScenario.CreateAccountParentGuardianRequest())

                    
                 //   exec(CreateAccountConditionalCautionScenario.CreateAccountConditionalCautionRequest())
                
                    
                    // .exec(CreateAccountFixedScenario.CreateAccountFixedRequest())
                    // .exec(CreateAccountFineScenario.CreateAccountFineRequest())
                    // .exec(CreateAccountConditionalCautionScenario.CreateAccountConditionalCautionRequest())
                //)
                );
    }
}
