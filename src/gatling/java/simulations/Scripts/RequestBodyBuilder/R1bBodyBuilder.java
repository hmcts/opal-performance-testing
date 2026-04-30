//GET /opal-fines-service/defendant-accounts/60000000000063/fixed-penalty
// defendant_account_id=60000000000063 --this one used in the recording, searched for JONES, and is/was the first result
/* 
import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import io.gatling.javaapi.core.Simulation;

import java.time.Duration;
import java.util.List;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class ViewFixedPenaltySimulation extends Simulation {

    private static final String BASE_URL = "https://opal-frontend.test.apps.hmcts.net";

    // This is a fixed search for JONEs, may need to call the search account option function rather than rely on this
    private static final String SEARCH_BODY = """
        {
          "active_accounts_only": true,
          "consolidation_search": false,
          "business_unit_ids": [5,8,9,10,11,12,14,21,22,24,26,28,29,30,31,36,38,45,47,52,57,60,61,65,66,73,77,78,80,82,89,92,96,97,99,103,105,106,107,109,110,112,113,116,119,124,125,126,128,129,130,135,138,139],
          "reference_number": null,
          "defendant": {
            "include_aliases": false,
            "organisation": false,
            "address_line_1": null,
            "postcode": null,
            "organisation_name": null,
            "exact_match_organisation_name": null,
            "surname": "Jones",
            "exact_match_surname": false,
            "forenames": null,
            "exact_match_forenames": false,
            "birth_date": null,
            "national_insurance_number": null
          }
        }
        """;

    private final HttpProtocolBuilder httpProtocol = http
        .baseUrl(BASE_URL)
        .acceptHeader("application/json, text/plain, *NEEDASLASHSTARHEREREMOVEDFORCOMMENTING")
        .acceptLanguageHeader("en-GB,en;q=0.9")
        .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/147.0.0.0 Safari/537.36 Edg/147.0.0.0");

    private final ChainBuilder searchAndOpenFixedPenalty =
        exec(
            http("Search defendant accounts")
                .post("/opal-fines-service/defendant-accounts/search")
                .header("Content-Type", "application/json")
                .body(StringBody(SEARCH_BODY)).asJson()
                .check(status().is(200))
                .check(jsonPath("$.count").saveAs("search_count"))
                .check(jsonPath("$.defendant_accounts[*].defendant_account_id").findAll().saveAs("defendant_account_ids"))
        )
        .exec(session -> {
            @SuppressWarnings("unchecked")
            final List<String> defendantAccountIds = (List<String>) session.get("defendant_account_ids");

            if (defendantAccountIds == null || defendantAccountIds.isEmpty()) {
                return session.markAsFailed();
            }

            // Defaulting to the first result from the search response
            return session.set("defendant_account_id", defendantAccountIds.get(0));
        })
        //a moment to look at the results before selecting the first one anyway
        .pause(20,60)
        .exec(
            http("Open account details page")
                .get("/fines/account/defendant/${defendant_account_id}/details")
                .check(status().in(200, 304))
        )
        .exec(
            http("Load header summary")
                .get("/opal-fines-service/defendant-accounts/${defendant_account_id}/header-summary")
                .check(status().is(200))
        )
      .exec(
            http("Load at a glance")
                .get("/opal-fines-service/defendant-accounts/${defendant_account_id}/at-a-glance")
                .check(status().is(200))
        )
        //this pause feels unnessecary, but it would take a person a moment to move the mouse
        .pause(10,30)
        .exec(
            http("Load fixed penalty tab")
                .get("/opal-fines-service/defendant-accounts/${defendant_account_id}/fixed-penalty")
                .check(status().in(200, 404))
                .check(status().saveAs("fixed_penalty_status"))
        )
       // .exec(session -> {
      //      int fixedPenaltyStatus = session.getInt("fixed_penalty_status");
      //      if (fixedPenaltyStatus == 404) {
                // Catch for accounts where the tab does not exist.
                // Leave the session alive so later data management or alternate navigation can handle it.
     //           return session.set("fixed_penalty_missing", true);
     //       }
     //       return session.set("fixed_penalty_missing", false);
    //    }
    ); 
    */