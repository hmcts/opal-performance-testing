package simulations.Scripts.Scenario.ParentAndGuardian;

import simulations.Scripts.Headers.Headers;
import simulations.Scripts.Utilities.AccountCounters;
import simulations.Scripts.Utilities.AccountSearch;
import simulations.Scripts.Utilities.AppConfig;
import simulations.Scripts.Utilities.SearchType;
import io.gatling.javaapi.core.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AmendYouthAccountScenario {

    private AmendYouthAccountScenario() {}
    private static final Logger logger = LoggerFactory.getLogger("OPAL");

    public static ChainBuilder AmendYouthAccountRequest() {

        return group("OPAL Add Parent And Guardian Account")
        .on(
            group("Selecting Account")
            .on(
                //Selecting Account tab:
                exec(
                    http("OPAL - API - Users-state")
                        .get(AppConfig.UrlConfig.BASE_URL + "/api/user-state")
                        .headers(Headers.getHeaders(12))
                        .check(status().saveAs("httpStatus"))
                        .check(status().is(200)) 
                )
                
                .exec(
                    http("OPAL - Sso - Authenticated")
                        .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                        .headers(Headers.getHeaders(11))
                        .check(status().is(200))                                         
                )
             
                
                .exec(session -> {
                    System.out.println(
                        "AmendYouthAccount - Searching for accountId: [" +
                        session.get("accountId") +
                        "]"
                    );
                    return session;
                })
            ) 
            .group("Search Account").on(
               
                //Selecting search button 
                pause(20,60)
                .exec(
                    AccountSearch.search(
                        SearchType.PGACCOUNT,
                    jsonPath(
                            "$.defendant_accounts[?(@.defendant_account_id == '#{accountId}')].defendant_account_id"
                    )
                    .find()
                    .saveAs("defendant_account_id"))
                )       

            
                .exec(
                    http("OPAL - Fines - Account - Defendant")
                        .get(AppConfig.UrlConfig.BASE_URL + "/fines/account/defendant/#{defendant_account_id}/details")
                        .headers(Headers.getHeaders(10))
                        .check(status().is(200))                                         

                ) 
                .exec(session ->
                    session.set("getDefendantAccountPartyId", "")
                )
              .exec(
                    http("OPAL - Opal-fines-service - Defendant-accounts - Header-summary")
                        .get(AppConfig.UrlConfig.BASE_URL
                            + "/opal-fines-service/defendant-accounts/#{defendant_account_id}/header-summary")
                        .headers(Headers.getHeaders(12))
                        .check(status().is(200))
                        .check(
                            jsonPath("$.parent_guardian_party_id").optional().saveAs("getParentGuardianPartyId"))
                        .check(
                            jsonPath("$.defendant_account_party_id").optional().saveAs("getDefendantAccountPartyId"))
                        .check(
                            jsonPath("$.business_unit_summary.business_unit_id").find().saveAs("getBusinessUnitId")
                        )
                )

             // Check whether Parent Guardian exists
                .exec(session -> {

                    Object parentGuardianPartyId = session.get("getParentGuardianPartyId");

                    boolean shouldAddPG = parentGuardianPartyId == null;

                    System.out.println(
                        "Existing PG Account: [" + parentGuardianPartyId + "]" +
                        " | Should Add PG: " + shouldAddPG
                    );

                    return session.set("shouldAddPG", shouldAddPG);
                })
            )
            .group("Selecting Account").on(

                exec(
                    http("OPAL - Opal-fines-service - Defendant-accounts - At-a-glance")
                        .get(AppConfig.UrlConfig.BASE_URL
                            + "/opal-fines-service/defendant-accounts/#{defendant_account_id}/at-a-glance")
                        .headers(Headers.getHeaders(12))
                        .check(status().is(200)) 
                        .check(
                            header("ETag").saveAs("etag")
                        )
                )
            )

           .doIfOrElse(session -> session.getBoolean("shouldAddPG"))
            .then(
                exec(
                    AddParentAndGuardianAccountScenario
                        .AddParentAndGuardianAccountRequest()
                )
                .exec(session -> {

                    int count = session.getInt("addedPGCount");

                    System.out.println("PG ACTION: ADDED");

                    // Global counter - all Gatling users
                    AccountCounters.PG_ADDED.incrementAndGet();

                    // Per-user counter
                    return session.set("addedPGCount", count + 1);
                })
            )
            .orElse(
                randomSwitch()
                    .on(
                        percent(50.0).then(
                            exec(
                                RemoveParentAndGuardianAccountScenario
                                    .RemoveParentAndGuardianAccountRequest()
                            )
                            .exec(session -> {

                                int count = session.getInt("removedPGCount");

                                System.out.println("PG ACTION: REMOVED");

                                // Global counter - all Gatling users
                                AccountCounters.PG_REMOVED.incrementAndGet();

                                // Per-user counter
                                return session.set(
                                    "removedPGCount",
                                    count + 1
                                );
                            })
                        ),

                        percent(50.0).then(
                            exec(
                                ChangeParentAndGuardianAccount
                                    .ChangeParentAndGuardianAccountRequest()
                            )
                            .exec(session -> {

                                int count = session.getInt("changedPGCount");

                                System.out.println("PG ACTION: CHANGED");

                                // Global counter - all Gatling users
                                AccountCounters.PG_CHANGED.incrementAndGet();

                                // Per-user counter
                                return session.set(
                                    "changedPGCount",
                                    count + 1
                                );
                            })
                        )
                    )
            )
            .group("Account Details").on(
                exec(
                    http("OPAL - Opal-fines-service - Major-creditor-accounts - At-a-glance")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/defendant-accounts/#{defendant_account_id}/at-a-glance")
                        .headers(Headers.getHeaders(12))
                        .check(status().is(200)) 
                        .check(header("ETag").saveAs("etag")
                    )
                )               
            )
        );            
    }
}
                     


   