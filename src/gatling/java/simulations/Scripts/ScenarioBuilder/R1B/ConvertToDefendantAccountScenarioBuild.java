package simulations.Scripts.ScenarioBuilder.R1B;

import simulations.Scripts.Scenario.ConvertToCompany.ConvertToCompanyAccountScenario;
import simulations.Scripts.Scenario.ConvertToCompany.ConvertToDefendantAccountScenario;
import simulations.Scripts.Scenario.Login.LoginScenario;
import simulations.Scripts.Utilities.Feeders;
import io.gatling.javaapi.core.*;

import static io.gatling.javaapi.core.CoreDsl.*;

public class ConvertToDefendantAccountScenarioBuild {

    public static ScenarioBuilder build(String scenarioName) {
        return scenario(scenarioName)
            .group("OPAL Login Requests")
            .on(
                exec(
                    feed(Feeders.companyAccountUsers())
                )
                .exec(LoginScenario.LoginRequest())

                // Initialise counters
                .exec(session -> session
                    .set("loopCounter", 0)
                )

                .repeat(5).on(
                    exec(session -> {

                        int iteration = session.getInt("loopCounter") + 1;

                        String CompanyNameColumn = "";
                        String accountIdColumn = "";

                        switch (iteration) {
                            case 1:
                                CompanyNameColumn = "CompanyName1";
                                accountIdColumn = "AccountId1";
                                break;

                            case 2:
                                CompanyNameColumn = "CompanyName2";
                                accountIdColumn = "AccountId2";
                                break;

                            case 3:
                                CompanyNameColumn = "CompanyName3";
                                accountIdColumn = "AccountId3";
                                break;

                            case 4:
                                CompanyNameColumn = "CompanyName4";
                                accountIdColumn = "AccountId4";
                                break;

                            case 5:
                                CompanyNameColumn = "CompanyName5";
                                accountIdColumn = "AccountId5";
                                break;

                            default:
                                throw new RuntimeException(
                                    "Unexpected iteration: " + iteration
                                );
                        }

                        String CompanyName = session.getString(CompanyNameColumn);
                        String accountId = session.getString(accountIdColumn);

                        System.out.println("======================================");
                        System.out.println("PG Account Search - Iteration: " + iteration);
                        System.out.println("CompanyName: [" + CompanyName + "]");
                        System.out.println("Account ID: [" + accountId + "]");
                        System.out.println("======================================");

                        return session
                            .set("CompanyName", CompanyName)
                            .set("accountId", accountId)
                            .set("loopCounter", iteration);
                    })

                    .exec(ConvertToDefendantAccountScenario.ConvertToDefendantAccountRequest())
                )
            );
    }    
}
