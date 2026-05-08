package simulations.Scripts.Scenario.SearchAccounts;

import simulations.Scripts.Headers.Headers;
import simulations.Scripts.Utilities.AppConfig;
import simulations.Scripts.Utilities.UserInfoLogger;
import io.gatling.javaapi.core.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import simulations.Scripts.RequestBodyBuilder.RequestBodyBuilder;
import simulations.Scripts.RequestBodyBuilder.RequestBodyBuilderR1b;
import simulations.Scripts.ScenarioBuilder.DraftAccountQueryBuilder;

public final class R1bDefendantViewScenario {

public static ChainBuilder ViewDefendant() {

    //must pass in ${defendant_account_id} from the Search Account
    return group("R1b View Defendant").on(
        
        //MH call the search here to get the ${defendant_account_id}
        exec(RequestBodyBuilderR1b.DefendantAccountSearch.searchDefendantAccounts())    
        
        .exec(
            http("Open account details page")
                .get("/fines/account/defendant/${defendant_account_id}/details")
                //don't know if we need the headers on the get?
                .headers(Headers.getHeaders(11))
                .check(status().is(200)))
                 
                .pause(15,30)

            .exec(
            http("Load header summary")
                .get("/opal-fines-service/defendant-accounts/${defendant_account_id}/header-summary")
                //don't know if we need the headers on the get?
                .headers(Headers.getHeaders(11))
                .check(status().is(200))
                //MH This is where we check if we have a Fixed Penalty account or not
                .check(jsonPath("$.account_type").saveAs("account_type"))
                )
            
                .exec(
            http("Load at a glance")
                .get("/opal-fines-service/defendant-accounts/${defendant_account_id}/at-a-glance")
                //don't know if we need the headers on the get?
                .headers(Headers.getHeaders(11))
                .check(status().is(200))
        )

        .pause(15,30)

        .exec(
            http("Load Defendant")
                .get("/opal-fines-service/defendant-accounts/${defendant_account_id}/details#defendant")
                //don't know if we need the headers on the get?
                .headers(Headers.getHeaders(11))
                .check(status().is(200))
        )

        .pause(15,30)

          .exec(
            http("Load Payment Terms")
                .get("/opal-fines-service/defendant-accounts/${defendant_account_id}/details#payment-terms")
                //don't know if we need the headers on the get?
                .headers(Headers.getHeaders(11))
                .check(status().is(200))
        )

        .pause(15,30)

          .exec(
            http("Load Enforcement")
                .get("/opal-fines-service/defendant-accounts/${defendant_account_id}/details#enforcement")
                //don't know if we need the headers on the get?
                .headers(Headers.getHeaders(11))
                .check(status().is(200))
        )

         .pause(15,30)

//impositions and History notes are not recording for some reason, may need to confirm development and permissions
//HOWEVER there doesn't appear to be any data on those tabs on the accounts I've looked at so it may be as simple as that and the get requetss would be fine?

          .exec(
            http("Load Impositions")
                .get("/opal-fines-service/defendant-accounts/${defendant_account_id}/details#impositions")
                //don't know if we need the headers on the get?
                .headers(Headers.getHeaders(11))
                .check(status().is(200))
        )

         .pause(15,30)

          .exec(
            http("Load History")
                .get("/opal-fines-service/defendant-accounts/${defendant_account_id}/details#history-and-notes")
                //don't know if we need the headers on the get?
                .headers(Headers.getHeaders(11))
                .check(status().is(200))
        )

.pause(15,30)

//if this is a Fixed penalty account (from the header) then also go to the fixed penalty page
        .doIf(session -> "Fixed Penalty".equals(session.getString("account_type"))).then(

    exec(
        http("Load fixed penalty")
            .get("/opal-fines-service/defendant-accounts/${defendant_account_id}/fixed-penalty")
            .check(status().is(200))
    )
        )

    ) //this is the end of return group("R1b View Defendant")
}

}