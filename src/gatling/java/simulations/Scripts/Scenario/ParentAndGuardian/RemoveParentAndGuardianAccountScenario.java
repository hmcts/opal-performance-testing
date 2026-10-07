package simulations.Scripts.Scenario.ParentAndGuardian;

import simulations.Scripts.Headers.Headers;
import simulations.Scripts.Utilities.AppConfig;
import simulations.Scripts.Utilities.ContentDigestGenerator;
import io.gatling.javaapi.core.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import simulations.Scripts.RequestBodyBuilder.RequestBodyBuilderR1b;

public final class RemoveParentAndGuardianAccountScenario {

    private RemoveParentAndGuardianAccountScenario() {}
    private static final Logger logger = LoggerFactory.getLogger("OPAL");

    public static ChainBuilder RemoveParentAndGuardianAccountRequest() {

        return group("OPAL Remove Parent And Guardian")
        .on(
            group("Remove Parent And Guardian")
            .on(

                exec(
                    http("OPAL - Opal-fines-service - Defendant-accounts - Defendant-account-parties")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/defendant-accounts/#{defendant_account_id}/defendant-account-parties/#{getParentGuardianPartyId}")
                        .headers(Headers.getHeaders(12))  
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

                .pause(5,20)
                .exec(session -> {
                    try {
                        String removePGRequestPayload =
                            RequestBodyBuilderR1b.DefendantAccountSearch.BuildremovePGRequestBody(session);
                        //    System.err.println("Payload: " + removePGRequestPayload);

                        // Create SHA-512 digest
                        String contentDigest =
                            ContentDigestGenerator.generateSha512ContentDigest(
                                removePGRequestPayload
                            );


                        ObjectMapper mapper = new ObjectMapper();

                        // Convert directly into JsonNode WITHOUT readTree
                        JsonNode json = mapper.readValue(removePGRequestPayload, JsonNode.class);

                        return session
                            .set("removePGRequestPayload", removePGRequestPayload)
                            .set("contentDigest", contentDigest);

                    } catch (Exception e) {
                        System.err.println("Payload parsing failed: " + e.getMessage());
                        return session.markAsFailed();
                    }
                })
                .exec(
                    http("OPAL - Opal-fines-service - Defendant-accounts - Defendant-account-parties - Delete")
                        .delete(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/defendant-accounts/#{defendant_account_id}/defendant-account-parties/#{getParentGuardianPartyId}")
                        .headers(Headers.getHeaders(19))  
                        .body(StringBody(session -> session.get("removePGRequestPayload"))).asJson()
                        .check(status().is(200))                                         

                )
                .exec(
                    http("OPAL - Opal-fines-service - Defendant-accounts - Header-summary")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/defendant-accounts/#{defendant_account_id}/header-summary")
                        .headers(Headers.getHeaders(12)) 
                        .check(status().is(200))                                         
                        .check(
                            jsonPath(session -> "$.defendant_account_party_id").find().optional().saveAs("GetDefendantAccountPartyId"))                        
                )
                
                .exec(
                    http("OPAL - Opal-fines-service - Defendant-accounts - Defendant-account-parties")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/defendant-accounts/#{defendant_account_id}/defendant-account-parties/#{GetDefendantAccountPartyId}")
                        .headers(Headers.getHeaders(12))
                        .check(status().is(200))    
                )
            )
        );            
    }
}
                     


   