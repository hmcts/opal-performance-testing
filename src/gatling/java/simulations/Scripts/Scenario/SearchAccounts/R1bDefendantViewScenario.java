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
import simulations.Scripts.RequestBodyBuilderR1b.RequestBodyBuilderR1b; //MH this is not the file path. this is a class path, this is strange. I do not like it.

public final class R1bDefendantViewScenario {

public static ChainBuilder ViewDefendant() {

    //must pass in ${defendant_account_id} from the Search Account
    return group("R1b View Defendant").on(
        
        //MH call the search here to get the ${defendant_account_id}
        exec(RequestBodyBuilderR1b.DefendantAccountSearch.searchDefendantAccounts())    

        //MH setting up requestIDs
         .exec(session -> session
    .set("requestId", java.util.UUID.randomUUID().toString())
    .set("traceparent",
        "00-" +
        java.util.UUID.randomUUID().toString().replace("-", "") +
        "-" +
        java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 16) +
        "-01"))
        
        
        //authentication is what you need
          .exec(
                    http("OPAL - Sso - Authenticated")
                        .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                        .headers(Headers.getHeaders(11))
                ) 
                //MH ok this set ip is weird why is this calling BASE_URL instead of AUTH_URL, we are throwing me out here need to discuss this one with AC
        
        .exec(
            http("Open account details page")
          
            .get(session -> "https://opal-frontend.test.apps.hmcts.net/fines/account/defendant/"
            + session.getString("defendant_account_id")
            + "/details")   
            
            // .get("https://opal-frontend.test.apps.hmcts.net/fines/account/defendant/${defendant_account_id}/details")
                //don't know if we need the headers on the get?
               .headers(Headers.getHeaders(16))
                .check(status().is(200))
            
                //MH debugging
             .check(status().saveAs("actualStatus"))
            .check(bodyString().saveAs("responseBody"))
            
            )

//MH getting the referrer value for the later calls from this page
            .exec(session -> {
    String id = session.getString("defendant_account_id");
    return session.set(
        "detailsPageUrl",
        "https://opal-frontend.test.apps.hmcts.net/fines/account/defendant/" + id + "/details");
            })

            //MH debugging
            .exec(session -> {

    System.out.println("===== HEADER SUMMARY DEBUG =====");
    System.out.println("URL defendant_account_id = " +
        session.getString("defendant_account_id"));

    System.out.println("STATUS = " +
        session.getString("actualStatus"));

    System.out.println("BODY = " +
        session.getString("responseBody"));

    return session;
    })
                 
                .pause(15,30)

            .exec(
            http("Load header summary")
                .get("https://opal-frontend.test.apps.hmcts.net/opal-fines-service/defendant-accounts/" + "#{defendant_account_id}" + "/header-summary")
                //don't know if we need the headers on the get?
                .headers(Headers.getHeaders(17))
                .header("Referer", "#{detailsPageUrl}")
                .check(status().is(200))
                //MH This is where we check if we have a Fixed Penalty account or not
                .check(jsonPath("$.account_type").saveAs("account_type"))
                //turns out we also need the party ID
                .check(jsonPath("$.defendant_account_party_id").saveAs("defendant_account_party_id"))
                )
            
                .exec(
            http("Load at a glance")
                .get("https://opal-frontend.test.apps.hmcts.net/opal-fines-service/defendant-accounts/" + "#{defendant_account_id}" + "/at-a-glance")
                //don't know if we need the headers on the get?
                .headers(Headers.getHeaders(17))
                .header("Referer", "#{detailsPageUrl}")
                .check(status().is(200))
        )

        .pause(15,30)

        .exec(
            http("Load Defendant")
            //Party ID needed here as well as the Defendant ID -DefID works for Fixed pen but not the other types for some reason?
               // .get("https://opal-frontend.test.apps.hmcts.net/opal-fines-service/defendant-accounts/" + "#{defendant_account_id}" + "/details#defendant")
                .get("https://opal-frontend.test.apps.hmcts.net/opal-fines-service/defendant-accounts/" + "#{defendant_account_id}" + "/defendant-account-parties/" + "#{defendant_account_party_id}")
                //don't know if we need the headers on the get?
                .headers(Headers.getHeaders(17))
                .header("Referer", "#{detailsPageUrl}")
                .check(status().is(200))
        )

        .pause(15,30)

          .exec(
            http("Load Payment Terms")
                //.get("https://opal-frontend.test.apps.hmcts.net/opal-fines-service/defendant-accounts/" + "#{defendant_account_id}" + "/details#payment-terms")
                .get("https://opal-frontend.test.apps.hmcts.net/opal-fines-service/defendant-accounts/" + "#{defendant_account_id}" + "/payment-terms/latest")
                //don't know if we need the headers on the get?
                .headers(Headers.getHeaders(17))
                .header("Referer", "#{detailsPageUrl}")
                .check(status().is(200))
        )

        .pause(15,30)

          .exec(
            http("Load Enforcement")
                //.get("https://opal-frontend.test.apps.hmcts.net/opal-fines-service/defendant-accounts/" + "#{defendant_account_id}" + "/details#enforcement")
                .get("https://opal-frontend.test.apps.hmcts.net/opal-fines-service/defendant-accounts/" + "#{defendant_account_id}" + "/enforcement-status")
                //don't know if we need the headers on the get?
                .headers(Headers.getHeaders(17))
                .header("Referer", "#{detailsPageUrl}")
                .check(status().is(200))
        )

         .pause(15,30)

//impositions and History notes are not recording for some reason, may need to confirm development and permissions
//HOWEVER there doesn't appear to be any data on those tabs on the accounts I've looked at so it may be as simple as that and the get requetss would be fine?

          .exec(
            http("Load Impositions")
                .get("https://opal-frontend.test.apps.hmcts.net/opal-fines-service/defendant-accounts/" + "#{defendant_account_id}" + "/details#impositions")
                //don't know if we need the headers on the get?
                .headers(Headers.getHeaders(17))
                .header("Referer", "#{detailsPageUrl}")
                .check(status().is(200))
        )

         .pause(15,30)

          .exec(
            http("Load History")
                .get("https://opal-frontend.test.apps.hmcts.net/opal-fines-service/defendant-accounts/" + "#{defendant_account_id}" + "/details#history-and-notes")
                //don't know if we need the headers on the get?
                .headers(Headers.getHeaders(17))
                .header("Referer", "#{detailsPageUrl}")
                .check(status().is(200))
        )

.pause(15,30)

//if this is a Fixed penalty account (from the header) then also go to the fixed penalty page
        .doIf(session -> "Fixed Penalty".equals(session.getString("account_type"))).then(

    exec(
        http("Load fixed penalty")
            .get("https://opal-frontend.test.apps.hmcts.net/opal-fines-service/defendant-accounts/" + "#{defendant_account_id}" + "/fixed-penalty")
            
            .headers(Headers.getHeaders(17))
            .header("Referer", "#{detailsPageUrl}")
            .check(status().is(200))
    )
        )

    ); //this is the end of return group("R1b View Defendant") MH-(The semi-colon is very important) (guess what compile error I had)
}

}