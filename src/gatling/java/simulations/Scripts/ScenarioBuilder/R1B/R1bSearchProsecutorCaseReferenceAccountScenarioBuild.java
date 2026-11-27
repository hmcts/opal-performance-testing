package simulations.Scripts.ScenarioBuilder.R1B;

import simulations.Scripts.Scenario.Login.LoginScenario;
import simulations.Scripts.Scenario.SearchAccounts.R1BSearchAliasesAccountScenario;
import simulations.Scripts.Scenario.SearchAccounts.R1BSearchProsecutorCaseReferenceAccountScenario;
import simulations.Scripts.Scenario.SearchAccounts.R1bDefendantViewScenario;
import simulations.Scripts.Scenario.SearchAccounts.SearchAccountScenario;
import simulations.Scripts.Utilities.AccountCounters;
import simulations.Scripts.Utilities.Feeders;
import io.gatling.javaapi.core.*;

import static io.gatling.javaapi.core.CoreDsl.*;

public class R1bSearchProsecutorCaseReferenceAccountScenarioBuild {

    public static ScenarioBuilder build(String scenarioName) {

        return scenario(scenarioName)
            .group("Defendant Account Prosecutor Case Reference Search and View")
            .on(
                exec(
                    feed(Feeders.defendantAccountProsecutorCaseReferenceSearchUsers())
                )
                .exec(LoginScenario.LoginRequest())
                
                .forever().on(   
                    exec(feed(Feeders.defendantAccountProsecutorCaseReferenceSearch()))                 
                    .exec(session -> session.set("loopCounter", 0))
                    
                    .repeat(5).on(
                    exec(session -> {

                        int iteration =
                            session.getInt("loopCounter") + 1;

                        String prosecutorCaseReferenceColumn = "";

                        switch (iteration) {

                            case 1:
                                prosecutorCaseReferenceColumn = "ProsecutorCaseReference1";

                                break;

                            case 2:                            
                                prosecutorCaseReferenceColumn = "ProsecutorCaseReference2";

                                break;

                            case 3:
                                prosecutorCaseReferenceColumn = "ProsecutorCaseReference3";

                                break;

                            case 4:
                                prosecutorCaseReferenceColumn = "ProsecutorCaseReference4";

                                break;

                            case 5:
                                prosecutorCaseReferenceColumn = "ProsecutorCaseReference5";

                                break;

                            default:
                                throw new RuntimeException(
                                    "Unexpected iteration: " + iteration
                                );
                        }

                        String prosecutorCaseReference = session.getString(prosecutorCaseReferenceColumn);

                        return session
                            .set("prosecutorCaseReference", prosecutorCaseReference)
                            .set("loopCounter", iteration);
                    })                    

                    .group("Defendant Search")
                    .on(
                        exec(
                            R1BSearchProsecutorCaseReferenceAccountScenario.R1BSearchProsecutorCaseReferenceAccountRequest()
                        )
                        .exec(session -> {
                            AccountCounters.SEARCH_PROSECUTOR_CASE_REFERENCE.incrementAndGet();
                            return session;
                        })
                    )

                    .group("Defendant View")
                    .on(
                        exec(R1bDefendantViewScenario.ViewDefendant()
                        )
                    )

                    .pause(40, 120)
                ))
            );
    }
}