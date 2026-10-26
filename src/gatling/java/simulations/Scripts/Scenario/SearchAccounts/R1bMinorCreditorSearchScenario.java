package simulations.Scripts.Scenario.SearchAccounts;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.gatling.javaapi.core.ChainBuilder;
import simulations.Scripts.Headers.Headers;
import simulations.Scripts.RequestBodyBuilder.RequestBodyBuilderR1b;
import simulations.Scripts.Utilities.AccountCounters;
import simulations.Scripts.Utilities.AccountSearch;
import simulations.Scripts.Utilities.AppConfig;
import simulations.Scripts.Utilities.ContentDigestGenerator;
import simulations.Scripts.Utilities.SearchType;

public class R1bMinorCreditorSearchScenario {
private R1bMinorCreditorSearchScenario() {
    }
    public static ChainBuilder R1bMinorCreditorSearchRequest() {
        return group("Minor Creditor Search")
        .on(  
            group("Searching Accounts")
            .on(
                //Search for accounts query parameters 
                pause(10,20)
                .exec(
                    http("OPAL - API - Users-state")
                        .get(AppConfig.UrlConfig.BASE_URL + "/api/user-state")
                        .headers(Headers.getHeaders(12))
                        .check(status().saveAs("httpStatus"))
                        .check(status().is(200))
                                            .check(
                        jsonPath("$.domains.fines.business_unit_users[?(@.business_unit_id > 0 && @.business_unit_id < 1000 && @.business_unit_id != 113 && @.business_unit_id != 107)].business_unit_id")
                            .findAll().saveAs("businessUnitIds"))              
                )
                .exec(
                    AccountSearch.search(
                        SearchType.MINOR_CREDITOR,
                        jsonPath("$.creditor_accounts[?(@.defendant.defendant_account_id == '#{accountId}')].creditor_account_id")
                            .saveAs("creditor_account_id")
                    )
                )
            )
            .group("Selecting Account").on(
                pause(10,20)
                .exec(
                    http("OPAL - Fines - Account - Minor-creditor - Details")
                        .get(AppConfig.UrlConfig.BASE_URL + "/fines/account/minor-creditor/#{creditor_account_id}/details")
                        .check(status().is(200))
                )                 
                .exec(
                    http("OPAL - Minor-creditor-accounts - Header-summary")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/minor-creditor-accounts/#{creditor_account_id}/header-summary")
                        .headers(Headers.getHeaders(12))
                        .check(status().is(200))
                        .check(jsonPath(session ->"$.business_unit.business_unit_id").find().saveAs("getBusinessUnitId"))                    
                )            
                .exec(
                    http("OPAL - Minor-creditor-accounts - At-a-glance")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/minor-creditor-accounts/#{creditor_account_id}/at-a-glance")
                        .headers(Headers.getHeaders(12))
                        .check(header("ETag").saveAs("etag"))
                        .check(status().is(200))
                        .check(jsonPath(session -> "$.address.address_line_1").find().optional().saveAs("getAddressLine1"))
                        .check(jsonPath(session -> "$.address.address_line_2").find().optional().saveAs("getAddressLine2"))
                        .check(jsonPath(session -> "$.creditor_account_id").find().optional().saveAs("getCreditorAccountId"))
                        .check(jsonPath(session -> "$.party.individual_details.forenames").find().optional().saveAs("getIndividualForenames"))
                        .check(jsonPath(session -> "$.party.individual_details.surname").find().optional().saveAs("getIndividualSurname"))
                        .check(jsonPath(session -> "$.party.individual_details.title").find().optional().saveAs("getIndividualTitle"))
                        .check(jsonPath(session -> "$.party.party_id").find().optional().saveAs("getPartyId"))
                        .check(header("ETag").saveAs("etag"))

                ) 
            ) 
            .group("Add Note")
            .on(
                exec(session -> {
                    try {
                        String addNoteRequestPayload =
                        RequestBodyBuilderR1b.DefendantAccountSearch.buildAddNoteRequestBody(session);

                        // System.out.println("Enforcement: " + addNoteRequestPayload);
                        
                        // Create SHA-512 digest
                        String contentDigest =
                            ContentDigestGenerator.generateSha512ContentDigest(
                                addNoteRequestPayload
                            );

                        ObjectMapper mapper = new ObjectMapper();

                        // Convert directly into JsonNode WITHOUT readTree
                        JsonNode json = mapper.readValue(addNoteRequestPayload, JsonNode.class);

                        return session
                            .set("addNoteRequestPayload", addNoteRequestPayload)
                            .set("contentDigest", contentDigest);

                        } catch (Exception e) {
                            System.err.println("Payload parsing failed: " + e.getMessage());
                            return session.markAsFailed();
                        }
                    }
                )            
                .exec(
                    http("OPAL - Opal-fines-service - Notes - Add")
                    .post(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/notes")
                    .headers(Headers.getHeaders(21))
                    .body(StringBody(session -> session.get("addNoteRequestPayload"))).asJson()
                    .check(status().is(201))                       
                )
                .exec(
                    http("OPAL - Sso - Authenticated")
                        .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                        .headers(Headers.getHeaders(11))
                        .check(status().is(200))                                         
                )
                .exec(
                    http("OPAL - Minor-creditor-accounts - Header-summary")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/minor-creditor-accounts/#{creditor_account_id}/header-summary")
                        .headers(Headers.getHeaders(12))
                        .check(status().is(200))
                        .check(jsonPath(session ->"$.business_unit.business_unit_id").find().saveAs("getBusinessUnitId"))                    
                )            
                .exec(
                    http("OPAL - Minor-creditor-accounts - At-a-glance")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/minor-creditor-accounts/#{creditor_account_id}/at-a-glance")
                        .headers(Headers.getHeaders(12))
                        .check(header("ETag").saveAs("etag"))
                        .check(status().is(200))
                        .check(jsonPath(session -> "$.address.address_line_1").find().optional().saveAs("getAddressLine1"))
                        .check(jsonPath(session -> "$.address.address_line_2").find().optional().saveAs("getAddressLine2"))
                        .check(jsonPath(session -> "$.creditor_account_id").find().optional().saveAs("getCreditorAccountId"))
                        .check(jsonPath(session -> "$.party.individual_details.forenames").find().optional().saveAs("getIndividualForenames"))
                        .check(jsonPath(session -> "$.party.individual_details.surname").find().optional().saveAs("getIndividualSurname"))
                        .check(jsonPath(session -> "$.party.individual_details.title").find().optional().saveAs("getIndividualTitle"))
                        .check(jsonPath(session -> "$.party.party_id").find().optional().saveAs("getPartyId"))
                        .check(header("ETag").saveAs("etag"))

                )  
            )
            
            .group("Selecting Creditor Tab").on(                      
                pause(10,20)
                .exec(
                    http("OPAL - Minor-creditor-accounts - Get")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/minor-creditor-accounts/#{creditor_account_id}")
                        .headers(Headers.getHeaders(12))
                        .check(status().is(200))
                        .check(
                            jsonPath("$.payment.account_name").optional().saveAs("accountName")
                        )
                )
            )
            .group("Amend Creditor details").on(                      
                pause(10,20)
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
            
                .doIfOrElse(session ->
                        !session.contains("accountName") ||
                        session.getString("accountName") == null
                )
                .then(
                    exec(session -> {

                        System.out.println(
                            "MINOR CREDITOR ACTION: UPDATED"
                        );

                        AccountCounters.MINOR_CREDITOR_UPDATED.incrementAndGet();

                        String updateMinorCreditorSearchAccountRequestPayload =
                            RequestBodyBuilderR1b.DefendantAccountSearch
                                .buildUpdateMinorCreditorAccountRequestBody(session);

                        String contentDigest =
                            ContentDigestGenerator
                                .generateSha512ContentDigest(
                                    updateMinorCreditorSearchAccountRequestPayload
                                );

                        // System.out.println(
                        //     "updateMinorCreditorSearchAccountRequestPayload = "
                        //         + updateMinorCreditorSearchAccountRequestPayload
                        // );

                        // System.out.println(
                        //     "Content-Digest = " + contentDigest
                        // );

                        return session
                            .set(
                                "updateMinorCreditorSearchAccountRequestPayload",
                                updateMinorCreditorSearchAccountRequestPayload
                            )
                            .set(
                                "contentDigest",
                                contentDigest
                            );
                    })
                )
                .orElse(
                    exec(session -> {

                        System.out.println(
                            "MINOR CREDITOR ACTION: CHANGED"
                        );

                        AccountCounters.MINOR_CREDITOR_CHANGED.incrementAndGet();

                        String updateMinorCreditorSearchAccountRequestPayload =
                            RequestBodyBuilderR1b.DefendantAccountSearch
                                .buildRemovePaymentMinorCreditorAccountRequestBody(session);

                        String contentDigest =
                            ContentDigestGenerator
                                .generateSha512ContentDigest(
                                    updateMinorCreditorSearchAccountRequestPayload
                                );

                        // System.out.println(
                        //     "updateMinorCreditorSearchAccountRequestPayload = "
                        //         + updateMinorCreditorSearchAccountRequestPayload
                        // );

                        // System.out.println(
                        //     "Content-Digest = " + contentDigest
                        // );

                        return session
                            .set(
                                "updateMinorCreditorSearchAccountRequestPayload",
                                updateMinorCreditorSearchAccountRequestPayload
                            )
                            .set(
                                "contentDigest",
                                contentDigest
                            );
                    })
                )
                .exec(
                    http("OPAL - Minor-creditor-accounts - Patch")
                        .patch(
                            AppConfig.UrlConfig.BASE_URL
                                + "/opal-fines-service/minor-creditor-accounts/#{creditor_account_id}"
                        )
                        .headers(Headers.getHeaders(19))
                        .body(
                            StringBody(
                                session ->
                                    session.get(
                                        "updateMinorCreditorSearchAccountRequestPayload"
                                    )
                            )
                        )
                        .asJson()
                        .check(status().is(200))
                )
                .exec(
                    http("OPAL - Sso - Authenticated")
                        .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                        .headers(Headers.getHeaders(11))
                        .check(status().is(200))                                         
                ) 
                .exec(
                    http("OPAL - Minor-creditor-accounts - Header-summary")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/minor-creditor-accounts/#{creditor_account_id}/header-summary")
                        .headers(Headers.getHeaders(12))  
                        .check(status().is(200))              
                ) 
                .exec(
                    http("OPAL - Minor-creditor-accounts - Get")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/minor-creditor-accounts/#{creditor_account_id}")
                        .headers(Headers.getHeaders(12))
                        .check(status().is(200))
                )
            )
            .group("Amend Creditor details").on(                      
                pause(10,20)
                .exec(
                    http("OPAL - Minor-creditor-accounts - History")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/minor-creditor-accounts/#{creditor_account_id}/history")
                        .headers(Headers.getHeaders(12))
                        .check(status().is(200))
                )
            )
        );
    }
    
}
