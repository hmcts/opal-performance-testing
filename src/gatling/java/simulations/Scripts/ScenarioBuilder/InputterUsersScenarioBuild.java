package simulations.Scripts.ScenarioBuilder;


import simulations.Scripts.Scenario.CreateAccounts.CreateAccountConditionalCautionScenario;
import simulations.Scripts.Scenario.CreateAccounts.CreateAccountFineScenario;
import simulations.Scripts.Scenario.CreateAccounts.CreateAccountFixedScenario;
import simulations.Scripts.Scenario.Login.LoginScenario;
import simulations.Scripts.Utilities.Feeders;
import io.gatling.javaapi.core.ScenarioBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;

public class InputterUsersScenarioBuild {

    /**
     * Builds the Inputter user workflow scenario.
     *
     * @param scenarioName - Name of the scenario (used in Gatling reports)
     * @return ScenarioBuilder - Fully constructed Gatling scenario
     */
    public static ScenarioBuilder build(String scenarioName) {

       return scenario(scenarioName)

        .group("Inputter Workflow")
        .on(

            // Feed CSV once per user
            feed(Feeders.inputterUsers())

            .exec(session -> session
                .set("username", session.getString("Username"))
                .set("password", session.getString("Password"))
                .set("accountType", session.getString("Account"))
                .set("createdAccountCount", 0)
            )

            .exec(LoginScenario.LoginRequest())

             // Keep creating accounts until simulation ends
            .forever().on(
                exec(session -> {
                    System.out.println(
                        "User: " + session.getString("username") +
                        " | Account type: " + session.getString("accountType")
                    );
                    return session;
                })
                .doIf(session ->
                    "FIXED".equalsIgnoreCase(session.getString("accountType"))
                ).then(
                    exec(CreateAccountFixedScenario.CreateAccountFixedRequest())
                )

                .doIf(session ->
                    "FINE".equalsIgnoreCase(session.getString("accountType"))
                ).then(
                    exec(CreateAccountFineScenario.CreateAccountFineRequest())
                )

                .doIf(session ->
                    "CONDITIONAL".equalsIgnoreCase(session.getString("accountType"))
                ).then(
                    exec(CreateAccountConditionalCautionScenario.CreateAccountConditionalCautionRequest())
                )
            )

        );
    }
}