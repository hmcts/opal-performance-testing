package simulations.Scripts.Scenario.DefendantAmendments;

import simulations.Scripts.Headers.Headers;
import simulations.Scripts.Utilities.AppConfig;
import simulations.Scripts.Utilities.ContentDigestGenerator;
import io.gatling.javaapi.core.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import simulations.Scripts.RequestBodyBuilder.RequestBodyBuilderR1b;

public final class AmendCollectionOrderEnforcementScenario {

    private AmendCollectionOrderEnforcementScenario() {}

    public static ChainBuilder AmendCollectionOrderEnforcementRequest() {

        return group("OPAL Adding Enforcements")
        .on( 
            group("Amend the Collection Oder for Enforcements").on(
            
                //Selecting Add Enforcement to add:
                pause(10,20)
                .exec(
                    http("OPAL - Opal-fines-service - Defendant-accounts - Enforcement-status")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/defendant-accounts/#{defendant_account_id}/enforcement-status")
                        .headers(Headers.getHeaders(12))
                        .check(status().saveAs("httpStatus"))
                        .check(status().is(200))
                        .check(
                            jsonPath("$.enforcement_overview.collection_order_flag")
                                .optional()
                                .saveAs("collectionOrderFlag")
                        )
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
                .exec(session -> {
                    try {
                        String amendEnforcementRequestPayload =
                            RequestBodyBuilderR1b.DefendantAccountSearch.buildAmendCollectionOrderEnforcementRequestBody(session);
     
                           // System.out.println("Enforcement: " + enforcementRequestPayload);
                            
                            // Create SHA-512 digest
                            String contentDigest =
                                ContentDigestGenerator.generateSha512ContentDigest(
                                    amendEnforcementRequestPayload
                                );

                            ObjectMapper mapper = new ObjectMapper();

                            // Convert directly into JsonNode WITHOUT readTree
                            JsonNode json = mapper.readValue(amendEnforcementRequestPayload, JsonNode.class);

                            return session
                                .set("amendEnforcementRequestPayload", amendEnforcementRequestPayload)
                                .set("contentDigest", contentDigest);

                        } catch (Exception e) {
                            System.err.println("Payload parsing failed: " + e.getMessage());
                            return session.markAsFailed();
                        }
                    }
                )
                .exec(
                    http("OPAL - Opal-fines-service - Defendant-accounts")
                    .patch(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/defendant-accounts/#{defendant_account_id}")
                    .headers(Headers.getHeaders(19))
                    .body(StringBody(session -> session.get("amendEnforcementRequestPayload"))).asJson()
                    .check(status().is(200))

                )              
                
                //Selecting Enforcement option to add
                .pause(10,20)
                .exec(
                    http("OPAL - Sso - Authenticated")
                        .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                        .headers(Headers.getHeaders(11))
                        .check(status().is(200))                                         
                )  
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
                     


   