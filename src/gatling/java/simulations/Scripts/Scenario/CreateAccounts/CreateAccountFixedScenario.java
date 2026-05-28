package simulations.Scripts.Scenario.CreateAccounts;

import simulations.Scripts.Headers.Headers;
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

import simulations.Scripts.RequestBodyBuilder.RequestBodyBuilder;
import simulations.Scripts.ScenarioBuilder.DraftAccountQueryBuilder;

public final class CreateAccountFixedScenario {

    private CreateAccountFixedScenario() {}

    public static ChainBuilder CreateAccountFixedRequest() {

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
                    http("OPAL - Opal-fines-service - Draft-accounts")
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
                .exec(UserInfoLogger.logDetailedErrorMessage("OPAL - Opal-fines-service - Draft-accounts"))
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
                    http("OPAL - Opal-fines-service - Draft-accounts")
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
                    http("OPAL - Opal-fines-service - Draft-accounts")
                        .get(session ->
                            AppConfig.UrlConfig.BASE_URL +
                            "/opal-fines-service/draft-accounts?" +
                            session.getString("draftAccountSubmittedQueryParams")
                        )
                        .headers(Headers.getHeaders(11))
                        .check(status().is(200))
                        .check(
                                jsonPath("$.summaries[*].draft_account_id").findAll().saveAs("draftAccountIds"),
                                jsonPath("$.summaries[*].business_unit_id").findAll().saveAs("businessUnitIds"),
                                jsonPath("$.summaries[*].account_status").findAll().saveAs("accountStatuses"),
                                jsonPath("$.summaries[*].submitted_by").findAll().saveAs("submittedBys"),
                                jsonPath("$.summaries[*].submitted_by_name").findAll().saveAs("submittedByNames")
                            )

                        )
                
                .exec(session -> {

                    List<Integer> draftAccountIds = session.getList("draftAccountIds");
                    List<Integer> businessUnitIds = session.getList("businessUnitIds");
                    List<String> accountStatuses = session.getList("accountStatuses");
                    List<String> submittedBys = session.getList("submittedBys");
                    List<String> submittedByNames = session.getList("submittedByNames");

                    if (draftAccountIds == null || draftAccountIds.isEmpty()) {
                        return session.markAsFailed();
                    }

                  // Generate a random index
                    int index = java.util.concurrent.ThreadLocalRandom.current()
                        .nextInt(businessUnitIds.size());
                        
                    return session
                        .set("selectedDraftAccountId", draftAccountIds.get(index))
                        .set("selectedBusinessUnitId", businessUnitIds.get(index))
                        .set("accountStatus", accountStatuses.get(index))
                        .set("submittedBy", submittedBys.get(index))
                        .set("submittedByName", submittedByNames.get(index));
                    }
                )

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
                    List<Integer> businessUnitIds = session.getList("businessUnitIds");
                    List<String> businessUnitUserIds = session.getList("businessUnitUserIds");

                    // Generate a random index
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

                //Selecting Business Unit and Fixed Penalty                     
                .pause(5,20)
                .exec(
                    http("OPAL - Sso - Authenticated")
                        .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                        .headers(Headers.getHeaders(11))
                )  
                .exec(          
                    http("OPAL - Opal-fines-service - Prosecutors")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/prosecutors?business_unit=#{selectedBusinessUnitId}")
                        .headers(Headers.getHeaders(12))
                 //   .check(Feeders.saveProsecutorId())  
                //    .check(Feeders.saveProsecutors()) 
                        .check(
                        jsonPath("$.ref_data[*].prosecutor_id").findAll().saveAs("prosecutorIds"),
                        jsonPath("$.ref_data[*].name").findAll().saveAs("prosecutorNames")
                    )

                ) 

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
                    
                //Entering Fixed Penalty details                    
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

                .exec(
                    http("OPAL - Opal-user-service - Users - 0 - State")
                    .get(AppConfig.UrlConfig.BASE_URL + "/opal-user-service/users/0/state")
                    .headers(Headers.getHeaders(12))
                )
                .exec(
                    http("OPAL - Sso - Authenticated")
                    .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                    .headers(Headers.getHeaders(11))
                )
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

                /// Checking fixed penalty account details - Selecting Submit for Review
                .pause(20,60)

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
                     // Store the generated payload in the session
                    String draftAccountRequestPayload = RequestBodyBuilder.BuildDraftAccountRequestBody(session);
                //    System.out.println("draftAccountRequestPayload = " + session.getString("draftAccountRequestPayload"));
                    return session.set("draftAccountRequestPayload", draftAccountRequestPayload);
                })
                .exec(
                    http("OPAL - Opal-fines-service - Draft-accounts")
                    .post(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/draft-accounts")
                    .headers(Headers.getHeaders(14))
                    .body(StringBody(session -> session.get("draftAccountRequestPayload"))).asJson()
                    .check(status().saveAs("httpStatus"))
                    .check(status().is(201)) 
                    .check(Feeders.saveErrorDetails()) 
                )
                .exec(UserInfoLogger.logDetailedErrorMessage("OPAL - Opal-fines-service - Draft-accounts"))
                .exitHereIfFailed() 
                
                .exec(
                    http("OPAL - Sso - Authenticated")
                    .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                    .headers(Headers.getHeaders(11))
                )        
        );            
    }
}
                     


   