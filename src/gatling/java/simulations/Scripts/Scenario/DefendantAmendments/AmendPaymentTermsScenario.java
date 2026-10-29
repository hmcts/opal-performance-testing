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

public final class AmendPaymentTermsScenario {

    private AmendPaymentTermsScenario() {}

    public static ChainBuilder AmendPaymentTermsRequest() {

        return group("OPAL Amend Payment Terms")
        .on( 
            group("Amend the Payment Terms").on(
            
                //Selecting Change Payment Terms tab:
                pause(10,20)
                .exec(
                    http("OPAL - Opal-fines-service - Defendant-accounts - Payment-terms - Latest")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/defendant-accounts/#{defendant_account_id}/payment-terms/latest")
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
                .exec(
                    http("OPAL - Opal-fines-service - Defendant-accounts - Header-summary")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/defendant-accounts/#{defendant_account_id}/header-summary")
                        .headers(Headers.getHeaders(12))
                        .check(header("ETag").saveAs("etag"))
                        .check(status().saveAs("httpStatus"))
                        .check(status().is(200))
                )
                .pause(10,20)

                .exec(session -> {
                    try {
                        String changePaymentTermsRequestPayload =
                            RequestBodyBuilderR1b.DefendantAccountSearch.buildChangePaymentTermsRequestBody(session);
        
                            // System.out.println("Enforcement: " + enforcementRequestPayload);
                            
                            // Create SHA-512 digest
                            String contentDigest =
                                ContentDigestGenerator.generateSha512ContentDigest(
                                    changePaymentTermsRequestPayload
                                );

                            ObjectMapper mapper = new ObjectMapper();

                            // Convert directly into JsonNode WITHOUT readTree
                            JsonNode json = mapper.readValue(changePaymentTermsRequestPayload, JsonNode.class);

                            return session
                                .set("changePaymentTermsRequestPayload", changePaymentTermsRequestPayload)
                                .set("contentDigest", contentDigest);

                        } catch (Exception e) {
                            System.err.println("Payload parsing failed: " + e.getMessage());
                            return session.markAsFailed();
                        }
                    }
                )
                .exec(
                    http("OPAL - Opal-fines-service - Defendant-accounts - Payment-terms")
                        .post(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/defendant-accounts/#{defendant_account_id}/payment-terms")
                        .headers(Headers.getHeaders(23))
                        .body(StringBody(session -> session.get("changePaymentTermsRequestPayload"))).asJson()
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
                    http("OPAL - Opal-fines-service - Defendant-accounts - Header-summary")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/defendant-accounts/#{defendant_account_id}/header-summary")
                        .headers(Headers.getHeaders(12))
                        .check(status().saveAs("httpStatus"))
                        .check(status().is(200))
                )
                .exec(
                    http("OPAL - Opal-fines-service - Defendant-accounts - Payment-terms - Latest")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/defendant-accounts/#{defendant_account_id}/payment-terms/latest")
                        .headers(Headers.getHeaders(12))
                        .check(status().saveAs("httpStatus"))
                        .check(status().is(200))
                )
            )                        
        );            
    }
}
                     


   