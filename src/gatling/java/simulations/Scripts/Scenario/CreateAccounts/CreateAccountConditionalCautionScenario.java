package simulations.Scripts.Scenario.CreateAccounts;

import simulations.Scripts.Headers.Headers;
import simulations.Scripts.Utilities.AccountCounters;
import simulations.Scripts.Utilities.AppConfig;
import simulations.Scripts.Utilities.ContentDigestGenerator;
import simulations.Scripts.Utilities.DataGenerator;
import simulations.Scripts.Utilities.DraftAccountPayloadProcessor;
import simulations.Scripts.Utilities.Feeders;
import simulations.Scripts.Utilities.UserInfoLogger;
import io.gatling.javaapi.core.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import simulations.Scripts.RequestBodyBuilder.RequestBodyBuilder;
import simulations.Scripts.ScenarioBuilder.Testing.DraftAccountQueryBuilder;

public final class CreateAccountConditionalCautionScenario {

    private CreateAccountConditionalCautionScenario() {}

    public static ChainBuilder CreateAccountConditionalCautionRequest() {

        return group("OPAL Create Manual Conditional Caution Account")
        .on(
            group("Create and Manage")
            .on(

                //Selecting Account tab:
                exec(
                    http("OPAL - API - Users-state")
                        .get(AppConfig.UrlConfig.BASE_URL + "/api/user-state")
                        .headers(Headers.getHeaders(12))
                        .check(status().saveAs("httpStatus"))
                        .check(status().is(200))
                )
                .exec(
                    http("OPAL - Sso - Authenticated")
                        .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                        .headers(Headers.getHeaders(11))
                        .check(status().saveAs("httpStatus"))
                        .check(status().is(200))
                )
                .exec(UserInfoLogger.logDetailedErrorMessage("OPAL - Sso - Authenticated"))
                .exitHereIfFailed()
               
                //Selecting Create and Manage Draft Accounts link               
                .exec(
                    http("OPAL - Sso - Authenticated")
                        .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                        .headers(Headers.getHeaders(11))
                        .check(status().saveAs("httpStatus"))
                        .check(status().is(200))
                )
                .exec(UserInfoLogger.logDetailedErrorMessage("OPAL - Sso - Authenticated"))
                .exitHereIfFailed()    
                .exec(
                    http("OPAL - Sso - Authenticated")
                        .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                        .headers(Headers.getHeaders(11))
                        .check(status().saveAs("httpStatus"))
                        .check(status().is(200))
                )
                .exec(UserInfoLogger.logDetailedErrorMessage("OPAL - Sso - Authenticated"))
                .exitHereIfFailed()  
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
                    http("OPAL - Opal-fines-service - Draft-accounts - QueryParams - Submitted")
                        .get(session ->
                            AppConfig.UrlConfig.BASE_URL +
                            "/opal-fines-service/draft-accounts?" +
                            session.getString("draftAccountSubmittedQueryParams")
                        )
                        .headers(Headers.getHeaders(11))
                    .check(status().saveAs("httpStatus"))
                    .check(status().is(200))
                    .check(Feeders.saveErrorDetails())
                    .check(
                        jsonPath("$.summaries").findAll().saveAs("summaries")
                    )
                )
                .exec(UserInfoLogger.logDetailedErrorMessage("OPAL - Opal-fines-service - Draft-accounts - QueryParams"))
                .exitHereIfFailed()
               
                //Build draft account query parameters from business unit data in session (Rejected)               

                .exec(session ->
                    DraftAccountQueryBuilder.buildAndStore(
                        session,
                        "draftAccountRejectedQueryParams",
                        List.of("Rejected"),
                        "not_submitted_by",
                       false
                    )
                )                
                .exec(
                    http("OPAL - Opal-fines-service - Draft-accounts - QueryParams - Rejected")
                        .get(session ->
                            AppConfig.UrlConfig.BASE_URL +
                            "/opal-fines-service/draft-accounts?" +
                            session.getString("draftAccountRejectedQueryParams")
                        )
                        .headers(Headers.getHeaders(11))
                        .check(status().is(200))
                )
                .exitHereIfFailed()                
            ) 
            .group("Initiate Account Creation").on(

                //Selecting Manual create account
                pause(5,20)
                
                .exec(
                    http("OPAL - API - Users-state")
                        .get(AppConfig.UrlConfig.BASE_URL + "/api/user-state")
                        .headers(Headers.getHeaders(12))
                        .check(status().saveAs("httpStatus"))
                        .check(status().is(200))
                )
                .exec(
                    http("OPAL - Sso - Authenticated")
                        .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                        .headers(Headers.getHeaders(11))
                        .check(status().saveAs("httpStatus"))
                        .check(status().is(200))
                )
                .exec(UserInfoLogger.logDetailedErrorMessage("OPAL - Sso - Authenticated"))
                .exitHereIfFailed()  
                
                .exec(
                    http("OPAL - Sso - Authenticated")
                        .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                        .headers(Headers.getHeaders(11))
                        .check(status().saveAs("httpStatus"))
                        .check(status().is(200))
                )
                .exec(UserInfoLogger.logDetailedErrorMessage("OPAL - Sso - Authenticated"))
                .exitHereIfFailed() 

                //Selecting new account radio button
                .pause(5,20)
                
                .exec(
                    http("OPAL - API - Users-state")
                        .get(AppConfig.UrlConfig.BASE_URL + "/api/user-state")
                        .headers(Headers.getHeaders(12))
                        .check(status().saveAs("httpStatus"))
                        .check(status().is(200))
                )            
                .exec(
                    http("OPAL - Sso - Authenticated")
                        .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                        .headers(Headers.getHeaders(11))
                        .check(status().saveAs("httpStatus"))
                        .check(status().is(200))
                )
                .exec(
                    http("OPAL - Opal-fines-service - Business-units")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/business-units?permission=CREATE_MANAGE_DRAFT_ACCOUNTS")
                        .headers(Headers.getHeaders(12))
                        .check(status().is(200)) 
                        .check(Feeders.saveBusinessUnitId())

                )               
            )
            .group("Load Account Details")
            .on(
                //Selecting Bussiness unit and Account type, then selecting the Continue button
                pause(5,20)

                .exec(
                    http("OPAL - API - Users-state")
                        .get(AppConfig.UrlConfig.BASE_URL + "/api/user-state")
                        .headers(Headers.getHeaders(12))
                        .check(status().saveAs("httpStatus"))
                        .check(status().is(200))
                )
                .exec(
                    http("OPAL - Sso - Authenticated")
                        .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                        .headers(Headers.getHeaders(11))
                        .check(status().saveAs("httpStatus"))
                        .check(status().is(200))
                )
                .exec(UserInfoLogger.logDetailedErrorMessage("OPAL - Sso - Authenticated"))
                .exitHereIfFailed() 
            )
            .group("Police and court details")
            .on(

                //Selecting Police and Court details link and Entering details 
                exec(
                    http("OPAL - API - Users-state")
                    .get(AppConfig.UrlConfig.BASE_URL + "/api/user-state")
                    .headers(Headers.getHeaders(12))
                    .check(status().saveAs("httpStatus"))
                    .check(status().is(200))
                )
                .exec(
                    http("OPAL - Sso - Authenticated")
                    .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                    .headers(Headers.getHeaders(11))
                    .check(status().saveAs("httpStatus"))
                    .check(status().is(200))
                )               
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
                            .check(status().is(200))
                            .check(
                                jsonPath("$.refData[?(@.lja_type == 'CRWCRT')].local_justice_area_id")
                                    .findAll()
                                    .saveAs("localJusticeAreaIds")
                            )
                            .check(
                                jsonPath("$.refData[?(@.lja_type == 'CRWCRT')].name")
                                    .findAll()
                                    .saveAs("localJusticeAreaNames")
                            )
                    ) 
            )
            .group("Enter Personal Details")
            .on(

                //Selecting Add Personal details button and adding details
                pause(5,20)

                .exec(
                    http("OPAL - API - Users-state")
                    .get(AppConfig.UrlConfig.BASE_URL + "/api/user-state")
                    .headers(Headers.getHeaders(12))
                    .check(status().saveAs("httpStatus"))
                    .check(status().is(200))
                )
                .exec(
                    http("OPAL - Sso - Authenticated")
                    .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                    .headers(Headers.getHeaders(11))
                    .check(status().saveAs("httpStatus"))
                    .check(status().is(200))
                )
                .exec(UserInfoLogger.logDetailedErrorMessage("OPAL - Sso - Authenticated"))
                .exitHereIfFailed()    
            )
            .group("Enter Contact Details")
            .on(
                    
                //Selecting Add Contact details button and adding details

                pause(5,20)
                .exec(
                    http("OPAL - API - Users-state")
                    .get(AppConfig.UrlConfig.BASE_URL + "/api/user-state")
                    .headers(Headers.getHeaders(12))
                    .check(status().saveAs("httpStatus"))
                    .check(status().is(200))
                )
                .exec(
                    http("OPAL - Sso - Authenticated")
                    .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                    .headers(Headers.getHeaders(11))
                    .check(status().saveAs("httpStatus"))
                    .check(status().is(200))
                )
                .exec(UserInfoLogger.logDetailedErrorMessage("OPAL - Sso - Authenticated"))
                .exitHereIfFailed() 
            )

            .group("Enter Employer Details")
            .on(

                //Selecting Add Employer details button and adding details
                pause(5,20)     
                .exec(
                    http("OPAL - API - Users-state")
                    .get(AppConfig.UrlConfig.BASE_URL + "/api/user-state")
                    .headers(Headers.getHeaders(12))
                    .check(status().saveAs("httpStatus"))
                    .check(status().is(200))
                )   
                .exec(
                    http("OPAL - Sso - Authenticated")
                    .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                    .headers(Headers.getHeaders(11))
                    .check(status().saveAs("httpStatus"))
                    .check(status().is(200))
                )
                .exec(UserInfoLogger.logDetailedErrorMessage("OPAL - Sso - Authenticated"))
                .exitHereIfFailed() 
            )
            
            .group("Add Offence")
            .on(

                //Selecting Add offence details button and adding details
                pause(5,20) 
                
                .exec(
                    http("OPAL - API - Users-state")
                    .get(AppConfig.UrlConfig.BASE_URL + "/api/user-state")
                    .headers(Headers.getHeaders(12))
                    .check(status().saveAs("httpStatus"))
                    .check(status().is(200))
                )                
                .exec(
                    http("OPAL - Sso - Authenticated")
                    .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                    .headers(Headers.getHeaders(11))
                    .check(status().saveAs("httpStatus"))
                    .check(status().is(200))
                )
                .exec(UserInfoLogger.logDetailedErrorMessage("OPAL - Sso - Authenticated"))
                .exitHereIfFailed()
               
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
               
                .pause(3,5)                
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

            )
            .group("Review Offence and impositions Details")
            .on(
                
                //Selecting Review offence Button                
                pause(5,20)
                .exec(
                    http("OPAL - API - Users-state")
                    .get(AppConfig.UrlConfig.BASE_URL + "/api/user-state")
                    .headers(Headers.getHeaders(12))
                    .check(status().saveAs("httpStatus"))
                    .check(status().is(200))
                ) 
                .exec(
                    http("OPAL - Sso - Authenticated")
                    .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                    .headers(Headers.getHeaders(11))
                    .check(status().saveAs("httpStatus"))
                    .check(status().is(200))
                )
                .exec(UserInfoLogger.logDetailedErrorMessage("OPAL - Sso - Authenticated"))
                .exitHereIfFailed()  
                 
            )
            .group("Add Payment Details")
            .on(

                //Selecting Add Payment terms button and entering details
                pause(5,20)     
                .exec(
                    http("OPAL - API - Users-state")
                    .get(AppConfig.UrlConfig.BASE_URL + "/api/user-state")
                    .headers(Headers.getHeaders(12))
                    .check(status().saveAs("httpStatus"))
                    .check(status().is(200))
                ) 
                .exec(
                    http("OPAL - Sso - Authenticated")
                    .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                    .headers(Headers.getHeaders(11))
                    .check(status().saveAs("httpStatus"))
                    .check(status().is(200))
                )
                .exec(UserInfoLogger.logDetailedErrorMessage("OPAL - Sso - Authenticated"))
                .exitHereIfFailed()  
            )
                 
            .group("Add Comments and notes").on(
                
                //Selecting Account comments and notes button and entering notes
                pause(5,20) 
                                
                .exec(
                    http("OPAL - API - Users-state")
                    .get(AppConfig.UrlConfig.BASE_URL + "/api/user-state")
                    .headers(Headers.getHeaders(12))
                    .check(status().saveAs("httpStatus"))
                    .check(status().is(200))
                ) 
                .exec(
                    http("OPAL - Sso - Authenticated")
                    .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                    .headers(Headers.getHeaders(11))
                    .check(status().saveAs("httpStatus"))
                    .check(status().is(200))
                )
                .exec(UserInfoLogger.logDetailedErrorMessage("OPAL - Sso - Authenticated"))
                .exitHereIfFailed()
            )  
            .group("Review Account")
            .on(
                //Selecting Review and Submit Account details button
                pause(5,20) 

                .exec(
                    http("OPAL - API - Users-state")
                    .get(AppConfig.UrlConfig.BASE_URL + "/api/user-state")
                    .headers(Headers.getHeaders(12))
                    .check(status().saveAs("httpStatus"))
                    .check(status().is(200))
                )  
                .exec(
                    http("OPAL - Sso - Authenticated")
                    .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                    .headers(Headers.getHeaders(11))
                    .check(status().saveAs("httpStatus"))
                    .check(status().is(200))
                )
                .exec(UserInfoLogger.logDetailedErrorMessage("OPAL - Sso - Authenticated"))
                .exitHereIfFailed()  
                .exec(
                    http("OPAL - Opal-fines-service - Major-creditors")
                    .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/prosecutors?business_unit=#{selectedBusinessUnitId}")
                    .headers(Headers.getHeaders(12))
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

                    int index = ThreadLocalRandom.current().nextInt(prosecutorIds.size());

                    return session
                        .set("selectedProsecutorId", prosecutorIds.get(index))
                        .set("selectedProsecutorName", prosecutorNames.get(index));
                })      
                .exec(session -> {

                    Object selectedBusinessUnit =
                            session.get("selectedBusinessUnitId");

                    List<?> businessUnitIds =
                            session.getList("businessUnitIds");

                    List<String> businessUnitUserIds =
                            session.getList("businessUnitUserIds");

                    int index = -1;

                    for (int i = 0; i < businessUnitIds.size(); i++) {

                        if (businessUnitIds.get(i).toString()
                                .equals(selectedBusinessUnit.toString())) {

                            index = i;
                            break;
                        }
                    }

                    if (index == -1) {

                        throw new RuntimeException(
                            """
                            No business unit user found

                            selectedBusinessUnitId = %s
                            businessUnitIds = %s
                            businessUnitUserIds = %s
                            """.formatted(
                                selectedBusinessUnit,
                                businessUnitIds,
                                businessUnitUserIds
                            )
                        );
                    }
                    return session.set(
                            "selectedBusinessUnitUserId",
                            businessUnitUserIds.get(index)
                    );
                })
                
                .exec(session -> {

                        List<String> localJusticeAreaIds =
                            session.getList("localJusticeAreaIds");

                        List<String> localJusticeAreaNames =
                            session.getList("localJusticeAreaNames");

                        if (localJusticeAreaIds == null || localJusticeAreaIds.isEmpty()) {
                            System.out.println("No Local Justice Areas found!");
                            return session.markAsFailed();
                        }

                        if (localJusticeAreaNames == null ||
                            localJusticeAreaNames.size() != localJusticeAreaIds.size()) {

                            System.out.println(
                                "Local Justice Area IDs and names do not match!"
                            );

                            return session.markAsFailed();
                        }

                        int index =
                            ThreadLocalRandom.current().nextInt(localJusticeAreaIds.size());

                        String selectedLocalJusticeAreaId =
                            localJusticeAreaIds.get(index);

                        String selectedLocalJusticeAreaName =
                            localJusticeAreaNames.get(index);

                        System.out.println("Selected index = " + index);

                        System.out.println(
                            "Selected Local Justice Area ID = " +
                            selectedLocalJusticeAreaId
                        );

                        System.out.println(
                            "Selected Local Justice Area Name = " +
                            selectedLocalJusticeAreaName
                        );

                        return session
                            .set("selectedLocalJusticeAreaId", selectedLocalJusticeAreaId)
                            .set("selectedLocalJusticeAreaName", selectedLocalJusticeAreaName);
                    })
            )
            //.exec(UserInfoLogger.logSessionStatus("Before Submit Draft Account"))

            .group("Submit Draft Account")
            .on(
                //Selecting Submit for review button
                pause(20,60)

                .exec(session -> {
                    try {
                    // Store the generated payload in the session
                    String draftAccountRequestPayload =
                        RequestBodyBuilder.BuildDraftAccountConditionalCautionRequestBody(session);
;

                        return DraftAccountPayloadProcessor.process(
                                session,
                                draftAccountRequestPayload
                            );

                        } catch (Exception e) {
                            System.err.println("Payload parsing failed: " + e.getMessage());
                            return session.markAsFailed();
                        }
                    })
        
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
                    AccountCounters.CONDITIONAL_CREATED.incrementAndGet();
                    return session;
                })
                
                //.exec(UserInfoLogger.logSessionStatus("After draft account POST"))

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
                    http("OPAL - API - Users-state")
                    .get(AppConfig.UrlConfig.BASE_URL + "/api/user-state")
                    .headers(Headers.getHeaders(12))
                    .check(status().saveAs("httpStatus"))
                    .check(status().is(200))
                )                 
                .exec(
                    http("OPAL - Sso - Authenticated")
                    .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                    .headers(Headers.getHeaders(11))
                    .check(status().saveAs("httpStatus"))
                    .check(status().is(200))
                )
                .exec(UserInfoLogger.logDetailedErrorMessage("OPAL - Sso - Authenticated"))
                .exitHereIfFailed()  
            )          
        );            
    }
}
                     


   