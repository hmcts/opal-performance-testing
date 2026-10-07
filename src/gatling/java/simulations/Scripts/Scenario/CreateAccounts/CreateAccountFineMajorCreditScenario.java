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

public final class CreateAccountFineMajorCreditScenario {

    private CreateAccountFineMajorCreditScenario() {}

    public static ChainBuilder CreateAccountFineRequest() {

        return group("OPAL Create Manual Fine Account")
        .on(            
            group("Create and Manage").on(
            
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
                        .check(status().is(200))                                         
                )
                //Selecting Create and Manage Draft Accounts link            
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
                .exec(UserInfoLogger.logDetailedErrorMessage("OPAL - Opal-fines-service - Draft-accounts - QueryParams - Submitted"))
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
            
                //Selecting create account button
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
                        .check(Feeders.saveBusinessUnitId())
                        .check(status().saveAs("httpStatus"))
                        .check(status().is(200))
                )                
                .exec(UserInfoLogger.logDetailedErrorMessage("OPAL - Opal-fines-service - Business-units"))              
                .exitHereIfFailed()   
            )   
            .group("Load Fine Details")
            .on(

                //Selecting Bussiness unit and Account type, then selecting the next button
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
        
            .group("Enter Court Details").on(

                    //Selecting Court details link and Entering details
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
                                    
                    //Selecting Court details
                    .pause(5,20) 
                    .exec(
                        http("OPAL - Opal-fines-service - Courts")
                            .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/courts?business_unit=#{selectedBusinessUnitId}")
                            .headers(Headers.getHeaders(12))
                            .check(status().is(200))
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
                
                .group("Enter Personal Details").on(
                                    
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
                )
                                
                .group("Enter Defendant Contact Details").on(
                    
                    //Selecting Add Contact button and adding details
                    pause(5,20)
                    
                    .exec(
                        http("OPAL - API - Users-state")
                        .get(AppConfig.UrlConfig.BASE_URL + "/api/user-state")
                        .headers(Headers.getHeaders(12))
                        .check(status().saveAs("httpStatus"))
                        .check(status().is(200))
                    )                    
                    .exec(UserInfoLogger.logDetailedErrorMessage("OPAL - API - Users-state")) 

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
                
                .group("Enter Employer Details").on(

                    //Selecting Add Employer button and adding details
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
                .group("Add Offence").on(

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
                    .exec(
                        http("OPAL - Opal-fines-service - Results")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/results?result_ids=FCOMP&result_ids=FVS&result_ids=FCOST&result_ids=FCPC&result_ids=FO&result_ids=FCC&result_ids=FVEBD&result_ids=FFR")
                        .headers(Headers.getHeaders(12))
                        .check(status().saveAs("httpStatus"))
                        .check(status().is(200))
                    )
                    .exec(UserInfoLogger.logDetailedErrorMessage("OPAL - Opal-fines-service - Results"))

                    .exec(
                        http("OPAL - Opal-fines-service - Major-creditors")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/major-creditors?businessUnit=#{selectedBusinessUnitId}")
                        .headers(Headers.getHeaders(12))
                        .check(status().saveAs("httpStatus"))
                        .check(status().is(200))
                         .check(
                                jsonPath("$.refData[*].major_creditor_id").findAll().saveAs("getMajorCreditorIds"))               

                    )                    
                    .exec(session -> {
                        List<String> majorCreditorIds = session.getList("getMajorCreditorIds");

                        String selectedMajorCreditorId =
                                majorCreditorIds.get(ThreadLocalRandom.current().nextInt(majorCreditorIds.size()));

                        return session.set("selectedMajorCreditorId", selectedMajorCreditorId);
                    })

                    .pause(5,20)
                    .exec(session -> {
                        String offence = DataGenerator.generateRandomOFFENCE();
                        return session.set("offenceCode", offence);
                    })
                    .exec(
                        http("OPAL - API - Users-state")
                        .get(AppConfig.UrlConfig.BASE_URL + "/api/user-state")
                        .headers(Headers.getHeaders(12))
                        .check(status().saveAs("httpStatus"))
                        .check(status().is(200))
                    )  
                // .group("Search Offences").on(

                //     //Searching for offences
                //     pause(5,20)

                //     .exec(session -> {
                //         try {
                //             String offencesSearchRequestPayload =
                //                 RequestBodyBuilder.buildOffencesSearchRequestPayload(session);
                            
                //             // Create SHA-512 digest
                //             String contentDigest =
                //                 ContentDigestGenerator.generateSha512ContentDigest(
                //                     offencesSearchRequestPayload
                //                 );

                //             ObjectMapper mapper = new ObjectMapper();

                //             // Convert directly into JsonNode WITHOUT readTree
                //             JsonNode json = mapper.readValue(offencesSearchRequestPayload, JsonNode.class);

                //             String accountType = json.has("account_type")
                //                 ? json.get("account_type").asText()
                //                 : "UNKNOWN";

                //             String businessUnitId = json.has("business_unit_id")
                //                 ? json.get("business_unit_id").asText()
                //                 : "UNKNOWN";

                //             return session
                //                 .set("offencesSearchRequestPayload", offencesSearchRequestPayload)
                //                 .set("contentDigest", contentDigest)
                //                 .set("createdAccountType", accountType)
                //                 .set("createdBusinessUnitId", businessUnitId);

                //         } catch (Exception e) {
                //             System.err.println("Payload parsing failed: " + e.getMessage());
                //             return session.markAsFailed();
                //         }
                //         })
                //     .exec(
                //         http("OPAL - Opal-fines-service - Offences - Search")
                //             .post("/opal-fines-service/offences/search")
                //             .headers(Headers.getHeaders(12))
                //             .body(StringBody(session -> session.get("offencesSearchRequestPayload"))).asJson()
                //         ) 

                // )
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

                .group("Enter Offence Details").on(
                    //Selecting Review offence Button      
                    pause(5,20) 
                    .exec(
                        http("OPAL - API - Users-state")
                        .get(AppConfig.UrlConfig.BASE_URL + "/api/user-state")
                        .headers(Headers.getHeaders(12))
                        .check(status().saveAs("httpStatus"))
                        .check(status().is(200))
                    )
                    .exec(UserInfoLogger.logDetailedErrorMessage("OPAL - API - Users-state")) 

                    //Selecting Add Payment terms button and entering details
                    .pause(5,20)    
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
                )
                
                .group("Review Account").on(
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
                                                    
                    //Selecting Review account Button
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
                    .exec(UserInfoLogger.logDetailedErrorMessage("OPAL - Sso - Authenticated"))
                    .exitHereIfFailed()  
                    
                    .exec(
                        http("OPAL - Opal-fines-service - Prosecutors")
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

                        List<String> prosecutorIds =
                            session.getList("prosecutorIds");

                        List<String> prosecutorNames =
                            session.getList("prosecutorNames");

                        if (prosecutorIds == null || prosecutorIds.isEmpty()) {
                            System.out.println("No prosecutors found!");
                            return session;
                        }

                        int index =
                            ThreadLocalRandom.current().nextInt(prosecutorIds.size());

                        String selectedProsecutorId =
                            prosecutorIds.get(index);

                        String selectedProsecutorName =
                            prosecutorNames.get(index);

                        System.out.println(
                            "selectedProsecutorId = " + selectedProsecutorId
                        );

                        System.out.println(
                            "selectedProsecutorName = " + selectedProsecutorName
                        );

                        return session
                            .set("selectedProsecutorId", selectedProsecutorId)
                            .set("selectedProsecutorName", selectedProsecutorName);
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
                .group("Submit Draft Account").on(
                    exec(session -> {
                        try {
                            String draftAccountRequestPayload =
                                RequestBodyBuilder.BuildDraftAccountFineRequestBody(session);
                           // System.out.println("draftAccountRequestPayload = " + draftAccountRequestPayload);

                            return DraftAccountPayloadProcessor.process(
                                session,
                                draftAccountRequestPayload
                            );
                            
                        } catch (Exception e) {
                            System.err.println("Payload parsing failed: " + e.getMessage());
                            return session.markAsFailed();
                        }
                    })
                    
                    //Selecting Submit for review button 
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
                    .check(status().saveAs("httpStatus"))
                    .check(status().is(200))
                )
                .exec(UserInfoLogger.logDetailedErrorMessage("OPAL - Sso - Authenticated"))
                .exitHereIfFailed()  
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
            )                         
        );            
    }
}
                     


   