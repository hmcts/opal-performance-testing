package simulations.Scripts.Scenario.CreateAccounts;

import simulations.Scripts.Headers.Headers;
import simulations.Scripts.Utilities.AccountCounters;
import simulations.Scripts.Utilities.AppConfig;
import simulations.Scripts.Utilities.DataGenerator;
import simulations.Scripts.Utilities.Feeders;
import simulations.Scripts.Utilities.UserInfoLogger;
import io.gatling.javaapi.core.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import simulations.Scripts.RequestBodyBuilder.RequestBodyBuilder;
import simulations.Scripts.ScenarioBuilder.DraftAccountQueryBuilder;

public final class CreateAccountFineScenario {

    private CreateAccountFineScenario() {}

    public static ChainBuilder CreateAccountFineRequest() {

        return group("OPAL Create Manual Account").on(
            //Selecting Account tab:

                exec(
                    http("OPAL - Sso - Authenticated")
                        .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                        .headers(Headers.getHeaders(11))
                        .check(status().is(200))                                         
                )  
  
            //Create and Manage Draft Accounts
                .exec(
                    http("OPAL - Sso - Authenticated")
                        .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                        .headers(Headers.getHeaders(11))
                        .check(status().is(200))                                         
                )  
                .exec(
                    http("OPAL - Sso - Authenticated")
                        .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                        .headers(Headers.getHeaders(11))
                        .check(status().is(200))                                         
                ) 
            // Displays the created accounts by filters.    
            //Build draft account query parameters from business unit data in session (Submitted / Resubmitted) 
                
                .exec(session ->
                    DraftAccountQueryBuilder.buildAndStore(
                        session,
                        "draftAccountSubmittedQueryParams",
                        List.of("Submitted", "Resubmitted"),
                        "not_submitted_by",
                       false
                    )
                )
                .exec(
                    http("OPAL - Opal-fines-service - Draft-accounts - QueryParams")
                        .get(session ->
                            AppConfig.UrlConfig.BASE_URL +
                            "/opal-fines-service/draft-accounts?" +
                            session.getString("draftAccountSubmittedQueryParams")
                        )
                        .headers(Headers.getHeaders(11))
                    .check(status().saveAs("httpStatus"))
                    .check(status().is(200))
                    .check(Feeders.saveErrorDetails())
                )
                .exec(UserInfoLogger.logDetailedErrorMessage("OPAL - Opal-fines-service - Draft-accounts - QueryParams"))
                .exitHereIfFailed()

                //Build draft account query parameters from business unit data in session (Publishing Failed)               

                .exec(session ->
                    DraftAccountQueryBuilder.buildAndStore(
                        session,
                        "draftAccountFailedQueryParams",
                        List.of("Publishing Failed"),
                        "not_submitted_by",
                       false
                    )
                )                
                .exec(
                    http("OPAL - Opal-fines-service - Draft-accounts - QueryParams")
                        .get(session ->
                            AppConfig.UrlConfig.BASE_URL +
                            "/opal-fines-service/draft-accounts?" +
                            session.getString("draftAccountFailedQueryParams")
                        )
                        .headers(Headers.getHeaders(11))
                        .check(status().is(200))
                )
                .exitHereIfFailed() 

                //Second call for draft account query parameters from business unit data in session (Publishing Failed)  
                .exec(
                    http("OPAL - Opal-fines-service - Draft-accounts - QueryParams")
                        .get(session ->
                            AppConfig.UrlConfig.BASE_URL +
                            "/opal-fines-service/draft-accounts?" +
                            session.getString("draftAccountSubmittedQueryParams")
                        )
                        .headers(Headers.getHeaders(11))
                        .check(status().is(200))
                              .check(
                                   jsonPath("$.summaries").findAll().saveAs("summaries"))
                                // jsonPath("$.summaries[*].draft_account_id").findAll().saveAs("draftAccountIds"),
                                // jsonPath("$.summaries[*].business_unit_id").findAll().saveAs("businessUnitIds"),
                                // jsonPath("$.summaries[*].account_status").findAll().saveAs("accountStatuses"),
                                // jsonPath("$.summaries[*].submitted_by").findAll().saveAs("submittedBys"),
                                // jsonPath("$.summaries[*].submitted_by_name").findAll().saveAs("submittedByNames")
                            )                       
                
                .exec(session -> {

                    List<String> summaries = session.getList("summaries");

                    if (summaries == null || summaries.isEmpty()) {
                        return session.markAsFailed();
                    }

                    String rawJson = summaries.get(0); // usually only one array wrapper

                    try {
                        ObjectMapper mapper = new ObjectMapper();

                        // STEP 1: parse outer array
                        JsonNode arrayNode = mapper.readTree(rawJson);

                        if (!arrayNode.isArray() || arrayNode.size() == 0) {
                            System.err.println("Summaries array is empty or invalid");
                            return session.markAsFailed();
                        }

                        // STEP 2: pick a real summary object inside array
                        JsonNode node = arrayNode.get(
                            ThreadLocalRandom.current().nextInt(arrayNode.size())
                        );

                        return session
                            .set("selectedDraftAccountId", node.get("draft_account_id").asText())
                            .set("selectedBusinessUnitId", node.get("business_unit_id").asText())
                            .set("accountStatus", node.get("account_status").asText())
                            .set("submittedBy", node.get("submitted_by").asText())
                            .set("submittedByName", node.get("submitted_by_name").asText());

                    } catch (Exception e) {
                        System.err.println("Failed to parse summaries JSON: " + rawJson);
                        e.printStackTrace();
                        return session.markAsFailed();
                    }
                })

                //Selecting Manual create account
                .exec(
                    http("OPAL - Sso - Authenticated")
                        .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                        .headers(Headers.getHeaders(11))
                        .check(status().is(200))                                         
                )                
                .exec(
                    http("OPAL - Sso - Authenticated")
                        .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                        .headers(Headers.getHeaders(11))
                        .check(status().is(200))                                         
                )
                .exitHereIfFailed() 

                .exec(session -> {
                    // Retrieve business unit IDs and corresponding user IDs from the session
                    List<Integer> businessUnitIds = session.getList("businessUnitIds");
                    List<String> businessUnitUserIds = session.getList("businessUnitUserIds");

                    // Generate a random index based on the size of the business unit list
                    int index = java.util.concurrent.ThreadLocalRandom.current()
                        .nextInt(businessUnitIds.size());

                    return session
                        .set("selectedBusinessUnitId", businessUnitIds.get(index))
                        .set("selectedbusinessUnitUserIds", businessUnitUserIds.get(index));
                })

                //Selecting Create new account
                .pause(5,20)

                .exec(
                    http("OPAL - Sso - Authenticated")
                        .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                        .headers(Headers.getHeaders(11))
                        .check(status().is(200))                                         
                )
                .exec(
                    http("OPAL - Opal-fines-service - Business-units")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/business-units?permission=CREATE_MANAGE_DRAFT_ACCOUNTS")
                        .headers(Headers.getHeaders(12))
                        .check(status().is(200)) 
                        .check(Feeders.saveBusinessUnitId())
                )               
                .exitHereIfFailed()                       
           
                //Selecting Create new account
                .pause(5,20)

                .exec(
                    http("OPAL - Sso - Authenticated")
                        .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                        .headers(Headers.getHeaders(11))
                ) 
                
                //Selecting Court details
                .pause(5,20) 
                .exec(
                    http("OPAL - Opal-fines-service - Courts")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/courts?business_unit=#{selectedBusinessUnitId}")
                        .headers(Headers.getHeaders(12))
                        .check(Feeders.saveCourtId())                        

                )
                .exec(
                    http("OPAL - Opal-fines-service - Local-justice-areas")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/local-justice-areas")
                        .headers(Headers.getHeaders(12))
                ) 

                //Selecting Add Personal details
                .pause(5,20)
                .exec(
                    http("OPAL - Sso - Authenticated")
                    .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                    .headers(Headers.getHeaders(11))
                )
                .exec(
                    http("OPAL - API - Users-state")
                    .get(AppConfig.UrlConfig.BASE_URL + "/api/user-state")
                    .headers(Headers.getHeaders(12))
                )
                .exec(
                    http("OPAL - Sso - Authenticated")
                    .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                    .headers(Headers.getHeaders(11))
                ) 

                //Selecting Add Defendant contact details
                .pause(5,20) 
                .exec(
                    http("OPAL - Sso - Authenticated")
                    .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                    .headers(Headers.getHeaders(11))
                ) 

                //Selecting Add Employer details 
                .pause(5,20)                       
                .exec(
                    http("OPAL - Sso - Authenticated")
                    .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                    .headers(Headers.getHeaders(11))
                )

                //Selecting Add an offence 
                .pause(5,20)        
                .exec(
                    http("OPAL - Opal-fines-service - Results")
                    .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/results?result_ids=FCOMP&result_ids=FVS&result_ids=FCOST&result_ids=FCPC&result_ids=FO&result_ids=FCC&result_ids=FVEBD&result_ids=FFR")
                    .headers(Headers.getHeaders(12))
                )
                .exec(
                    http("OPAL - Opal-fines-service - Major-creditors")
                    .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/major-creditors?businessUnit=#{selectedBusinessUnitId}")
                    .headers(Headers.getHeaders(12))
                )
                .pause(5,20)
                .exec(session -> {
                    String offence = DataGenerator.generateRandomOFFENCE();
                    return session.set("offenceCode", offence);
                })

                //This is added once entered a offence.
                .exec(
                    http("OPAL - Opal-fines-service - Offences")
                    .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/offences?q=#{offenceCode}")
                    .headers(Headers.getHeaders(12))
                    .check(status().saveAs("httpStatus"))
                    .check(status().is(200))
                )
                .exec(UserInfoLogger.logDetailedErrorMessage("OPAL - Opal-fines-service - Offences"))
                .exitHereIfFailed()  

                //Selecting Review offence        
                .pause(5,20) 
                .exec(
                    http("OPAL - API - Users-state")
                    .get(AppConfig.UrlConfig.BASE_URL + "/api/user-state")
                    .headers(Headers.getHeaders(12))
                )

                //Selecting Add Payment terms  
                .pause(5,20)    
                .exec(
                    http("OPAL - Sso - Authenticated")
                    .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                    .headers(Headers.getHeaders(11))
                )            

                //Selecting Account comments and notes
                .pause(5,20) 
                .exec(
                    http("OPAL - Sso - Authenticated")
                    .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                    .headers(Headers.getHeaders(11))
                )   
                                
                //Selecting Review account
                .pause(5,20) 
                .exec(
                    http("OPAL - Sso - Authenticated")
                    .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                    .headers(Headers.getHeaders(11))
                ) 
                .exec(
                    http("OPAL - Sso - Authenticated")
                    .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                    .headers(Headers.getHeaders(11))
                )                                 
                .exec(
                    http("OPAL - Sso - Authenticated")
                    .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                    .headers(Headers.getHeaders(11))
                ) 
                .exec(
                    http("OPAL - Opal-fines-service - Major-creditors")
                    .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/prosecutors?business_unit=#{selectedBusinessUnitId}")
                    .headers(Headers.getHeaders(12))
                    // .check(Feeders.saveProsecutorId())  
                    // .check(Feeders.saveProsecutors()) 
                    .check(
                        jsonPath("$.ref_data[*].prosecutor_id").findAll().saveAs("prosecutorIds"),
                        jsonPath("$.ref_data[*].name").findAll().saveAs("prosecutorNames")
                    )

                ) 
                                .exec(session -> {
                    // Retrieve lists of prosecutor IDs and names from the Gatling session
                    List<Integer> prosecutorIds = session.getList("prosecutorIds");
                    List<String> prosecutorNames = session.getList("prosecutorNames");
                    //log it and return the session unchanged to avoid runtime errors
                    if (prosecutorIds == null || prosecutorIds.isEmpty()) {
                        System.out.println("No prosecutors found!");
                        return session;
                    }
                    // Generate a random index based on the size of the prosecutor list
                    int index = ThreadLocalRandom.current().nextInt(prosecutorIds.size());
                    // Store the randomly selected prosecutor ID and name back into the session for use in later requests
                    return session
                        .set("selectedProsecutorId", prosecutorIds.get(index))
                        .set("selectedProsecutorName", prosecutorNames.get(index));
                })
                .exec(session -> {
                    // Retrieve business unit IDs and corresponding user IDs from the session
                    List<Integer> businessUnitIds = session.getList("businessUnitIds");
                    List<String> businessUnitUserIds = session.getList("businessUnitUserIds");

                    // Generate a random index
                    int index = java.util.concurrent.ThreadLocalRandom.current()
                        .nextInt(businessUnitIds.size());

                    return session
                        .set("selectedProsecutorId", businessUnitIds.get(index))
                        .set("selectedProsecutorName", businessUnitUserIds.get(index));
                })                                        

              .exec(session -> {
                    try {
                        String draftAccountRequestPayload =
                            RequestBodyBuilder.BuildDraftAccountFineRequestBody(session);

                        ObjectMapper mapper = new ObjectMapper();

                        // Convert directly into JsonNode WITHOUT readTree
                        JsonNode json = mapper.readValue(draftAccountRequestPayload, JsonNode.class);

                        String accountType = json.has("account_type")
                            ? json.get("account_type").asText()
                            : "UNKNOWN";

                        String businessUnitId = json.has("business_unit_id")
                            ? json.get("business_unit_id").asText()
                            : "UNKNOWN";

                        return session
                            .set("draftAccountRequestPayload", draftAccountRequestPayload)
                            .set("createdAccountType", accountType)
                            .set("createdBusinessUnitId", businessUnitId);

                    } catch (Exception e) {
                        System.err.println("Payload parsing failed: " + e.getMessage());
                        return session.markAsFailed();
                    }
                })
                
                //Selecting Submit for review
                .pause(20,60)
                .exec(
                    http("OPAL - Opal-fines-service - Draft-accounts")
                    .post(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/draft-accounts")
                    .headers(Headers.getHeaders(14)) 
                    .body(StringBody(session -> session.get("draftAccountRequestPayload"))).asJson()
                    .check(status().saveAs("httpStatus"))
                    .check(status().is(201))
                    .check(Feeders.saveErrorDetails()) 
                    .check(Feeders.saveCreatedAccountId())

                )
                .exec(session -> {
                    AccountCounters.TOTAL_CREATED.incrementAndGet();
                    AccountCounters.FINE_CREATED.incrementAndGet();
                    return session;
                })
                .exec(UserInfoLogger.logDetailedErrorMessage("OPAL - Opal-fines-service - Draft-accounts"))
                .exitHereIfFailed() 

                // =======================================================
                // CUSTOM LOGGING SECTION
                // =======================================================
                .exec(session -> {

                    int count = session.getInt("createdAccountCount") + 1;

                    System.out.println(
                        "\n========== DRAFT ACCOUNT CREATED ==========\n" +
                        "User: " + session.getString("username") + "\n" +
                        "Account Type: " + session.getString("createdAccountType") + "\n" +
                        "Business Unit ID: " + session.getString("createdBusinessUnitId") + "\n" +
                        "New Created Account ID: " + session.getString("getCreatedAccountId") + "\n" +
                        "Created Account Count: " + count + "\n" +
                        "===========================================\n"
                    );

                    return session.set("createdAccountCount", count);
                })
                .exec(
                    http("OPAL - Sso - Authenticated")
                    .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                    .headers(Headers.getHeaders(11))
                )        
        );            
    }
}
                     


   