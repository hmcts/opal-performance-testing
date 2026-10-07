package simulations.Scripts.Scenario.SearchAccounts;

import simulations.Scripts.Headers.Headers;
import simulations.Scripts.RequestBodyBuilder.RequestBodyBuilderR1b;
import simulations.Scripts.Utilities.AppConfig;
import simulations.Scripts.Utilities.ContentDigestGenerator;
import io.gatling.javaapi.core.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class R1bDefendantAmmendmentsScenario {

public static ChainBuilder R1bDefendantAmmendmentsRequest() {

    //must pass in ${defendant_account_id} from the Search Account
    return group("R1b View Defendant Ammendments")
    .on(        
        exec(
            http("OPAL - Sso - Authenticated")
                .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                .headers(Headers.getHeaders(11))
        )         
        .exec(
            http("Open account details page")          
            .get(session -> AppConfig.UrlConfig.BASE_URL + "/fines/account/defendant/" + session.getString("defendant_account_id") + "/details")   
            
            // .get(AppConfig.UrlConfig.BASE_URL + " /fines/account/defendant/${defendant_account_id}/details")
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
                    AppConfig.UrlConfig.BASE_URL + "/fines/account/defendant/" + id + "/details");
            }
        )
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
            }
        )
                 
        .pause(15,30)

        .exec(
            http("Load header summary")
                .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/defendant-accounts/#{defendant_account_id}/header-summary")
                //don't know if we need the headers on the get?
                .headers(Headers.getHeaders(17))
                .header("Referer", "#{detailsPageUrl}")
                .check(status().is(200))
                //MH This is where we check if we have a Fixed Penalty account or not
                .check(jsonPath("$.account_type").saveAs("account_type"))
                //turns out we also need the party ID
                .check(jsonPath("$.defendant_account_party_id").saveAs("defendant_account_party_id"))
                .check(jsonPath("$.business_unit_summary.business_unit_id").find().saveAs("getBusinessUnitId"))
        )
        
        .exec(
            http("Load at a glance")
                .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/defendant-accounts/#{defendant_account_id}/at-a-glance")
                //don't know if we need the headers on the get?
                .headers(Headers.getHeaders(17))
                .header("Referer", "#{detailsPageUrl}")
                .check(status().is(200))
                .check(header("ETag").saveAs("etag"))

        )

        .pause(15,30)

        .exec(session -> {
            try {
                String addNoteRequestPayload =
                RequestBodyBuilderR1b.DefendantAccountSearch.buildAddNoteRequestBody(session);

                // System.out.println("Enforcement: " + addNoteRequestPayload);
                
                // Create SHA-512 digest
                String contentDigest =
                    ContentDigestGenerator.generateSha512ContentDigest(
                        addNoteRequestPayload
                    );

                ObjectMapper mapper = new ObjectMapper();

                // Convert directly into JsonNode WITHOUT readTree
                JsonNode json = mapper.readValue(addNoteRequestPayload, JsonNode.class);

                return session
                    .set("addNoteRequestPayload", addNoteRequestPayload)
                    .set("contentDigest", contentDigest);

                } catch (Exception e) {
                    System.err.println("Payload parsing failed: " + e.getMessage());
                    return session.markAsFailed();
                }
            }
        )            
        .exec(
            http("OPAL - Opal-fines-service - Notes - Add")
            .post(AppConfig.UrlConfig.BASE_URL + " /opal-fines-service/notes/add")
               .headers(Headers.getHeaders(22))
               .body(StringBody(session -> session.get("addNoteRequestPayload"))).asJson()
               .check(status().is(200))                       
        )

        .exec(
            http("Load Defendant")
            //Party ID needed here as well as the Defendant ID -DefID works for Fixed pen but not the other types for some reason?
               // .get(AppConfig.UrlConfig.BASE_URL + " /opal-fines-service/defendant-accounts/" + "#{defendant_account_id}" + "/details#defendant")
                .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/defendant-accounts/#{defendant_account_id}/defendant-account-parties/#{defendant_account_party_id}")
                //don't know if we need the headers on the get?
                .headers(Headers.getHeaders(17))
                .header("Referer", "#{detailsPageUrl}")
                .check(status().is(200))
        )

        .pause(15,30)

          .exec(
            http("Load Payment Terms")
                //.get(AppConfig.UrlConfig.BASE_URL + " /opal-fines-service/defendant-accounts/" + "#{defendant_account_id}" + "/details#payment-terms")
                .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/defendant-accounts/#{defendant_account_id}/payment-terms/latest")
                //don't know if we need the headers on the get?
                .headers(Headers.getHeaders(17))
                .header("Referer", "#{detailsPageUrl}")
                .check(status().is(200))
        )

        .pause(15,30)

        .exec(
            http("Load Enforcement")
                //.get(AppConfig.UrlConfig.BASE_URL + " /opal-fines-service/defendant-accounts/" + "#{defendant_account_id}" + "/details#enforcement")
                .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/defendant-accounts/#{defendant_account_id}/enforcement-status")
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
                .get(AppConfig.UrlConfig.BASE_URL + " /opal-fines-service/defendant-accounts/#{defendant_account_id}/details#impositions")
                //don't know if we need the headers on the get?
                .headers(Headers.getHeaders(17))
                .header("Referer", "#{detailsPageUrl}")
                .check(status().is(200))
        )

        .pause(15,30)

        .exec(
            http("Load History")
                .get(AppConfig.UrlConfig.BASE_URL + " /opal-fines-service/defendant-accounts/#{defendant_account_id}/details#history-and-notes")
                //don't know if we need the headers on the get?
                .headers(Headers.getHeaders(17))
                .header("Referer", "#{detailsPageUrl}")
                .check(status().is(200))
        )

        .pause(15,30)

        //if this is a Fixed penalty account (from the header) then also go to the fixed penalty page
        .doIf(session -> "Fixed Penalty".equals(session.getString("account_type")))
            .then(
                exec(
                http("Load fixed penalty")
                    .get(AppConfig.UrlConfig.BASE_URL + " /opal-fines-service/defendant-accounts/#{defendant_account_id}/fixed-penalty")
                    
                    .headers(Headers.getHeaders(17))
                    .header("Referer", "#{detailsPageUrl}")
                    .check(status().is(200))
                )
            )
        ); //this is the end of return group("R1b View Defendant") MH-(The semi-colon is very important) (guess what compile error I had)
    }
}