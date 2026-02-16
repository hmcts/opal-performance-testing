package simulations.Scripts.Scenario.OpalLogin;

import simulations.Scripts.Headers.Headers;
import simulations.Scripts.Utilities.AppConfig;
import simulations.Scripts.Utilities.Feeders;
import simulations.Scripts.Utilities.UserInfoLogger;
import io.gatling.javaapi.core.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import simulations.Scripts.RequestBodyBuilder.RequestBodyBuilder;

public final class CreateAccountFineScenario {

    private CreateAccountFineScenario() {}

    public static ChainBuilder CreateAccountFineRequest() {

        return group("OPAL Create Manual Account").on(
                exec(http("OPAL - Sso - Authenticated")
                        .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                        .headers(Headers.getHeaders(11))
                        .check(status().is(200))                                         
                )
                
                .exec(http("OPAL - Sso - Authenticated")
                        .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                        .headers(Headers.getHeaders(11))
                        .check(status().is(200))                                         
                )
                .exitHereIfFailed() 
                .exec(
                    http("OPAL - Opal-fines-service - Business-units")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/business-units?permission=CREATE_MANAGE_DRAFT_ACCOUNTS")
                        .headers(Headers.getHeaders(12))
                        .check(status().is(200)) 
                        .check(Feeders.saveBusinessUnitId())

                )               
                .exitHereIfFailed()                       
           
                //Select Business Unit
                .pause(3,5)

                .exec(
                    http("OPAL - Sso - Authenticated")
                        .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                        .headers(Headers.getHeaders(11))
                ) 
                .exec(
                    http("OPAL - Opal-fines-service - Courts")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/courts?business_unit=#{getBusinessUnitId}")
                        .headers(Headers.getHeaders(12))
                )
                .exec(
                    http("OPAL - Opal-fines-service - Local-justice-areas")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/local-justice-areas")
                        .headers(Headers.getHeaders(12))
                ) 
                .exec(
                    http("OPAL - Sso - Authenticated")
                    .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                    .headers(Headers.getHeaders(11))
                )
                .exec(
                    http("OPAL - Opal-user-service - Users - 0 - State")
                    .get(AppConfig.UrlConfig.BASE_URL + "/opal-user-service/users/0/state")
                    .headers(Headers.getHeaders(12))
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
                    http("OPAL - Opal-fines-service - Results")
                    .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/results?result_ids=FCOMP&result_ids=FVS&result_ids=FCOST&result_ids=FCPC&result_ids=FO&result_ids=FCC&result_ids=FVEBD&result_ids=FFR")
                    .headers(Headers.getHeaders(12))
                )
                .exec(
                    http("OPAL - Opal-fines-service - Major-creditors")
                    .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/major-creditors?businessUnit=#{getBusinessUnitId}")
                    .headers(Headers.getHeaders(12))
                )
                .pause(3,5)
                .exec(
                    http("OPAL - Opal-fines-service - Offences")
                    .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/offences?q=HY35014")
                    .headers(Headers.getHeaders(12))
                )
                .exec(
                    http("OPAL - Opal-user-service - Users - 0 - State")
                    .get(AppConfig.UrlConfig.BASE_URL + "/opal-user-service/users/0/state")
                    .headers(Headers.getHeaders(12))
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
                    http("OPAL - Sso - Authenticated")
                    .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                    .headers(Headers.getHeaders(11))
                ) 
                .exec(
                    http("OPAL - Opal-fines-service - Major-creditors")
                    .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/prosecutors?business_unit=#{getBusinessUnitId}")
                    .headers(Headers.getHeaders(12))
                ) 
                .exec(session -> {
                        String draftAccountRequestPayload =
                            RequestBodyBuilder.BuildDraftAccountFineRequestBody(session);
                        //System.out.println("draftAccountRequestPayload (Fine) = " + draftAccountRequestPayload);
                        return session.set("draftAccountRequestPayload", draftAccountRequestPayload);
                    }
                )                
                .exec(
                    http("OPAL - Opal-fines-service - Draft-accounts")
                    .post(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/draft-accounts")
                    .headers(Headers.getHeaders(14)) 
                    .body(StringBody(session -> session.get("draftAccountRequestPayload"))).asJson()
                    .check(status().is(201)) 
                )
                .exec(
                    http("OPAL - Sso - Authenticated")
                    .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                    .headers(Headers.getHeaders(11))
                )        
        );            
    }
}
                     


   