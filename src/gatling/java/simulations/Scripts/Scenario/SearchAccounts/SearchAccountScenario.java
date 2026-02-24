package simulations.Scripts.Scenario.SearchAccounts;

import simulations.Scripts.Headers.Headers;
import simulations.Scripts.Utilities.AppConfig;
import io.gatling.javaapi.core.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import simulations.Scripts.RequestBodyBuilder.RequestBodyBuilder;
import simulations.Scripts.ScenarioBuilder.DraftAccountQueryBuilder;

public final class SearchAccountScenario {

    private SearchAccountScenario() {}
    private static final Logger logger = LoggerFactory.getLogger("OPAL");

    public static ChainBuilder SearchAccountRequest() {

        return group("OPAL Search Account").on(
                exec(
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
                    http("OPAL - Sso - Authenticated")
                        .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                        .headers(Headers.getHeaders(11))
                        .check(status().is(200))                                         
                )
                .exitHereIfFailed() 

                .exec(
                    http("OPAL - Opal-fines-service - Business-units")
                        .get(session -> AppConfig.UrlConfig.BASE_URL  + "/opal-fines-service/business-units")
                        .headers(Headers.getHeaders(11))
                        .check(status().is(200))
                        .check(
                        jsonPath("$.refData[?(@.opal_domain == 'Fines')].business_unit_id").findAll().saveAs("getListBusinessUnitId"))
                )
                .pause(2, 5)
                
                .exec(
                    http("OPAL - Sso - Authenticated")
                        .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                        .headers(Headers.getHeaders(11))
                        .check(status().is(200))                                         
                )               
                
                // .exec(session -> {
                //     System.out.println("BU LIST = " + session.get("getListBusinessUnitId"));
                //     return session;
                // })
                
                //Search for accounts query parameters 
                .exec(session -> {
                    String searchAccountRequestPayload =
                        RequestBodyBuilder.buildSearchAccountRequestBody(session);
                    return session.set("searchAccountRequestPayload", searchAccountRequestPayload);
                })   
                .exec(
                    http("OPAL - Opal-fines-service - Defendant-accounts - Search")
                    .post(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/defendant-accounts/search")
                    .headers(Headers.getHeaders(14)) 
                    .body(StringBody(session -> session.get("searchAccountRequestPayload"))).asJson()
                    .check(status().is(200)) 

                )
               
        );            
    }
}
                     


   