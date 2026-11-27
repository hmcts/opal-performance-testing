package simulations.Scripts.ScenarioBuilder.R1B;

import simulations.Scripts.Scenario.Login.LoginScenario;
import simulations.Scripts.Scenario.SearchAccounts.R1BSearchAccountNumberScenario;
import simulations.Scripts.Scenario.SearchAccounts.R1bDefendantViewScenario;
import simulations.Scripts.Scenario.SearchAccounts.SearchAccountScenario;
import simulations.Scripts.ScenarioBuilder.Testing.SearchAccountScenarioBuild;
import simulations.Scripts.Utilities.AccountCounters;
import simulations.Scripts.Utilities.Feeders;
import io.gatling.javaapi.core.*;

import static io.gatling.javaapi.core.CoreDsl.*;

public class R1bSearchAccountNumberScenarioBuild {

    public static ScenarioBuilder build(String scenarioName) {

        return scenario(scenarioName)
            .group("Defendant Account Number Search and View")
            .on(
                exec(
                    feed(Feeders.defendantAccountNumberSearchUsers())
                )
                .exec(LoginScenario.LoginRequest())
                
                .forever().on(   
                    exec(feed(Feeders.defendantAccountNumberSearch()))                 
                    .exec(session -> session.set("loopCounter", 0))
                    
                    .repeat(5).on(
                    exec(session -> {

                        int iteration =
                            session.getInt("loopCounter") + 1;
                       
                        String accountnumberColumn = "";

                        switch (iteration) {

                            case 1:                                
                                accountnumberColumn = "AccountNumber1";

                                break;

                            case 2:
                                accountnumberColumn = "AccountNumber2";

                                break;

                            case 3:                               
                                accountnumberColumn = "AccountNumber3";

                                break;

                            case 4:                              
                                accountnumberColumn = "AccountNumber4";

                                break;

                            case 5:                              
                                accountnumberColumn = "AccountNumber5";

                                break;

                            default:
                                throw new RuntimeException(
                                    "Unexpected iteration: " + iteration
                                );
                        }

                        String accountNumber = session.getString(accountnumberColumn);

                        return session
                            .set("accountNumber", accountNumber)
                            .set("loopCounter", iteration);
                    })                    

                    .group("Defendant Search")
                    .on(
                        exec(
                            R1BSearchAccountNumberScenario.R1BSearchAccountNumberRequest()
                        )
                        .exec(session -> {
                            AccountCounters.SEARCH_ACCOUNT_NUMBER.incrementAndGet();
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