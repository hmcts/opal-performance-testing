package simulations.Scripts.ScenarioBuilder.R1B;

import simulations.Scripts.Scenario.Login.LoginScenario;
import simulations.Scripts.Scenario.ParentAndGuardian.AmendYouthAccountScenario;
import simulations.Scripts.Utilities.Feeders;
import io.gatling.javaapi.core.*;

import static io.gatling.javaapi.core.CoreDsl.*;

public class ParentGuardianAccountSearchScenarioBuild {

    public static ScenarioBuilder build(String scenarioName) {
        return scenario(scenarioName)
            .group("Parent Guardian Account")
            .on(
                exec(
                    feed(Feeders.pGUsers())
                )
                .exec(LoginScenario.LoginRequest())

                // Initialise counters
                .exec(session -> session
                    .set("loopCounter", 0)
                    .set("addedPGCount", 0)
                    .set("removedPGCount", 0)
                    .set("changedPGCount", 0)
                )

                .repeat(5).on(
                    exec(session -> {

                        int iteration = session.getInt("loopCounter") + 1;

                        String forenameColumn = "";
                        String surnameColumn = "";
                        String accountIdColumn = "";

                        switch (iteration) {
                            case 1:
                                forenameColumn = "forename1";
                                surnameColumn = "surname1";
                                accountIdColumn = "AccountId1";
                                break;

                            case 2:
                                forenameColumn = "forename2";
                                surnameColumn = "surname2";
                                accountIdColumn = "AccountId2";
                                break;

                            case 3:
                                forenameColumn = "forename3";
                                surnameColumn = "surname3";
                                accountIdColumn = "AccountId3";
                                break;

                            case 4:
                                forenameColumn = "forename4";
                                surnameColumn = "surname4";
                                accountIdColumn = "AccountId4";
                                break;

                            case 5:
                                forenameColumn = "forename5";
                                surnameColumn = "surname5";
                                accountIdColumn = "AccountId5";
                                break;

                            default:
                                throw new RuntimeException(
                                    "Unexpected iteration: " + iteration
                                );
                        }

                        String forenames = session.getString(forenameColumn);
                        String surname = session.getString(surnameColumn);
                        String accountId = session.getString(accountIdColumn);

                        System.out.println("======================================");
                        System.out.println("PG Account Search - Iteration: " + iteration);
                        System.out.println("Forename: [" + forenames + "]");
                        System.out.println("Surname:  [" + surname + "]");
                        System.out.println("Account ID: [" + accountId + "]");
                        System.out.println("======================================");

                        return session
                            .set("forename", forenames)
                            .set("surname", surname)
                            .set("accountId", accountId)
                            .set("loopCounter", iteration);
                    })

                    .exec(
                        AmendYouthAccountScenario.AmendYouthAccountRequest()
                    )
                )

                .exec(session -> {

                    int added = session.getInt("addedPGCount");
                    int removed = session.getInt("removedPGCount");
                    int changed = session.getInt("changedPGCount");

                    System.out.println("======================================");
                    System.out.println("PARENT & GUARDIAN ACCOUNT SUMMARY");
                    System.out.println("======================================");
                    System.out.println("Added:   " + added);
                    System.out.println("Removed: " + removed);
                    System.out.println("Changed: " + changed);
                    System.out.println("--------------------------------------");
                    System.out.println("Total:   " + (added + removed + changed));
                    System.out.println("======================================");

                    return session;
                })
            );
    }    
}
