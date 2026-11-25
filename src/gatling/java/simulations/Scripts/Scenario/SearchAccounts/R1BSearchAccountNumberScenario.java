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
    private static final Logger logger = LoggerFactory.getLogger("OPAL");

    public static ChainBuilder R1BSearchAccountNumberRequest() {

        return group("Search Account Number").on(

                //Search for accounts query parameters 
               exec(
                    AccountSearch.search(
                        SearchType.ACCOUNT_NUMBER,
                    jsonPath("$.count").saveAs("search_count"),
                    jsonPath("$.defendant_accounts[0].defendant_account_id").exists(),
                    jsonPath("$.defendant_accounts[0].defendant_account_id").saveAs("defendant_account_id"))

                )
                .exec(UserInfoLogger.logDetailedErrorMessage("OPAL - Defendant Accounts - Account Number Search"))   
                .exitHereIfFailed()              
                
                .exec(
                    AccountSearch.search(
                        SearchType.ACCOUNT_NUMBER_ORGANISATION,
                    jsonPath("$.count").saveAs("search_count"),
                    jsonPath("$.defendant_accounts[0].defendant_account_id").exists(),
                    jsonPath("$.defendant_accounts[0].defendant_account_id").saveAs("defendant_account_id"))

                )
                .exec(UserInfoLogger.logDetailedErrorMessage("OPAL - Defendant Accounts - Account Number Organisation Search"))   
                .exitHereIfFailed()        
                
                .exec(
                    AccountSearch.search(
                        SearchType.ACCOUNT_NUMBER_MINOR_CREDIT,
                    jsonPath("$.count").saveAs("search_count"),
                    jsonPath("$.defendant_accounts[0].defendant_account_id").exists(),
                    jsonPath("$.defendant_accounts[0].defendant_account_id").saveAs("defendant_account_id"))

                )
                .exec(UserInfoLogger.logDetailedErrorMessage("OPAL - Defendant Accounts - Account Number Minor Creditor Search"))   
                .exitHereIfFailed()                    
            );            
    }
}
                    


   