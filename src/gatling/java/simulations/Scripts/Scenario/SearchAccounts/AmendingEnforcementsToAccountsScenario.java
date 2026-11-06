package simulations.Scripts.Scenario.SearchAccounts;

import simulations.Scripts.Headers.Headers;
import simulations.Scripts.Scenario.DefendantAmendments.AddingEnforcementScenario;
import simulations.Scripts.Scenario.DefendantAmendments.AmendCollectionOrderEnforcementScenario;
import simulations.Scripts.Scenario.DefendantAmendments.RemovingEnforcementScenario;
import simulations.Scripts.Utilities.AccountSearch;
import simulations.Scripts.Utilities.AppConfig;
import simulations.Scripts.Utilities.Feeders;
import simulations.Scripts.Utilities.SearchType;
import simulations.Scripts.Utilities.UserInfoLogger;
import io.gatling.javaapi.core.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public final class AmendingEnforcementsToAccountsScenario {

    private AmendingEnforcementsToAccountsScenario() {}

    public static ChainBuilder AmendingEnforcementsToAccountsRequest() {

        return group("OPAL Add Enforcements To Accounts")
        .on( 
            group("Searching Accounts")
            .on(
                //Search for accounts query parameters 
                pause(10,20)
                .exec(
                    AccountSearch.search(
                        SearchType.ENFORCEMENT,
                    jsonPath(
                            "$.defendant_accounts[?(@.defendant_account_id == '#{accountId}')].defendant_account_id")
                    //.find()
                    .saveAs("defendant_account_id"))
                )                    
            )
            .group("Selecting Account").on(
                pause(10,20)
                .exec(
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
                        .check(Feeders.saveErrorDetails())
                )
                .exec(UserInfoLogger.logDetailedErrorMessage("OPAL - Sso - Authenticated"))
                .exitHereIfFailed()                     
                .exec(
                    http("OPAL - Sso - Authenticated")
                        .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                        .headers(Headers.getHeaders(11))
                        .check(status().is(200))
                        .check(Feeders.saveErrorDetails())
                )
                .exec(UserInfoLogger.logDetailedErrorMessage("OPAL - Sso - Authenticated"))
                .exitHereIfFailed()         
                .exec(
                    http("OPAL - Opal-fines-service - Defendant-accounts - Header-summary")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/defendant-accounts/#{defendant_account_id}/header-summary")
                        .headers(Headers.getHeaders(12))
                        .check(status().saveAs("httpStatus"))
                        .check(status().is(200))
                        .check(jsonPath(
                            "$.business_unit_summary.business_unit_id")
                            .find()
                            .saveAs("getBusinessUnitId")
                        )
                )  
                .exec(
                    http("OPAL - Opal-fines-service - Defendant-accounts - At-a-glance")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/defendant-accounts/#{defendant_account_id}/at-a-glance")
                        .headers(Headers.getHeaders(12))
                        .check(status().saveAs("httpStatus"))
                        .check(status().is(200))
                        .check(header("ETag").saveAs("etag"))              
                )
            )
            .group("Selecting Enforcement tab").on(
                pause(10,20)
                .exec(
                    http("OPAL - Opal-fines-service - Defendant-accounts - Header-summary")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/defendant-accounts/#{defendant_account_id}/enforcement-status")
                        .headers(Headers.getHeaders(12))
                        .check(status().saveAs("httpStatus"))
                        .check(status().is(200))
                        .check(
                            jsonPath("$.last_enforcement_action.enforcement_action.result_id")
                                .optional()
                                .saveAs("enforcementActionResultId")
                        )
                )
            )
                .exec(session -> {

                    String resultId = session.get("enforcementActionResultId");

                    boolean shouldAddEnforcement =
                        resultId == null ||
                        resultId.equalsIgnoreCase("CW") ||
                        resultId.equalsIgnoreCase("REM") ||
                        resultId.equalsIgnoreCase("CONF") ||
                        resultId.equalsIgnoreCase("FSN") ||
                        resultId.equalsIgnoreCase("WDN") ||
                        resultId.equalsIgnoreCase("SC") ||
                        resultId.equalsIgnoreCase("S136") ||
                        resultId.equalsIgnoreCase("NAP");

                        System.out.println(
                            "Existing Enforcement Result: [" + resultId + "]" +
                            " | Should Add Enforcement: " + shouldAddEnforcement
                        );

                    return session.set("shouldAddEnforcement", shouldAddEnforcement);
                })

                .doIfOrElse(session -> session.getBoolean("shouldAddEnforcement"))
                    .then(
                        exec(
                            AddingEnforcementScenario.AddingEnforcementRequest()
                    )
                )                
                .orElse(
                    randomSwitch()
                        .on(    
                            percent(50.0).then(
                                exec(
                                    AmendCollectionOrderEnforcementScenario.AmendCollectionOrderEnforcementRequest()
                                )
                            ),
                            percent(50.0).then(
                                exec(
                                    RemovingEnforcementScenario.RemovingEnforcementRequest()
                                )
                            )
                        )
                )       
            
        );            
    }
}
                     


   