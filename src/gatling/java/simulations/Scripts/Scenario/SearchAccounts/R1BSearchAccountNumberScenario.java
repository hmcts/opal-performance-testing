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

public final class R1BSearchAccountNumberScenario {

    private R1BSearchAccountNumberScenario() {}
    private static final Logger logger = LoggerFactory.getLogger(R1BSearchAccountNumberScenario.class);

    public static ChainBuilder R1BSearchAccountNumberRequest() {

        return group("Search Account Number").on(

            // Clear candidates in case this group runs again for the same user.
            exec(session -> session
                    .remove("account_number_id_candidate")
                    .remove("account_number_org_id_candidate")
                    .remove("account_number_minor_credit_id_candidate")
                    .remove("defendant_account_id")
            )

            .exec(AccountSearch.search(
                    SearchType.ACCOUNT_NUMBER,
                    true,
                    jsonPath("$.defendant_accounts[?(@.account_number == '#{accountNumber}')].defendant_account_id")
                            .find(0)
                            .optional()
                            .saveAs("account_number_id_candidate")
            ))
            .exec(UserInfoLogger.logDetailedErrorMessage(
                    "OPAL - Defendant Accounts - Account Number Search"
            ))
            .exitHereIfFailed()

            .exec(AccountSearch.search(
                    SearchType.ACCOUNT_NUMBER_ORGANISATION,
                    true,
                    jsonPath("$.defendant_accounts[?(@.account_number == '#{accountNumber}')].defendant_account_id")
                            .find(0)
                            .optional()
                            .saveAs("account_number_org_id_candidate")
            ))
            .exec(UserInfoLogger.logDetailedErrorMessage(
                    "OPAL - Defendant Accounts - Account Number Organisation Search"
            ))
            .exitHereIfFailed()

            .exec(AccountSearch.search(
                    SearchType.ACCOUNT_NUMBER_MINOR_CREDIT,
                    true,
                    jsonPath("$.defendant_accounts[?(@.account_number == '#{accountNumber}')].defendant_account_id")
                            .find(0)
                            .optional()
                            .saveAs("account_number_minor_credit_id_candidate")
            ))
            .exec(UserInfoLogger.logDetailedErrorMessage(
                    "OPAL - Defendant Accounts - Account Number Minor Creditor Search"
            ))
            .exitHereIfFailed()

            // Select the first non-empty matching ID, in search order.
            .exec(session -> {
                String[] candidateKeys = {
                        "account_number_id_candidate",
                        "account_number_org_id_candidate",
                        "account_number_minor_credit_id_candidate"
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
                    logger.error(
                            "No defendant_account_id found for account number '{}'.",
                            session.getString("accountNumber")
                    );
                    return session.markAsFailed();
                }

                logger.info(
                        "Selected defendant_account_id '{}' for account number '{}'.",
                        defendantAccountId,
                        session.getString("accountNumber")
                );

                return session.set("defendant_account_id", defendantAccountId);
            })
    );
    }
}
                    


   