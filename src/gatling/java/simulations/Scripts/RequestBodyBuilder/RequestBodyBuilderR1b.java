package simulations.Scripts.RequestBodyBuilderR1b;


import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.core.CoreDsl.jsonPath;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Session;
import simulations.Scripts.Utilities.DataGenerator;
/**
 * Factory class for building various request bodies used in R1b functinality because MH was afraid that otherwise the RequestBodyBuilder would just be massive and uneditable.
 * This class delegates to specialised builders for each type of request.
 */
public final class RequestBodyBuilderR1b {

public static final class DefendantAccountSearch {
    //to call this elsewhere use .exec(RequestBodyBuilderR1b.DefendantAccountSearch.searchDefendantAccounts())

    private DefendantAccountSearch() {
        // Utility class.
    }

    public static String buildDefendantSearchAccountRequestBody(Session session) {
        String businessUnitIdsJson = session.get("getListBusinessUnitId") != null
            ? session.get("getListBusinessUnitId").toString()
            : "[]";

            //MH Need to add these as .csv files under each journey so that different jorneys might search from a different list of names -especially if they expect to find
            //a fixed penalty or a parent guardian (nor not find a parent guardian so they can add one)
        String surname = session.contains("LASTNAME") ? session.getString("LASTNAME") : "";
        String forenames = session.contains("FIRSTNAME") ? session.getString("FIRSTNAME") : "";
//MH I desperately want to change the below to use something like ${FIRSTNAME} because it makes more sense to my Jmeter brain, but I am TRUSTING THE PROCESS
        return String.format(
                "{\n" +
                "  \"active_accounts_only\": true,\n" +
                "  \"business_unit_ids\": %s,\n" +  
                "  \"defendant\": {\n" +
                "    \"address_line_1\": null,\n" +
                "    \"birth_date\": null,\n" +
                "    \"exact_match_forenames\": false,\n" +
                "    \"exact_match_organisation_name\": null,\n" +
                "    \"exact_match_surname\": false,\n" +
                "    \"forenames\": \"%s\",\n" +
                "    \"include_aliases\": false,\n" +
                "    \"national_insurance_number\": null,\n" +
                "    \"organisation\": false,\n" +
                "    \"organisation_name\": null,\n" +
                "    \"postcode\": null,\n" +
                "    \"surname\": \"%s\"\n" +
                "  },\n" +
                "  \"reference_number\": null\n" +
                "}",
                businessUnitIdsJson,
                forenames,
                surname
            );
    }

    public static ChainBuilder searchDefendantAccounts() {
        return exec(
            http("Search defendant accounts")
                .post("/opal-fines-service/defendant-accounts/search")
                .header("Content-Type", "application/json")
                .body(StringBody(session -> buildSearchAccountRequestBody(session))).asJson()
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

            return session.set("defendant_account_id", defendantAccountIds.get(0));
        });
    }
}




}