package simulations.Scripts.Scenario.OpalLogin;

import simulations.Scripts.Headers.Headers;
import simulations.Scripts.Utilities.AppConfig;
import simulations.Scripts.Utilities.Feeders;
import simulations.Scripts.Utilities.UserInfoLogger;
import io.gatling.javaapi.core.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

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
                    http("Opal - Opal-fines-service - Draft-accounts")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/draft-accounts?business_unit=73&business_unit=80&business_unit=66&business_unit=77&business_unit=78&business_unit=67&business_unit=65&status=Submitted&status=Resubmitted&not_submitted_by=L073AO&not_submitted_by=L080AO&not_submitted_by=L066AO&not_submitted_by=L077AO&not_submitted_by=L078AO&not_submitted_by=L067AO&not_submitted_by=L065AO")
                        .headers(Headers.getHeaders(11))
                )
                .exec(
                    http("Opal - Opal-fines-service - Draft-accounts")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/draft-accounts?business_unit=73&business_unit=80&business_unit=66&business_unit=77&business_unit=78&business_unit=67&business_unit=65&status=Publishing%20Failed&not_submitted_by=L073AO&not_submitted_by=L080AO&not_submitted_by=L066AO&not_submitted_by=L077AO&not_submitted_by=L078AO&not_submitted_by=L067AO&not_submitted_by=L065AO")
                        .headers(Headers.getHeaders(11))
                )
                .exec(
                    http("Opal - Opal-fines-service - Draft-accounts")
                        .get(AppConfig.UrlConfig.BASE_URL
                            + "/opal-fines-service/draft-accounts"
                            + "?business_unit=73&business_unit=80&business_unit=66"
                            + "&business_unit=77&business_unit=78&business_unit=67&business_unit=65"
                            + "&status=Submitted&status=Resubmitted"
                            + "&not_submitted_by=L073AO&not_submitted_by=L080AO"
                            + "&not_submitted_by=L066AO&not_submitted_by=L077AO"
                            + "&not_submitted_by=L078AO&not_submitted_by=L067AO"
                            + "&not_submitted_by=L065AO"
                        )
                        .headers(Headers.getHeaders(11))
                        .check(status().is(200))
                        .check(
                                jsonPath("$.summaries[*].draft_account_id").findAll().saveAs("draftAccountIds"),
                                jsonPath("$.summaries[*].business_unit_id").findAll().saveAs("businessUnitIds"),
                                jsonPath("$.summaries[*].account_status").findAll().saveAs("accountStatuses"),
                                jsonPath("$.summaries[*].submitted_by").findAll().saveAs("submittedBys"),
                                jsonPath("$.summaries[*].submitted_by_name").findAll().saveAs("submittedByNames")
                            )

                        )
                
                .exec(session -> {

                    List<Integer> draftAccountIds = session.getList("draftAccountIds");
                    List<Integer> businessUnitIds = session.getList("businessUnitIds");
                    List<String> accountStatuses = session.getList("accountStatuses");
                    List<String> submittedBys = session.getList("submittedBys");
                    List<String> submittedByNames = session.getList("submittedByNames");

                    if (draftAccountIds == null || draftAccountIds.isEmpty()) {
                        return session.markAsFailed();
                    }

                    int index = 0; // or random

                    return session
                        .set("selectedDraftAccountId", draftAccountIds.get(index))
                        .set("selectedBusinessUnitId", businessUnitIds.get(index))
                        .set("accountStatus", accountStatuses.get(index))
                        .set("submittedBy", submittedBys.get(index))
                        .set("submittedByName", submittedByNames.get(index));
                    }
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
                    http("Opal - Opal-fines-service - Draft-accounts")
                        .get(session -> AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/draft-accounts/" + session.get("selectedDraftAccountId"))
                        .headers(Headers.getHeaders(11))
                        .check(status().is(200))
                )
                .exec(
                    http("Opal - Opal-fines-service - Business-units")
                        .get(session -> AppConfig.UrlConfig.BASE_URL  + "/opal-fines-service/business-units/" + session.get("selectedBusinessUnitId"))
                        .headers(Headers.getHeaders(11))
                        .check(status().is(200))
                )
                .exec(
                    http("Opal - Opal-fines-service - Offences")
                    .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/offences/33369")
                    .headers(Headers.getHeaders(11))
                )
                .exec(
                    http("Opal - Opal-fines-service - Courts")
                    .get(session -> AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/courts?business_unit=" + session.get("selectedBusinessUnitId"))
                    .headers(Headers.getHeaders(11))
                )
                .exec(
                    http("Opal - Opal-fines-service - Results")
                    .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/results?result_ids=FCOMP&result_ids=FVS&result_ids=FCOST&result_ids=FCPC&result_ids=FO&result_ids=FCC&result_ids=FVEBD&result_ids=FFR")
                    .headers(Headers.getHeaders(11))
                )
                .exec(
                    http("Opal - Opal-fines-service - Major-creditors")
                    .get(session -> AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/major-creditors?businessUnit=" + session.get("selectedBusinessUnitId"))
                    .headers(Headers.getHeaders(11))
                )
                .exec(
                    http("Opal - Opal-fines-service - Prosecutors")
                    .get(session -> AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/prosecutors?business_unit=" + session.get("selectedBusinessUnitId"))
                    .headers(Headers.getHeaders(11))
                )
                .exec(
                    http("Opal - Opal-fines-service - Local-justice-areas")
                    .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/local-justice-areas")
                    .headers(Headers.getHeaders(11))
                )
                .exec(
                    http("Opal - Opal-fines-service - Offences")
                    .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/offences?q=HY35014")
                    .headers(Headers.getHeaders(11))
                )
                .exec(session -> {
                    boolean approve = ThreadLocalRandom.current().nextBoolean();

                    if (approve) {
                        return session
                            .set("draftAccountRequestPayload",
                                RequestBodyBuilder.BuildApproveAccountRequestBody(session))
                            .set("actionType", "APPROVE");
                    } else {
                        return session
                            .set("draftAccountRequestPayload",
                                RequestBodyBuilder.BuildRejectAccountRequestBody(session))
                            .set("actionType", "REJECT");
                    }
                })            


                //Approve or reject the selected draft account
                .exec(
                    http("OPAL - Opal-fines-service - Draft-accounts")
                    .patch(session -> AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/draft-accounts/" + session.get("selectedDraftAccountId"))
                    .headers(Headers.getHeaders(15))
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
                http("OPAL - Opal-fines-service - Draft-accounts")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/draft-accounts?business_unit=73&business_unit=80&business_unit=66&business_unit=77&business_unit=78&business_unit=67&business_unit=65&status=Submitted&status=Resubmitted&not_submitted_by=L073AO&not_submitted_by=L080AO&not_submitted_by=L066AO&not_submitted_by=L077AO&not_submitted_by=L078AO&not_submitted_by=L067AO&not_submitted_by=L065AO")
                        .headers(Headers.getHeaders(11))
                )
                .exec(
                    http("OPAL - Opal-fines-service - Draft-accounts")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/draft-accounts?business_unit=73&business_unit=80&business_unit=66&business_unit=77&business_unit=78&business_unit=67&business_unit=65&status=Publishing%20Failed&not_submitted_by=L073AO&not_submitted_by=L080AO&not_submitted_by=L066AO&not_submitted_by=L077AO&not_submitted_by=L078AO&not_submitted_by=L067AO&not_submitted_by=L065AO")
                        .headers(Headers.getHeaders(11))
                )
                .exec(           
                    http("OPAL - Opal-fines-service - Draft-accounts")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/draft-accounts?business_unit=73&business_unit=80&business_unit=66&business_unit=77&business_unit=78&business_unit=67&business_unit=65&status=Submitted&status=Resubmitted&not_submitted_by=L073AO&not_submitted_by=L080AO&not_submitted_by=L066AO&not_submitted_by=L077AO&not_submitted_by=L078AO&not_submitted_by=L067AO&not_submitted_by=L065AO")
                        .headers(Headers.getHeaders(11))
                        .check(status().is(200))
                )
        );            
    }
}
                     


   