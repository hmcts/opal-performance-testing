package simulations.Scripts.Scenario.ParentAndGuardian;

import simulations.Scripts.Headers.Headers;
import simulations.Scripts.Utilities.AppConfig;
import simulations.Scripts.Utilities.ContentDigestGenerator;
import io.gatling.javaapi.core.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import simulations.Scripts.RequestBodyBuilder.RequestBodyBuilderR1b;

public final class AddParentAndGuardianAccountScenario {

    private AddParentAndGuardianAccountScenario() {}
    private static final Logger logger = LoggerFactory.getLogger("OPAL");

    public static ChainBuilder AddParentAndGuardianAccountRequest() {

        return group("OPAL Add Parent And Guardian Account")
        .on(
            group("Create and Manage")
            .on(
                exec(
                    http("OPAL - Opal-fines-service - Defendant-accounts - Defendant-account-parties - This is a test - Add")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/defendant-accounts/#{defendant_account_id}/defendant-account-parties/#{getDefendantAccountPartyId}")
                        .headers(Headers.getHeaders(12))
                        .check(jsonPath(session -> "$.defendant_account_party.address.address_line_1").find().optional().saveAs("getAddressLine1"))
                        .check(jsonPath(session -> "$.defendant_account_party.address.address_line_2").find().optional().saveAs("getAddressLine2"))
                        .check(jsonPath(session -> "$.defendant_account_party.contact_details.primary_email_address").find().optional().saveAs("getPrimaryEmailAddress"))
                        .check(jsonPath(session -> "$.defendant_account_party.employer_details.employer_address.address_line_1").find().optional().saveAs("getEmployerAddressLine1"))
                        .check(jsonPath(session -> "$.defendant_account_party.employer_details.employer_name").find().optional().saveAs("getEmployerName"))
                        .check(jsonPath(session -> "$.defendant_account_party.employer_details.employer_reference").find().optional().saveAs("getEmployerReference"))
                        .check(jsonPath(session -> "$.defendant_account_party.party_details.individual_details.forenames").find().optional().saveAs("getIndividualForenames"))
                        .check(jsonPath(session -> "$.defendant_account_party.party_details.individual_details.surname").find().optional().saveAs("getIndividualSurnames"))
                        .check(jsonPath(session -> "$.defendant_account_party.party_details.party_id").find().optional().saveAs("getIndividualPartyId"))
                        .check(jsonPath(session -> "$.defendant_account_party.vehicle_details.vehicle_make_and_model").find().optional().saveAs("getVehicleMakeAndModel"))
                        .check(jsonPath(session -> "$.defendant_account_party.vehicle_details.vehicle_registration").find().optional().saveAs("getVehicleRegistration"))
                        ) 
                )
                .exec(
                    http("OPAL - Opal-fines-service - Defendant-accounts - Header-summary")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/defendant-accounts/#{defendant_account_id}/header-summary")
                        .headers(Headers.getHeaders(12))
                        .check(jsonPath(
                            "$.business_unit_summary.business_unit_id")
                            .find()
                            .saveAs("getBusinessUnitId")
                        )
                )
                .pause(5,20)
                .exec(session -> {
                    try {
                        String defendantAccountPartiesRequestPayload =
                            RequestBodyBuilderR1b.DefendantAccountSearch.BuildDefendantAccountPartiesRequestBody(session);
                           // System.err.println("Payload: " + defendantAccountPartiesRequestPayload);

                        // Create SHA-512 digest
                        String contentDigest =
                            ContentDigestGenerator.generateSha512ContentDigest(
                                defendantAccountPartiesRequestPayload
                            );


                        ObjectMapper mapper = new ObjectMapper();

                        // Convert directly into JsonNode WITHOUT readTree
                        JsonNode json = mapper.readValue(defendantAccountPartiesRequestPayload, JsonNode.class);

                        return session
                            .set("defendantAccountPartiesRequestPayload", defendantAccountPartiesRequestPayload)
                            .set("contentDigest", contentDigest);

                    } catch (Exception e) {
                        System.err.println("Payload parsing failed: " + e.getMessage());
                        return session.markAsFailed();
                    }
                })
                .exec(
                    http("OPAL - Opal-fines-service - Defendant-accounts - Defendant-account-parties")
                        .post(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/defendant-accounts/#{defendant_account_id}/defendant-account-parties")
                        .headers(Headers.getHeaders(19))
                        .body(StringBody(session -> session.get("defendantAccountPartiesRequestPayload"))).asJson()

                )
                .exec(
                    http("OPAL - Opal-fines-service - Defendant-accounts - Header-summary")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/defendant-accounts/#{defendant_account_id}/header-summary")
                            .headers(Headers.getHeaders(12))
                            .check(
                                jsonPath("$.parent_guardian_party_id").optional().saveAs("getParentGuardianPartyId"))
                )
                .exec(
                    http("OPAL - Opal-fines-service - Defendant-accounts - Defendant-account-parties")
                        .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/defendant-accounts/#{defendant_account_id}/defendant-account-parties/#{getParentGuardianPartyId}")
                            .headers(Headers.getHeaders(12))
                )
            );            
    }
}
                     


   