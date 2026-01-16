package simulations.Scripts.Scenario.OpalLogin;

import simulations.Scripts.Headers.Headers;
import simulations.Scripts.Utilities.AppConfig;
import simulations.Scripts.Utilities.Feeders;
import simulations.Scripts.Utilities.UserInfoLogger;
import io.gatling.javaapi.core.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import simulations.Scripts.RequestBodyBuilder.RequestBodyBuilder;

public final class ApproveAccountScenario {

    private ApproveAccountScenario() {}
    private static final Logger logger = LoggerFactory.getLogger("OPAL");

    public static ChainBuilder ApproveAccountRequest() {

        return group("OPAL Approve Account").on(
                exec(
                    http("OPAL - Sso - Authenticated")
                        .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                        .headers(Headers.getHeaders(11))
                        .check(status().is(200))                                         
                )
                .exec(UserInfoLogger.logDetailedErrorMessage("OPAL - Sso - Authenticated"))
                
                .exec(
                    http("OPAL - Sso - Authenticated")
                        .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                        .headers(Headers.getHeaders(11))
                        .check(status().is(200))                                         
                )
                .exitHereIfFailed() 
                .exec(
                    http("request_3")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/draft-accounts?business_unit=73&business_unit=80&business_unit=66&business_unit=77&business_unit=78&business_unit=67&business_unit=65&status=Submitted&status=Resubmitted&not_submitted_by=L073AO&not_submitted_by=L080AO&not_submitted_by=L066AO&not_submitted_by=L077AO&not_submitted_by=L078AO&not_submitted_by=L067AO&not_submitted_by=L065AO")
                        .headers(Headers.getHeaders(11))
                )
                .exec(
                    http("request_4")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/draft-accounts?business_unit=73&business_unit=80&business_unit=66&business_unit=77&business_unit=78&business_unit=67&business_unit=65&status=Publishing%20Failed&not_submitted_by=L073AO&not_submitted_by=L080AO&not_submitted_by=L066AO&not_submitted_by=L077AO&not_submitted_by=L078AO&not_submitted_by=L067AO&not_submitted_by=L065AO")
                        .headers(Headers.getHeaders(11))
                )
                .exec(           
                    http("request_8")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/draft-accounts?business_unit=73&business_unit=80&business_unit=66&business_unit=77&business_unit=78&business_unit=67&business_unit=65&status=Submitted&status=Resubmitted&not_submitted_by=L073AO&not_submitted_by=L080AO&not_submitted_by=L066AO&not_submitted_by=L077AO&not_submitted_by=L078AO&not_submitted_by=L067AO&not_submitted_by=L065AO")
                        .headers(Headers.getHeaders(11))
                        .check(status().is(200))
                        .check(jsonPath("$.summaries").ofList().saveAs("summaries"))
                        .check(jsonPath("$.count").ofInt().saveAs("summariesCount"))
                )
                .exec(session -> {
                    // Extract a random draft account from the summaries list
                    java.util.List<Object> summaries = session.getList("summaries");
                    if (summaries != null && !summaries.isEmpty()) {
                        // Get random index
                        int randomIndex = new java.util.Random().nextInt(summaries.size());
                        Object randomAccount = summaries.get(randomIndex);
                        
                        // Extract values from the random account (assuming it's a Map from JSON parsing)
                        if (randomAccount instanceof java.util.Map) {
                            java.util.Map<String, Object> accountMap = (java.util.Map<String, Object>) randomAccount;
                            
                            // Store the extracted values in session
                            session.set("selectedDraftAccountId", accountMap.get("draft_account_id"));
                            session.set("selectedBusinessUnitId", accountMap.get("business_unit_id"));
                            session.set("selectedAccountStatus", accountMap.get("account_status"));
                            session.set("selectedAccountType", accountMap.get("account_type"));
                            session.set("selectedSubmittedBy", accountMap.get("submitted_by"));
                            session.set("selectedSubmittedByName", accountMap.get("submitted_by_name"));
                            
                            // Extract account_snapshot values
                            java.util.Map<String, Object> snapshot = (java.util.Map<String, Object>) accountMap.get("account_snapshot");
                            if (snapshot != null) {
                                session.set("selectedDefendantName", snapshot.get("defendant_name"));
                                session.set("selectedBusinessUnitName", snapshot.get("business_unit_name"));
                                session.set("selectedCreatedDate", snapshot.get("created_date"));
                                session.set("selectedDateOfBirth", snapshot.get("date_of_birth"));
                            }
                            
                            System.out.println("Selected Draft Account ID: " + accountMap.get("draft_account_id"));
                            System.out.println("Selected Business Unit ID: " + accountMap.get("business_unit_id"));
                            System.out.println("Selected Defendant: " + snapshot.get("defendant_name"));
                        }
                    }
                    return session;
                })

                .exec(
                    http("OPAL - Sso - Authenticated")
                        .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                        .headers(Headers.getHeaders(11))
                )  
                .exec(
                    http("OPAL - Sso - Authenticated")
                        .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                        .headers(Headers.getHeaders(11))
                )  
                .exec(
                    http("request_8")
                    .get(session -> AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/draft-accounts/" + session.get("selectedDraftAccountId"))
                    .headers(Headers.getHeaders(11))
                )
                .exec(
                    http("request_9")
                    .get(session -> AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/business-units/" + session.get("selectedBusinessUnitId"))
                    .headers(Headers.getHeaders(11))
                )
                .exec(
                    http("request_10")
                    .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/offences/33369")
                    .headers(Headers.getHeaders(11))
                )
                .exec(
                    http("request_11")
                    .get(session -> AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/courts?business_unit=" + session.get("selectedBusinessUnitId"))
                    .headers(Headers.getHeaders(11))
                )
                .exec(
                    http("request_12")
                    .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/results?result_ids=FCOMP&result_ids=FVS&result_ids=FCOST&result_ids=FCPC&result_ids=FO&result_ids=FCC&result_ids=FVEBD&result_ids=FFR")
                    .headers(Headers.getHeaders(11))
                )
                .exec(
                    http("request_13")
                    .get(session -> AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/major-creditors?businessUnit=" + session.get("selectedBusinessUnitId"))
                    .headers(Headers.getHeaders(11))
                )
                .exec(
                    http("request_14")
                    .get(session -> AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/prosecutors?business_unit=" + session.get("selectedBusinessUnitId"))
                    .headers(Headers.getHeaders(11))
                )
                .exec(
                    http("request_15")
                    .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/local-justice-areas")
                    .headers(Headers.getHeaders(11))
                )
                .exec(
                    http("request_16")
                    .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/offences?q=HY35014")
                    .headers(Headers.getHeaders(11))
                )

                .exec(session -> {
                    String draftAccountRequestPayload = RequestBodyBuilder.BuildApproveAccountRequestBody(session);
                    System.out.println("draftAccountRequestPayload = " + session.getString("draftAccountRequestPayload"));
                    return session.set("draftAccountRequestPayload", draftAccountRequestPayload);
                })


                //Approve the selected draft account
                .exec(
                    http("OPAL - Opal-fines-service - Draft-accounts")
                    .post(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/draft-accounts/24")
                    .headers(Headers.getHeaders(14))
                    .body(StringBody(session -> session.get("draftAccountRequestPayload"))).asJson()
                    .check(status().is(201)) 
                )             
                
                .exec(
                    http("OPAL - Sso - Authenticated")
                        .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                        .headers(Headers.getHeaders(11))
                )                
                .exec(
                    http("OPAL - Sso - Authenticated")
                        .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                        .headers(Headers.getHeaders(11))
                )
                .exec(
                    http("OPAL - Sso - Authenticated")
                        .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                        .headers(Headers.getHeaders(11))
                )                
                .exec(
                    http("OPAL - Sso - Authenticated")
                    .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                    .headers(Headers.getHeaders(11))
                )    
                
                .exec(
                http("request_3")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/draft-accounts?business_unit=73&business_unit=80&business_unit=66&business_unit=77&business_unit=78&business_unit=67&business_unit=65&status=Submitted&status=Resubmitted&not_submitted_by=L073AO&not_submitted_by=L080AO&not_submitted_by=L066AO&not_submitted_by=L077AO&not_submitted_by=L078AO&not_submitted_by=L067AO&not_submitted_by=L065AO")
                        .headers(Headers.getHeaders(11))
                )
                .exec(
                    http("request_4")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/draft-accounts?business_unit=73&business_unit=80&business_unit=66&business_unit=77&business_unit=78&business_unit=67&business_unit=65&status=Publishing%20Failed&not_submitted_by=L073AO&not_submitted_by=L080AO&not_submitted_by=L066AO&not_submitted_by=L077AO&not_submitted_by=L078AO&not_submitted_by=L067AO&not_submitted_by=L065AO")
                        .headers(Headers.getHeaders(11))
                )
                .exec(           
                    http("request_8")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/draft-accounts?business_unit=73&business_unit=80&business_unit=66&business_unit=77&business_unit=78&business_unit=67&business_unit=65&status=Submitted&status=Resubmitted&not_submitted_by=L073AO&not_submitted_by=L080AO&not_submitted_by=L066AO&not_submitted_by=L077AO&not_submitted_by=L078AO&not_submitted_by=L067AO&not_submitted_by=L065AO")
                        .headers(Headers.getHeaders(11))
                        .check(status().is(200))
                )
        );            
    }
}
                     


   