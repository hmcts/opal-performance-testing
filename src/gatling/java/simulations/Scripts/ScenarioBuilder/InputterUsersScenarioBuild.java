package simulations.Scripts.ScenarioBuilder;

import simulations.Scripts.Scenario.OpalLogin.*;
import simulations.Scripts.Utilities.AccountCounters;
import simulations.Scripts.Utilities.AppConfig;
import simulations.Scripts.Utilities.Feeders;
import io.gatling.javaapi.core.*;

import static io.gatling.javaapi.core.CoreDsl.*;

public class InputterUsersScenarioBuild {

    public static ScenarioBuilder build(String scenarioName) {

        return scenario(scenarioName)
            .group("Inputter Workflow")
            .on(
                exec(feed(Feeders.inputterUsers()))
                .exec(LoginScenario.LoginRequest())

                .asLongAs(session -> !AccountCounters.isComplete()).on(

                    exec(session -> {

                        int fixed = AccountCounters.fixed.incrementAndGet();
                        if (fixed <= AppConfig.TestingConfig.FIXED_TARGET) {
                            return session.set("accountType", "FIXED");
                        }

                        int fine = AccountCounters.fine.incrementAndGet();
                        if (fine <= AppConfig.TestingConfig.FINE_TARGET) {
                            return session.set("accountType", "FINE");
                        }

                        int conditional = AccountCounters.conditional.incrementAndGet();
                        if (conditional <= AppConfig.TestingConfig.CONDITIONAL_TARGET) {
                            return session.set("accountType", "CONDITIONAL");
                        }

                        return session;
                    })

                    .doIfEquals("accountType", "FIXED")
                        .then(exec(CreateAccountFixedScenario.CreateAccountFixedRequest()))

                    .doIfEquals("accountType", "FINE")
                        .then(exec(CreateAccountFineScenario.CreateAccountFineRequest()))

                    .doIfEquals("accountType", "CONDITIONAL")
                        .then(exec(CreateAccountConditionalCautionScenario
                            .CreateAccountConditionalCautionRequest()))
                )
            );
    }
}
