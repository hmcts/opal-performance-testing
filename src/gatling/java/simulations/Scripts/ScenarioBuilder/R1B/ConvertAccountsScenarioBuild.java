package simulations.Scripts.ScenarioBuilder.R1B;

import simulations.Scripts.Scenario.ConvertToCompany.ConvertToCompanyAccountScenario;
import simulations.Scripts.Scenario.ConvertToCompany.ConvertToDefendantAccountScenario;
import simulations.Scripts.Scenario.Login.LoginScenario;
import simulations.Scripts.Utilities.Feeders;
import io.gatling.javaapi.core.*;

import static io.gatling.javaapi.core.CoreDsl.*;

public class ConvertAccountsScenarioBuild {

    public static ScenarioBuilder build(String scenarioName) {
        return scenario(scenarioName)
            .group("OPAL Login Requests")
            .on(
                exec(
                    feed(Feeders.convertDefendantAccounts())
                )
                .exec(LoginScenario.LoginRequest())

                // Initialise counters
                .exec(session -> session
                    .set("loopCounter", 0)
                )

                .repeat(5).on(
                    exec(session -> {

                        int iteration = session.getInt("loopCounter") + 1;

                        String forenameColumn = "";
                        String surnameColumn = "";
                        String companyNameColumn = "";

                        switch (iteration) {
                            case 1:
                                forenameColumn = "forename1";
                                surnameColumn = "surname1";
                                companyNameColumn = "CompanyName1";
                                break;

                            case 2:
                                forenameColumn = "forename2";
                                surnameColumn = "surname2";
                                companyNameColumn = "CompanyName2";
                                break;

                            case 3:
                                forenameColumn = "forename3";
                                surnameColumn = "surname3";
                                companyNameColumn = "CompanyName3";
                                break;

                            case 4:
                                forenameColumn = "forename4";
                                surnameColumn = "surname4";
                                companyNameColumn = "CompanyName4";
                                break;

                            case 5:
                                forenameColumn = "forename5";
                                surnameColumn = "surname5";
                                companyNameColumn = "CompanyName5";
                                break;

                            default:
                                throw new RuntimeException(
                                    "Unexpected iteration: " + iteration
                                );
                        }

                        String forenames = session.getString(forenameColumn);
                        String surname = session.getString(surnameColumn);
                        String companyName = session.getString(companyNameColumn);

                        System.out.println("======================================");
                        System.out.println("Account Search - Iteration: " + iteration);
                        System.out.println("Forename: [" + forenames + "]");
                        System.out.println("Surname:  [" + surname + "]");
                        System.out.println("CompanyName: [" + companyName + "]");
                        System.out.println("======================================");

                        return session
                            .set("forename", forenames)
                            .set("surname", surname)
                            .set("companyName", companyName)
                            .set("loopCounter", iteration);
                    })

                    .exec(
                        randomSwitch().on(
                            percent(50.0).then(
                                ConvertToCompanyAccountScenario.ConvertToCompanyAccountRequest()
                            ),
                            percent(50.0).then(
                                ConvertToDefendantAccountScenario.ConvertToDefendantAccountRequest()
                            )
                        )
                    )

                )
            );
    }    
}
