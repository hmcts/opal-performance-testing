package simulations.Scripts.Scenario.SearchAccounts;

import simulations.Scripts.Headers.Headers;
import simulations.Scripts.Utilities.AccountSearch;
import simulations.Scripts.Utilities.AppConfig;
import simulations.Scripts.Utilities.SearchType;
import simulations.Scripts.Utilities.UserInfoLogger;
import io.gatling.javaapi.core.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class R1BSearchProsecutorCaseReferenceAccountScenario {

    private R1BSearchProsecutorCaseReferenceAccountScenario() {}
    private static final Logger logger = LoggerFactory.getLogger("OPAL");

    public static ChainBuilder R1BSearchProsecutorCaseReferenceAccountRequest() {

        return group("Search Account Prosecutor Case Reference").on(

                // Clear candidates in case this group runs again for the same user.
                exec(session -> session
                        .remove("prosecutor_case_ref_id_candidate")
                        .remove("prosecutor_case_ref_org_id_candidate")
                        .remove("defendant_account_id")
                )

                .exec(AccountSearch.search(
                        SearchType.PROSECUTOR_CASE_REFERENCE,
                        true,
                        jsonPath("$.defendant_accounts[?(@.prosecutor_case_reference == '#{prosecutorCaseReference}')].defendant_account_id")
                                .find(0)
                                .optional()
                                .saveAs("prosecutor_case_ref_id_candidate")
                ))
                .exec(UserInfoLogger.logDetailedErrorMessage(
                        "OPAL - Defendant Accounts - Account Prosecutor Case Reference Search"
                ))
                .exitHereIfFailed()

                .exec(AccountSearch.search(
                        SearchType.PROSECUTOR_CASE_REFERENCE_ORGANISATION,
                        true,
                        jsonPath("$.defendant_accounts[?(@.prosecutor_case_reference == '#{prosecutorCaseReference}')].defendant_account_id")
                                .find(0)
                                .optional()
                                .saveAs("prosecutor_case_ref_org_id_candidate")
                ))
                .exec(UserInfoLogger.logDetailedErrorMessage(
                        "OPAL - Defendant Accounts - Account Prosecutor Case Reference Organisation Search"
                ))
                .exitHereIfFailed()

                // Select the first non-empty matching ID, in search order.
                .exec(session -> {
                    String[] candidateKeys = {
                            "prosecutor_case_ref_id_candidate",
                            "prosecutor_case_ref_org_id_candidate"
                    };

                    String defendantAccountId = null;

                    for (String key : candidateKeys) {
                        if (session.contains(key)) {
                            Object candidate = session.get(key);

                            if (candidate != null) {
                                String value = candidate.toString().trim();

                                if (!value.isEmpty()
                                        && !"null".equalsIgnoreCase(value)) {
                                    defendantAccountId = value;
                                    break;
                                }
                            }
                        }
                    }

                    if (defendantAccountId == null) {
                        System.out.println(
                                "No defendant_account_id found for account number "
                                        + session.getString("prosecutorCaseReference")
                        );
                        return session.markAsFailed();
                    }

                    System.out.println(
                            "Selected defendant_account_id = " + defendantAccountId
                    );

                    return session.set("defendant_account_id", defendantAccountId);
                })
        );    
    }
}
                    


   