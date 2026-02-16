package simulations.Scripts.Scenario.OpalLogin;

import simulations.Scripts.Headers.Headers;
import simulations.Scripts.Utilities.AppConfig;
import simulations.Scripts.Utilities.UserInfoLogger;
import io.gatling.javaapi.core.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import simulations.Scripts.ScenarioBuilder.DraftAccountQueryBuilder;

public final class CheckApprovedAccountScenario {

    private CheckApprovedAccountScenario() {}

    public static ChainBuilder CheckApprovedAccountRequest() {

        return group("OPAL Approve Account").on(
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

                //Get Draft Accounts - Submitted and Resubmitted
                .exec(session ->
                    DraftAccountQueryBuilder.buildAndStore(
                        session,
                        "draftAccountSubmittedQueryParams",
                        List.of("Submitted", "Resubmitted"),
                        "not_submitted_by",
                       false
                    )
                )
                .exec(
                    http("Opal - Opal-fines-service - Draft-accounts")
                        .get(session ->
                            AppConfig.UrlConfig.BASE_URL +
                            "/opal-fines-service/draft-accounts?" +
                            session.getString("draftAccountSubmittedQueryParams")
                        )
                        .headers(Headers.getHeaders(11))
                        .check(status().is(200))
                )
                //Get Draft Accounts - Rejected
                .exec(session ->
                    DraftAccountQueryBuilder.buildAndStore(
                        session,
                        "draftAccountRejectedQueryParams",
                        List.of("Rejected"),
                        "submitted_by",
                       false
                    )
                )
                .exec(
                    http("Opal - Opal-fines-service - Draft-accounts")
                        .get(session ->
                            AppConfig.UrlConfig.BASE_URL +
                            "/opal-fines-service/draft-accounts?" +
                            session.getString("draftAccountRejectedQueryParams")
                        )
                        .headers(Headers.getHeaders(11))
                        .check(status().is(200))
                )
                .exec(
                    http("Opal - Opal-fines-service - Draft-accounts")
                        .get(session ->
                            AppConfig.UrlConfig.BASE_URL +
                            "/opal-fines-service/draft-accounts?" +
                            session.getString("draftAccountRejectedQueryParams")
                        )
                        .headers(Headers.getHeaders(11))
                        .check(status().is(200))
                ) 
                .exec(
                    http("Opal - Opal-fines-service - Draft-accounts")
                        .get(session ->
                            AppConfig.UrlConfig.BASE_URL +
                            "/opal-fines-service/draft-accounts?" +
                            session.getString("draftAccountSubmittedQueryParams")
                        )
                        .headers(Headers.getHeaders(11))
                        .check(status().is(200))
                )              

                
               //Select Approve Account tab.
                .exec(
                    http("Opal - Opal-fines-service - Draft-accounts")
                        .get(session ->
                            AppConfig.UrlConfig.BASE_URL +
                            "/opal-fines-service/draft-accounts?" +
                            session.getString("draftAccountRejectedQueryParams")
                        )
                        .headers(Headers.getHeaders(11))
                        .check(status().is(200))
                )
                //Get Draft Accounts - Published and Submitted
                .exec(session ->
                    DraftAccountQueryBuilder.buildAndStore(
                        session,
                        "draftAccountPublishedQueryParams",
                        List.of("Published"),
                        "submitted_by",
                        true
                    )
                )
                .exec(
                    http("Opal - Opal-fines-service - Draft-accounts")
                        .get(session ->
                            AppConfig.UrlConfig.BASE_URL +
                            "/opal-fines-service/draft-accounts?" +
                            session.getString("draftAccountPublishedQueryParams")
                        )
                        .headers(Headers.getHeaders(11))
                        .check(status().is(200))
                )
                .exec(
                    http("Opal - Opal-fines-service - Draft-accounts")
                        .get(session ->
                            AppConfig.UrlConfig.BASE_URL +
                            "/opal-fines-service/draft-accounts?" +
                            session.getString("draftAccountSubmittedQueryParams")
                        )
                        .headers(Headers.getHeaders(11))
                        .check(status().is(200))
                )               
                .exec(
                    http("Opal - Opal-fines-service - Draft-accounts")
                        .get(session ->
                            AppConfig.UrlConfig.BASE_URL +
                            "/opal-fines-service/draft-accounts?" +
                            session.getString("draftAccountPublishedQueryParams")
                        )
                        .headers(Headers.getHeaders(11))
                        .check(status().is(200))
                )
        );            
    }
}
                     


   