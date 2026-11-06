package simulations.Scripts.Scenario.DefendantAmendments;

import simulations.Scripts.Headers.Headers;
import simulations.Scripts.Utilities.AppConfig;
import simulations.Scripts.Utilities.ContentDigestGenerator;
import simulations.Scripts.Utilities.Feeders;
import simulations.Scripts.Utilities.UserInfoLogger;
import io.gatling.javaapi.core.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import simulations.Scripts.RequestBodyBuilder.RequestBodyBuilderR1b;

public final class RemovingEnforcementScenario {

    private RemovingEnforcementScenario() {}

    public static ChainBuilder RemovingEnforcementRequest() {

        return group("OPAL Removing Enforcements")
        .on( 
            group("Removing Enforcements").on(
            
                //Selecting Remove Enforcement:
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
                )   
                 .exec(session -> {
                    try {
                        String removeEnforcementRequestPayload =
                            RequestBodyBuilderR1b.DefendantAccountSearch.BuildRemoveEnforcementRequestBody(session);
                    
                            // Create SHA-512 digest
                            String contentDigest =
                                ContentDigestGenerator.generateSha512ContentDigest(
                                    removeEnforcementRequestPayload
                                );

                            ObjectMapper mapper = new ObjectMapper();

                            // Convert directly into JsonNode WITHOUT readTree
                            JsonNode json = mapper.readValue(removeEnforcementRequestPayload, JsonNode.class);

                            return session
                                .set("removeEnforcementRequestPayload", removeEnforcementRequestPayload)
                                .set("contentDigest", contentDigest);

                        } catch (Exception e) {
                            System.err.println("Payload parsing failed: " + e.getMessage());
                            return session.markAsFailed();
                        }
                    }
                )                
                .exec(
                    http("OPAL - Opal-fines-service - Defendant-accounts - Remove-enf-hold")
                        .patch(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/defendant-accounts/#{defendant_account_id}/remove-enf-hold")
                        .headers(Headers.getHeaders(19))
                        .body(StringBody(session -> session.get("removeEnforcementRequestPayload"))).asJson()
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
                    http("OPAL - Opal-fines-service - Defendant-accounts - Header-summary")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/defendant-accounts/#{defendant_account_id}/header-summary")
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
                    http("OPAL - Opal-fines-service - Defendant-accounts - Header-summary")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/defendant-accounts/#{defendant_account_id}/header-summary")
                        .headers(Headers.getHeaders(12))
                        .check(status().saveAs("httpStatus"))
                        .check(status().is(200))
                )
                .exec(
                    http("OPAL - Opal-fines-service - Defendant-accounts - Enforcement-status")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/defendant-accounts/#{defendant_account_id}/enforcement-status")
                        .headers(Headers.getHeaders(12))
                        .check(status().saveAs("httpStatus"))
                        .check(status().is(200))
                )
            )                        
        );            
    }
}
                     


   