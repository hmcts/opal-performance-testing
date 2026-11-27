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

public final class AddingOverrideEnforcementScenario {

    private AddingOverrideEnforcementScenario() {}

    public static ChainBuilder AddingOverrideEnforcementRequest() {

        return group("Adding Enforcements")
        .on( 
            group("Adding Enforcement to Account").on(            
                
                //Selecting Enforcement option to add
                pause(10,20)
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
                    http("OPAL - Opal-fines-service - Results - Enforcement")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/results?enforcement=true&enforcement_override=true")
                        .headers(Headers.getHeaders(12))
                        .check(status().saveAs("httpStatus"))
                        .check(status().is(200))
                        .check(
                                jsonPath("$.refData[?(@.result_id == 'SUMA' || @.result_id == 'MAN' || @.result_id == 'AEOC' || @.result_id == 'TFOOUT' || @.result_id == 'ABDC')].result_id").findRandom().saveAs("EnforcementId")
                        )                        
                ) 
                .exec(UserInfoLogger.logDetailedErrorMessage("OPAL - Opal-fines-service - Results - Enforcement"))
                .exitHereIfFailed() 

                .exec(
                    http("OPAL - Opal-fines-service - Local-Justice-Areas")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/local-justice-areas")
                        .headers(Headers.getHeaders(11))
                        .check(status().is(200))
                        .check(Feeders.saveErrorDetails())
                        .check(
                            jsonPath("$.refData[*].local_justice_area_id").findRandom().saveAs("localJusticeAreaId")
                        )
                )
                .exec(UserInfoLogger.logDetailedErrorMessage("OPAL - Opal-fines-service - Local-Justice-Areas"))
                .exitHereIfFailed()  

                // Adding Enforcement option details and selecting Add  
                .exec(
                    http("OPAL - Opal-fines-service - Enforcers")
                    .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/enforcers")
                    .check(status().saveAs("httpStatus"))
                    .check(status().is(200))
                    .check(
                            jsonPath("$.refData[*].enforcer_id").findRandom().saveAs("enforcerId")
                    )
                ) 
                .exec(UserInfoLogger.logDetailedErrorMessage("OPAL - Opal-fines-service - Enforcers"))
                .exitHereIfFailed()            
                
                .exec(
                    http("OPAL - Opal-fines-service - Results")
                    //     .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/results/NOENF")

                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/results/#{EnforcementId}")
                        .headers(Headers.getHeaders(12))
                        .check(status().saveAs("httpStatus"))
                        .check(status().is(200))
                ) 
                .exec(UserInfoLogger.logDetailedErrorMessage("OPAL - Opal-fines-service - Results"))
                .exitHereIfFailed() 

                .exec(session -> {
                    try {
                        String overrideEnforcementRequestPayload =
                            RequestBodyBuilderR1b.DefendantAccountSearch.buildOverrideEnforcementRequestBody(session);
     
                           // System.out.println("Enforcement: " + overrideEnforcementRequestPayload);
                            
                            // Create SHA-512 digest
                            String contentDigest =
                                ContentDigestGenerator.generateSha512ContentDigest(
                                    overrideEnforcementRequestPayload
                                );

                            ObjectMapper mapper = new ObjectMapper();

                            // Convert directly into JsonNode WITHOUT readTree
                            JsonNode json = mapper.readValue(overrideEnforcementRequestPayload, JsonNode.class);

                            return session
                                .set("overrideEnforcementRequestPayload", overrideEnforcementRequestPayload)
                                .set("contentDigest", contentDigest);

                        } catch (Exception e) {
                            System.err.println("Payload parsing failed: " + e.getMessage());
                            return session.markAsFailed();
                        }
                    }
                )
                .exec(
                    http("OPAL - Opal-fines-service - Defendant-accounts - Patch")
                        .patch(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/defendant-accounts/#{defendant_account_id}")
                        .headers(Headers.getHeaders(19))
                        .body(StringBody(session -> session.get("overrideEnforcementRequestPayload"))).asJson()
                        .check(status().is(200))
                        .check(Feeders.saveErrorDetails())
                )
                .exec(UserInfoLogger.logDetailedErrorMessage("OPAL - Opal-fines-service - Defendant-accounts - Patch"))
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
                    http("OPAL - API - Users-state")
                        .get(AppConfig.UrlConfig.BASE_URL + "/api/user-state")
                        .headers(Headers.getHeaders(12))
                        .check(status().saveAs("httpStatus"))
                        .check(status().is(200))
                )
                .exec(UserInfoLogger.logDetailedErrorMessage("OPAL - API - Users-state"))
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