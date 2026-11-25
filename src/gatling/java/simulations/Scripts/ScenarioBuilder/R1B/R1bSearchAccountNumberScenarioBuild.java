package simulations.Scripts.ScenarioBuilder.R1B;

import simulations.Scripts.Scenario.Login.LoginScenario;
import simulations.Scripts.Scenario.SearchAccounts.R1bDefendantViewScenario;
import simulations.Scripts.Scenario.SearchAccounts.SearchAccountScenario;
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
                    feed(Feeders.defendantSearchUsers())
                )
                .exec(LoginScenario.LoginRequest())
                
                .forever().on(   
                    exec(feed(Feeders.defendantSearchAccounts()))                 
                    .exec(session -> session.set("loopCounter", 0))
                    
                    .repeat(5).on(
                    exec(session -> {

                        int iteration =
                            session.getInt("loopCounter") + 1;

                        String forenameColumn = "";
                        String surnameColumn = "";
                        String accountnumberColumn = "";

                        switch (iteration) {

                            case 1:
                                forenameColumn = "forename1";
                                surnameColumn = "surname1";
                                accountnumberColumn = "accountnumber1";

                                break;

                            case 2:
                                forenameColumn = "forename2";
                                surnameColumn = "surname2";
                                accountnumberColumn = "accountnumber2";

                                break;

                            case 3:
                                forenameColumn = "forename3";
                                surnameColumn = "surname3";
                                accountnumberColumn = "accountnumber3";

                                break;

                            case 4:
                                forenameColumn = "forename4";
                                surnameColumn = "surname4";
                                accountnumberColumn = "accountnumber4";

                                break;

                            case 5:
                                forenameColumn = "forename5";
                                surnameColumn = "surname5";
                                accountnumberColumn = "accountnumber5";

                                break;

                            default:
                                throw new RuntimeException(
                                    "Unexpected iteration: " + iteration
                                );
                        }

                        String forenames = session.getString(forenameColumn);
                        String surname = session.getString(surnameColumn);
                        String accountnumber = session.getString(accountnumberColumn);

                        return session
                            .set("forename", forenames)
                            .set("surname", surname)
                            .set("accountnumber", accountnumber)
                            .set("loopCounter", iteration);
                    })
                    

                    .group("Defendant Search")
                    .on(
                        exec(
                            SearchAccountScenario.SearchAccountRequest()
                        )
                        .exec(session -> {
                            System.out.println("FIXED ACCOUNT: TRUE");
                            AccountCounters.SEARCH_ACCOUNT.incrementAndGet();
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