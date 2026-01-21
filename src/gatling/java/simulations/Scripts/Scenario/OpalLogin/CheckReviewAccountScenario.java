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

public final class CheckReviewAccountScenario {

    private CheckReviewAccountScenario() {}
    private static final Logger logger = LoggerFactory.getLogger("OPAL");

    public static ChainBuilder CheckReviewAccountRequest() {

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
                .exec(
                    http("OPAL - Sso - Authenticated")
                        .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                        .headers(Headers.getHeaders(11))
                        .check(status().is(200))                                         
                )
                .exitHereIfFailed() 

                .exec(  
                    http("request_4")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/draft-accounts?business_unit=73&business_unit=80&business_unit=78&business_unit=67&business_unit=65&business_unit=77&business_unit=66&status=Submitted&status=Resubmitted&submitted_by=L073JG&submitted_by=L080JG&submitted_by=L078JG&submitted_by=L067JG&submitted_by=L065JG&submitted_by=L077JG&submitted_by=L066JG")
                        .headers(Headers.getHeaders(11))
                )
                .exec(
                    http("request_5")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/draft-accounts?business_unit=73&business_unit=80&business_unit=78&business_unit=67&business_unit=65&business_unit=77&business_unit=66&status=Rejected&submitted_by=L073JG&submitted_by=L080JG&submitted_by=L078JG&submitted_by=L067JG&submitted_by=L065JG&submitted_by=L077JG&submitted_by=L066JG")
                        .headers(Headers.getHeaders(11))
                )
                .exec(
                    http("request_6")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/draft-accounts?business_unit=73&business_unit=80&business_unit=78&business_unit=67&business_unit=65&business_unit=77&business_unit=66&status=Rejected&submitted_by=L073JG&submitted_by=L080JG&submitted_by=L078JG&submitted_by=L067JG&submitted_by=L065JG&submitted_by=L077JG&submitted_by=L066JG")
                        .headers(Headers.getHeaders(11))
                )   
                .exec(
                    http("request_7")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/draft-accounts?business_unit=73&business_unit=80&business_unit=78&business_unit=67&business_unit=65&business_unit=77&business_unit=66&status=Submitted&status=Resubmitted&submitted_by=L073JG&submitted_by=L080JG&submitted_by=L078JG&submitted_by=L067JG&submitted_by=L065JG&submitted_by=L077JG&submitted_by=L066JG")
                        .headers(Headers.getHeaders(11))
                )
               
               //Select Reviewed Account tab.
                .exec(
                    http("OPAL - Sso - Authenticated")
                        .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                        .headers(Headers.getHeaders(11))
                        .check(status().is(200))                                         
                )
                .exec(
                    http("request_2")
                        .get("/opal-fines-service/draft-accounts/45")
                        .headers(Headers.getHeaders(11))
                )
                .exec(
                    http("request_3")
                        .get("/opal-fines-service/business-units/77")
                        .headers(Headers.getHeaders(11))
                )
                .exec(
                    http("request_4")
                        .get("/opal-fines-service/offences/33369")
                        .headers(Headers.getHeaders(11))
                )
                .exec(  
                    http("request_5")
                        .get("/opal-fines-service/courts?business_unit=77")
                        .headers(Headers.getHeaders(11))
                )
                .exec(
                    http("request_6")
                        .get("/opal-fines-service/results?result_ids=FCOMP&result_ids=FVS&result_ids=FCOST&result_ids=FCPC&result_ids=FO&result_ids=FCC&result_ids=FVEBD&result_ids=FFR")
                        .headers(Headers.getHeaders(11))
                )
                .exec(
                    http("request_7")
                        .get("/opal-fines-service/prosecutors?business_unit=77")
                        .headers(Headers.getHeaders(11))
                )
                .exec(
                    http("request_8")
                        .get("/opal-fines-service/local-justice-areas")
                        .headers(Headers.getHeaders(11))
                )
                .exec(
                    http("request_9")
                        .get("/opal-fines-service/major-creditors?businessUnit=77")
                        .headers(Headers.getHeaders(11))
                )
                .exec(
                    http("request_10")
                        .get("/opal-fines-service/offences?q=HY35014")
                        .headers(Headers.getHeaders(11))
                )  
        );            
    }
}
                     


   