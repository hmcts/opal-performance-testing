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
public final class R1bDefendantViewScenario {

public static ChainBuilder ViewDefendant() {

    //must pass in ${defendant_account_id} from the Search Account
    return group("R1b View Defendant").on(

        group("Header-summary")
        .on(
        //authentication is what you need
            exec(
                http("OPAL - Sso - Authenticated")
                .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                .headers(Headers.getHeaders(11))
            )        
            //Open account details page
            .exec(
                http("OPAL - Fines - Account - Defendant - Details")
                    .get(AppConfig.UrlConfig.BASE_URL + "/fines/account/defendant/#{defendant_account_id}/details")
                    .check(status().is(200))
            )         

            //MH getting the referrer value for the later calls from this page
            .exec(session -> {
                    String id = session.getString("defendant_account_id");
                    return session.set(
                    "detailsPageUrl",
                    AppConfig.UrlConfig.BASE_URL + "/fines/account/defendant/" + id + "/details");
                    }
                )
                   
            .pause(15,30)
            //Load header summary
            .exec(
                http("OPAL - Defendant-accounts - Header-summary")
                    .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/defendant-accounts/#{defendant_account_id}/header-summary")
                    .headers(Headers.getHeaders(17))
                    .check(status().is(200))
                    //MH This is where we check if we have a Fixed Penalty account or not
                    .check(jsonPath("$.account_type").saveAs("account_type"))
                    //turns out we also need the party ID
                    .check(jsonPath("$.defendant_account_party_id").saveAs("defendant_account_party_id"))
                    .check(jsonPath("$.business_unit_summary.business_unit_id").find().saveAs("getBusinessUnitId"))
            )
            
        )
        .pause(15,30)
        .group("At-a-glance")
        .on(             
        //Load at a glance
            exec(
                http("OPAL - Defendant-accounts - At-a-glance")
                    .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/defendant-accounts/#{defendant_account_id}/at-a-glance")
                    //don't know if we need the headers on the get?
                    .headers(Headers.getHeaders(17))
                    .check(status().is(200))
                    .check(header("ETag").saveAs("etag") 
                )
            )
        

            .pause(15,30)
            //Load Defendant
            .exec(
                http("OPAL - Opal-fines-service - Defendant-accounts - Defendant-account-parties")
                //Party ID needed here as well as the Defendant ID -DefID works for Fixed pen but not the other types for some reason?
                    .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/defendant-accounts/#{defendant_account_id}/defendant-account-parties/#{defendant_account_party_id}")
                    .headers(Headers.getHeaders(17))
                    .check(status().is(200))
            )
        )
        .group("Add Note")
        .on(
            exec(session -> {
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
                .post(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/notes/add")
                .headers(Headers.getHeaders(21))
                .body(StringBody(session -> session.get("addNoteRequestPayload"))).asJson()
                .check(status().is(201))                       
            )
        )

        .pause(15,30)
        .group("Payment Terms")
        .on(  
        //Load Payment Terms
            exec(
                http("OPAL - Opal-fines-service - Defendant-accounts - Payment-terms - Latest")
                    .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/defendant-accounts/#{defendant_account_id}/payment-terms/latest")
                    .headers(Headers.getHeaders(17))
                    .check(status().is(200))
            )
        )

        .pause(15,30)
        .group("Enforcement")
        .on(         
        //Load Enforcement
            exec(
                http("OPAL - Opal-fines-service - Defendant-accounts - Enforcement-status")
                    .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/defendant-accounts/#{defendant_account_id}/enforcement-status")
                    .headers(Headers.getHeaders(17))
                    .check(status().is(200))
            )
        )

        //impositions and History notes are not recording for some reason, may need to confirm development and permissions
        //HOWEVER there doesn't appear to be any data on those tabs on the accounts I've looked at so it may be as simple as that and the get requetss would be fine?
        
        .pause(15,30)
        .group("Impositions")
        .on(        
        //Load Impositions

            exec(
            http("OPAL - Opal-fines-service - Defendant-accounts - Impositions")
                .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/defendant-accounts/#{defendant_account_id}/impositions")
                .headers(Headers.getHeaders(17))
                .check(status().is(200))
            )
        )

        .pause(15,30)
        .group("History")
        .on(         
        //Load History
            exec(
            http("OPAL - Opal-fines-service - Defendant-accounts - History")
                .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/defendant-accounts/#{defendant_account_id}/history")
                .headers(Headers.getHeaders(17))
                .check(status().is(200))
            )
        )
        //if this is a Fixed penalty account (from the header) then also go to the fixed penalty page
        .pause(15,30)
        .group("Fixed Penalty")
        .on( 
        //Load fixed penalty
            doIf(session -> "Fixed Penalty".equals(session.getString("account_type")))
            .then(

                exec(
                    http("OPAL - Opal-fines-service - Defendant-accounts - Fixed-penalty")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/defendant-accounts/#{defendant_account_id}/fixed-penalty")                    
                        .headers(Headers.getHeaders(17))
                        .check(status().is(200))
                )
            )
        ));
    }
}