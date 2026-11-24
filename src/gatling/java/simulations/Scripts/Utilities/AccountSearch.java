package simulations.Scripts.Utilities;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.CheckBuilder;
import io.gatling.javaapi.http.HttpRequestActionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import simulations.Scripts.Headers.Headers;
import simulations.Scripts.RequestBodyBuilder.RequestBodyBuilder;
import simulations.Scripts.RequestBodyBuilder.RequestBodyBuilderR1b;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class AccountSearch {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(AccountSearch.class);

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
                                        "OPAL - Defendant Accounts - PG Search";
                            }

                            case MINOR_CREDITOR -> {
                                payload =
                                        RequestBodyBuilderR1b.DefendantAccountSearch
                                                .buildMinorCreditorSearchAccountRequestBody(session);

                                endpoint =
                                        "/opal-fines-service/minor-creditor-accounts/search";

                                requestName =
                                        "OPAL - Minor Creditor Accounts - Minor Creditor Search";
                            }

                            case ACCOUNT -> {
                                payload =
                                        RequestBodyBuilder
                                                .buildSearchAccountRequestBody(session);

                                endpoint =
                                        "/opal-fines-service/defendant-accounts/search";

                                requestName =
                                        "OPAL - Defendant Accounts - Account Search";
                            }

                            case COMPANY -> {
                                payload =
                                        RequestBodyBuilder
                                                .buildSearchCompanyAccountRequestBody(session);

                                endpoint =
                                        "/opal-fines-service/defendant-accounts/search";

                                requestName =
                                        "OPAL - Defendant Accounts - Company Search";
                            }
                        
                            case ALIASES -> {
                                payload =
                                        RequestBodyBuilderR1b.DefendantAccountSearch
                                                .BuildSearchAliasesAccountRequestBody(session);

                                endpoint =
                                        "/opal-fines-service/defendant-accounts/search";

                                requestName =
                                        "OPAL - Defendant Accounts - Aliases Search";
                            }

                            case ACCOUNT_NUMBER -> {
                                payload =
                                        RequestBodyBuilderR1b.DefendantAccountSearch
                                                .BuildSearchAliasesAccountRequestBody(session);

                                endpoint =
                                        "/opal-fines-service/defendant-accounts/search";

                                requestName =
                                        "OPAL - Defendant Accounts - Account Number Search";
                            }

                            case ACCOUNT_NUMBER_ORGANISATION -> {
                                payload =
                                        RequestBodyBuilderR1b.DefendantAccountSearch
                                                .BuildSearchAccountNumberOrganisationRequestBody(session);

                                endpoint =
                                        "/opal-fines-service/defendant-accounts/search";

                                requestName =
                                        "OPAL - Defendant Accounts - Account Number Search";
                            }

                            case ACCOUNT_NUMBER_MINOR_CREDIT -> {
                                payload =
                                        RequestBodyBuilderR1b.DefendantAccountSearch
                                                .BuildSearchAccountNumberMinorCreditRequestBody(session);

                                endpoint =
                                        "/opal-fines-service/minor-creditor/search";

                                requestName =
                                        "OPAL - Defendant Accounts - Account Number Search";
                            }

                            case PROSECUTOR_CASE_REFERENCE -> {
                                payload =
                                        RequestBodyBuilderR1b.DefendantAccountSearch
                                                .BuildSearchProsecutorCaseReferenceRequestBody(session);

                                endpoint =
                                        "/opal-fines-service/defendant-accounts/search";

                                requestName =
                                        "OPAL - Defendant Accounts - Account Number Search";
                            }

                            case PROSECUTOR_CASE_REFERENCE_ORGANISATION -> {
                                payload =
                                        RequestBodyBuilderR1b.DefendantAccountSearch
                                                .BuildSearchProsecutorCaseReferenceOrganisationRequestBody(session);

                                endpoint =
                                        "/opal-fines-service/defendant-accounts/search";

                                requestName =
                                        "OPAL - Defendant Accounts - Account Number Search";
                            }

                            default -> throw new IllegalArgumentException(
                                    "Unsupported search type: " + searchType
                            );
                        }

                        String contentDigest =
                                ContentDigestGenerator
                                        .generateSha512ContentDigest(payload);

                        // Remove values from any previous search so a failed or
                        // empty response cannot leave stale values in the session.
                        return session
                                .remove("search_count")
                                .remove("defendant_account_id")
                                .set("searchAccountRequestPayload", payload)
                                .set("searchEndpoint", endpoint)
                                .set("searchRequestName", requestName)
                                .set("contentDigest", contentDigest);

                    } catch (Exception e) {
                        LOGGER.error(
                                "Could not build account search request",
                                e
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
                        StringBody(session ->
                                session.getString("searchAccountRequestPayload")
                        )
                )
                .asJson()
                .check(status().saveAs("httpStatus"))
                .check(status().is(200))
                .check(Feeders.saveErrorDetails())
                .check(bodyString().optional().saveAs("responseBody"))
                .check(jsonPath("$.count").ofInt().saveAs("search_count"));

        // Add any extra checks supplied by the scenario.
        for (CheckBuilder check : checks) {
            request = request.check(check);
        }

        return buildSearchPayload
                .exitHereIfFailed()
                .exec(request)
                .exec(session -> {
                    if (!session.contains("search_count")) {
                        LOGGER.warn(
                                "Search '{}' returned no count value.",
                                session.getString("searchRequestName")
                        );
                        return session;
                    }

                    int count = session.getInt("search_count");

                    if (count == 0) {
                        String username = session.contains("Username")
                                ? session.getString("Username")
                                : "N/A";

                        String businessUnitId =
                                session.contains("getBusinessUnitId")
                                        ? session.getString("getBusinessUnitId")
                                        : session.contains("selectedBusinessUnitId")
                                                ? session.getString("selectedBusinessUnitId")
                                                    : session.contains("businessUnitId")
                                                    ? session.getString("businessUnitId")
                                                    : "N/A";

                        LOGGER.info(
                                "Search '{}' for user '{}' in BU '{}' returned count 0; stopping this virtual user.",
                                session.getString("searchRequestName"),
                                username,
                                businessUnitId
                        );
                    }
                    return session;
                })
                .exitHereIf(session ->
                        session.contains("search_count")
                                && session.getInt("search_count") == 0
                );
    }
}