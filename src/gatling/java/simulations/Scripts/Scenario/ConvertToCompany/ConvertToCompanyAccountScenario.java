package simulations.Scripts.Scenario.ConvertToCompany;

import simulations.Scripts.Headers.Headers;
import simulations.Scripts.RequestBodyBuilder.RequestBodyBuilderR1b;
import simulations.Scripts.Utilities.AccountSearch;
import simulations.Scripts.Utilities.AppConfig;
import simulations.Scripts.Utilities.ContentDigestGenerator;
import simulations.Scripts.Utilities.SearchType;
import io.gatling.javaapi.core.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class ConvertToCompanyAccountScenario {

    private ConvertToCompanyAccountScenario() {}

    public static ChainBuilder ConvertToCompanyAccountRequest() {

        return group("OPAL Converting Account To Company")
        .on( 
            group("Converting Account To Company").on(
            
                //Selecting Add Defendant tab:
                pause(10,20)
                .exec(
                    http("OPAL - Sso - Authenticated")
                        .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                        .headers(Headers.getHeaders(11))
                        .check(status().is(200))                                         
                )    
                //Search for accounts query parameters 
                .exec(
                    AccountSearch.search(
                        SearchType.ACCOUNT,
                    jsonPath("$.count").saveAs("search_count"),
                    jsonPath("$.defendant_accounts[0].defendant_account_id").exists(),
                    jsonPath("$.defendant_accounts[0].defendant_account_id")
                    .find().saveAs("defendant_account_id"))
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
                        .check(status().saveAs("httpStatus"))
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
                        .check(header("ETag").saveAs("etag"))
                        .check(status().saveAs("httpStatus"))
                        .check(status().is(200))
                        .check(jsonPath("$.defendant_account_party_id").saveAs("defendantAccountPartyId"))
                        .check(jsonPath("$.party_details.party_id").saveAs("partyId"))
                        .check(jsonPath("$.business_unit_summary.business_unit_id").find().saveAs("getBusinessUnitId")
                        )
                )   
                .exec(session -> {
                    try {
                        String CovertToCompanyRequestPayload =
                            RequestBodyBuilderR1b.DefendantAccountSearch.buildCovertToCompanyRequestBody(session);     
                            
                            System.out.println("CovertToCompanyRequestPayload = " + CovertToCompanyRequestPayload);

                            // Create SHA-512 digest
                            String contentDigest =
                                ContentDigestGenerator.generateSha512ContentDigest(
                                    CovertToCompanyRequestPayload
                                );

                            ObjectMapper mapper = new ObjectMapper();

                            // Convert directly into JsonNode WITHOUT readTree
                            JsonNode json = mapper.readValue(CovertToCompanyRequestPayload, JsonNode.class);

                            return session
                                .set("CovertToCompanyRequestPayload", CovertToCompanyRequestPayload)
                                .set("contentDigest", contentDigest);

                        } catch (Exception e) {
                            System.err.println("Payload parsing failed: " + e.getMessage());
                            return session.markAsFailed();
                        }
                    }
                )                      
                .exec(
                    http("OPAL - Opal-fines-service - Defendant-accounts - Defendant-account-parties - PATCH")
                        .put(
                            AppConfig.UrlConfig.BASE_URL +
                            "/opal-fines-service/defendant-accounts/#{defendant_account_id}/defendant-account-parties/#{defendantAccountPartyId}")
                        .headers(Headers.getHeaders(21))
                        .body(StringBody(session -> session.get("CovertToCompanyRequestPayload"))).asJson()
                        .check(status().saveAs("httpStatus"))
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
                    http("OPAL - Opal-fines-service - Defendant-accounts - Defendant-account-parties - GET")
                        .get(
                            AppConfig.UrlConfig.BASE_URL +
                            "/opal-fines-service/defendant-accounts/#{defendant_account_id}/defendant-account-parties/#{defendantAccountPartyId}"
                        )
                        .headers(Headers.getHeaders(12))
                        .check(status().saveAs("httpStatus"))
                        .check(status().is(200))
                ) 
            )                        
        );            
    }
}