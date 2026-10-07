package simulations.Scripts.Scenario.SearchAccounts;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import io.gatling.javaapi.core.ChainBuilder;
import simulations.Scripts.Headers.Headers;
import simulations.Scripts.Utilities.AppConfig;
import simulations.Scripts.Utilities.UserInfoLogger;

public class R1bMajorCreditorSearchScenario {
private R1bMajorCreditorSearchScenario() {
    }
    public static ChainBuilder MajorCreditorSearch() {
        return group("Major Creditor Search")
        .on( 
            exec(session -> {
                List<String> businessUnitIds = session.getList("businessUnitIds");
                  System.out.println("businessUnitIds = " + businessUnitIds);

                if (businessUnitIds == null || businessUnitIds.isEmpty()) {
                    return session.markAsFailed();
                }

                String selectedBusinessUnitId =
                    businessUnitIds.get(
                        ThreadLocalRandom.current().nextInt(businessUnitIds.size())
                    );
                    System.out.println("selectedBusinessUnitId = " + selectedBusinessUnitId);

                return session.set("selectedBusinessUnitId", selectedBusinessUnitId);
            })        
        
            .group("Selecting Business Unit")
                .on(       
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

                // Get major creditors for business unit
                .exec(
                    http("OPAL - Opal-fines-service - Major-creditors")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/major-creditors").queryParam("businessUnit", "#{selectedBusinessUnitId}")
                        .check(status().is(200))
                        .check(
                            jsonPath("$.refData[*].creditor_account_id").saveAs("creditor_account_id"))
                        .check(
                            jsonPath("$.refData[*].name").saveAs("major_creditor_name"))
                )
                .exec(
                    http("OPAL - Opal-fines-service - Central-funds")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/central-funds/#{selectedBusinessUnitId}")
                        .check(status().is(200))
                        .headers(Headers.getHeaders(20))
                )
                .pause(1)   
            )
            .group("Searching and Opening a Major Creditor Account").on( 
                // Open major creditor defendant view (Basically the Major Creditor search)
                exec(
                    http("OPAL - Fines - Account - Details")
                        .get(AppConfig.UrlConfig.BASE_URL + "/fines/account/major-creditor/#{creditor_account_id}/details")
                        .check(status().is(200))
                )
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
                ) 
                .exec(
                    http("OPAL - Sso - Authenticated")
                    .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                    .headers(Headers.getHeaders(11))
                    .check(status().is(200))
                )                    
                .pause(1)
                .exec(
                    http("OPAL - Opal-fines-service - Major-creditor-accounts - Header-summary")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/major-creditor-accounts/#{creditor_account_id}/header-summary")
                        .headers(Headers.getHeaders(12))
                        .check(status().is(200))
                
                )   
                .exec(UserInfoLogger.logDetailedErrorMessage("OPAL - Opal-fines-service - Major-creditor-accounts - Header-summary"))
                .exitHereIfFailed() 

                .exec(
                    http("OPAL - Opal-fines-service - Major-creditor-accounts - At-a-glance")                                      
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/major-creditor-accounts/#{creditor_account_id}/at-a-glance")
                        .headers(Headers.getHeaders(12))
                        .check(header("ETag").saveAs("etag"))
                        .check(status().is(200))

                )
            )
            .group("Selecting History Tab")
            .on(    
                pause(1)
                .exec(
                    http("OPAL - Opal-fines-service - Major-creditor-accounts - History")                                      
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/major-creditor-accounts/#{creditor_account_id}/history")
                        .headers(Headers.getHeaders(12))
                        .check(status().is(200))

                )
            )
        );
    }    
}
