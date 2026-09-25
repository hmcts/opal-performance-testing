package simulations.Scripts.Utilities;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.CheckBuilder;
import io.gatling.javaapi.http.HttpRequestActionBuilder;

import simulations.Scripts.Headers.Headers;
import simulations.Scripts.RequestBodyBuilder.RequestBodyBuilder;
import simulations.Scripts.RequestBodyBuilder.RequestBodyBuilderR1b;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class AccountSearch {

    public static ChainBuilder search(
            SearchType searchType,
            CheckBuilder... checks
    ) {

        ChainBuilder buildSearchPayload =
            exec(session -> {

                try {

                    String payload;
                    String endpoint;
                    String requestName;

                    switch (searchType) {

                        case ENFORCEMENT -> {

                            payload =
                                RequestBodyBuilderR1b.DefendantAccountSearch
                                    .BuildSearchEnforcementAccountRequestBody(session);

                            endpoint =
                                "/opal-fines-service/defendant-accounts/search";

                            requestName =
                                "OPAL - Defendant Accounts - Enforcement Search";
                        }
 
                        case PGACCOUNT -> {

                            payload =
                                RequestBodyBuilderR1b.DefendantAccountSearch
                                    .BuildSearchParentandGuardianAccountRequestBody(session);

                            endpoint =
                                "/opal-fines-service/defendant-accounts/search";

                            requestName =
                                "OPAL - Defendant Accounts - Enforcement Search";
                        }

                        case MINOR_CREDITOR -> {

                            payload =
                                RequestBodyBuilderR1b.DefendantAccountSearch
                                    .buildMinorCreditorSearchAccountRequestBody(session);

                            endpoint =
                                "/opal-fines-service/minor-creditor-accounts/search";

                            requestName =
                                "OPAL - Minor Creditor Accounts - Search";
                        }

                        case ACCOUNT -> {
                            payload =
                                RequestBodyBuilder
                                    .buildSearchAccountRequestBody(session);

                            endpoint =
                                "/opal-fines-service/defendant-accounts/search";
                            
                            requestName =
                                "OPAL - Minor Creditor Accounts - Search";
                        }

                        case COMPANY -> {
                            payload =
                                RequestBodyBuilder
                                    .buildSearchCompanyAccountRequestBody(session);

                            endpoint =
                                "/opal-fines-service/defendant-accounts/search";
                            
                            requestName =
                                "OPAL - Minor Creditor Accounts - Search";
                        }



                        default ->
                            throw new IllegalArgumentException(
                                "Unsupported search type: " + searchType
                            );
                    }

                    String contentDigest =
                        ContentDigestGenerator
                            .generateSha512ContentDigest(payload);

                    return session
                        .set("searchAccountRequestPayload", payload)
                        .set("searchEndpoint", endpoint)
                        .set("searchRequestName", requestName)
                        .set("contentDigest", contentDigest);

                } catch (Exception e) {

                    System.err.println(
                        "Account search failed: " + e.getMessage()
                    );

                    return session.markAsFailed();
                }
            });


        HttpRequestActionBuilder request =
            http(session ->
                session.getString("searchRequestName")
            )
            .post(session ->
                AppConfig.UrlConfig.BASE_URL
                    + session.getString("searchEndpoint")
            )
            .headers(Headers.getHeaders(14))
            .body(
                StringBody(
                    session ->
                        session.getString(
                            "searchAccountRequestPayload"
                        )
                )
            )
            .asJson()
            .check(status().is(200));


        // Add any extra checks supplied by the scenario
        for (CheckBuilder check : checks) {
            request = request.check(check);
        }


        return buildSearchPayload
            .exitHereIfFailed()
            .exec(request);
    }
}