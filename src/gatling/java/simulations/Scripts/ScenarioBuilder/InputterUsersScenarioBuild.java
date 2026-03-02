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

            // Group all actions under a named group for reporting clarity
            .group("Inputter Workflow")

            .on(
                // Feed user credentials from CSV
                feed(Feeders.inputterUsers())

                // Execute login request
                .exec(LoginScenario.LoginRequest())

                // If you want to repeat a fixed number of times instead of forever,
                // uncomment below and remove .forever()
                //
                // .repeat(10).on(
                //
                // This would execute the account creation logic 10 times per user

                // This will continuously execute account creation
                // until the simulation stops
                .forever().on(
                    // Pull NEXT row from SAME CSV file
                    // This allows different account types per iteration
                    feed(Feeders.inputterUsers())

                    // Depending on the Account type in the CSV,
                    // execute a different account creation request

                    // If Account column == "FIXED"
                    .doIf(session -> "FIXED".equals(session.getString("Account"))).then(
                        exec(CreateAccountFixedScenario.CreateAccountFixedRequest())
                    )

                    // If Account column == "FINE"
                    .doIf(session -> "FINE".equals(session.getString("Account"))).then(
                        exec(CreateAccountFineScenario.CreateAccountFineRequest())
                    )

                    // If Account column == "CONDITIONAL"
                    .doIf(session -> "CONDITIONAL".equals(session.getString("Account"))).then(
                        exec(CreateAccountConditionalCautionScenario.CreateAccountConditionalCautionRequest())
                    )

                )
            );
    }
}