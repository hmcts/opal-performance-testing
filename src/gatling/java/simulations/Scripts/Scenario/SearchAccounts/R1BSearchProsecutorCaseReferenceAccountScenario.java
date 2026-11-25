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

        return group("Search Account Aliases").on(

                //Search for accounts query parameters 
                exec(
                    AccountSearch.search(
                        SearchType.PROSECUTOR_CASE_REFERENCE,
                    jsonPath("$.count").saveAs("search_count"),
                    jsonPath("$.defendant_accounts[0].defendant_account_id").exists(),
                    jsonPath("$.defendant_accounts[0].defendant_account_id").saveAs("defendant_account_id"))
                )
                .exec(UserInfoLogger.logDetailedErrorMessage("OPAL - Defendant Accounts - Account Prosecutor Case Reference Search"))   
                .exitHereIfFailed()  
                
                .exec(
                    AccountSearch.search(
                        SearchType.PROSECUTOR_CASE_REFERENCE_ORGANISATION,
                    jsonPath("$.count").saveAs("search_count"),
                    jsonPath("$.defendant_accounts[0].defendant_account_id").exists(),
                    jsonPath("$.defendant_accounts[0].defendant_account_id").saveAs("defendant_account_id"))
                )
                .exec(UserInfoLogger.logDetailedErrorMessage("OPAL - Defendant Accounts - Account Prosecutor Case Reference Organisation Search"))   
                .exitHereIfFailed() 
            );            
    }
}
                    


   