package simulations.Scripts.RequestBodyBuilder;


import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.core.CoreDsl.jsonPath;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Session;
import simulations.Scripts.Utilities.DataGenerator;
/**
 * Factory class for building various request bodies used in R1b functinality because MH was afraid that otherwise the RequestBodyBuilder would just be massive and uneditable.
 * This class delegates to specialised builders for each type of request.
 */
public class RequestBodyBuilderR1b {

public static final class DefendantAccountSearch {
    //to call this elsewhere use .exec(RequestBodyBuilderR1b.DefendantAccountSearch.searchDefendantAccounts())

    private DefendantAccountSearch() {
        // Utility class.
    }

    public static String buildDefendantSearchAccountRequestBody(Session session) {
        String businessUnitIdsJson = session.get("getListBusinessUnitId") != null
            ? session.get("getListBusinessUnitId").toString()
            : "[]";

            //MH Need to add these as .csv files under each journey so that different jorneys might search from a different list of names -especially if they expect to find
            //a fixed penalty or a parent guardian (nor not find a parent guardian so they can add one)
        String surname = session.contains("LASTNAME") ? session.getString("LASTNAME") : "";
        String forenames = session.contains("FIRSTNAME") ? session.getString("FIRSTNAME") : "";
//MH I desperately want to change the below to use something like ${FIRSTNAME} because it makes more sense to my Jmeter brain, but I am TRUSTING THE PROCESS
        return String.format(
                "{\n" +
                "  \"active_accounts_only\": true,\n" +
                "  \"business_unit_ids\": %s,\n" +  
                "  \"defendant\": {\n" +
                "    \"address_line_1\": null,\n" +
                "    \"birth_date\": null,\n" +
                "    \"exact_match_forenames\": false,\n" +
                "    \"exact_match_organisation_name\": null,\n" +
                "    \"exact_match_surname\": false,\n" +
                "    \"forenames\": \"%s\",\n" +
                "    \"include_aliases\": false,\n" +
                "    \"national_insurance_number\": null,\n" +
                "    \"organisation\": false,\n" +
                "    \"organisation_name\": null,\n" +
                "    \"postcode\": null,\n" +
                "    \"surname\": \"%s\"\n" +
                "  },\n" +
                "  \"reference_number\": null\n" +
                "}",
                businessUnitIdsJson,
                forenames,
                surname
            );
    }

    public static ChainBuilder searchDefendantAccounts() {
        return exec(
            http("Search defendant accounts")
                .post("https://opal-frontend.test.apps.hmcts.net/opal-fines-service/defendant-accounts/search")
                .header("Content-Type", "application/json")
                .body(StringBody(session -> buildDefendantSearchAccountRequestBody(session))).asJson() //MH change here because it wasn't working, I've had a number of the named wrong
                .check(status().is(200))
                .check(jsonPath("$.count").saveAs("search_count"))
                .check(jsonPath("$.defendant_accounts[0].defendant_account_id").exists())
                .check(jsonPath("$.defendant_accounts[0].defendant_account_id").saveAs("defendant_account_id"))     
    );
    }

    public static String BuildSearchParentandGuardianAccountRequestBody(Session session) {

            String businessUnitIdsJson = session.get("businessUnitIds") != null
                    ? session.get("businessUnitIds").toString()
                    : "[]";

            String forenames = session.getString("forename");
            String surname = session.getString("surname");

            return String.format(
                "{\n" +
                "  \"account_number\": null,\n" +
                "  \"active_accounts_only\": false,\n" +
                "  \"business_unit_ids\": %s,\n" +
                "  \"consolidation_search\": false,\n" +
                "  \"defendant\": {\n" +
                "    \"address_line_1\": null,\n" +
                "    \"birth_date\": null,\n" +
                "    \"exact_match_forenames\": null,\n" +
                "    \"exact_match_organisation_name\": null,\n" +
                "    \"exact_match_surname\": null,\n" +
                "    \"forenames\": \"%s\",\n" +
                "    \"include_aliases\": false,\n" +
                "    \"national_insurance_number\": null,\n" +
                "    \"organisation\": false,\n" +
                "    \"organisation_name\": null,\n" +
                "    \"postcode\": null,\n" +
                "    \"surname\": \"%s\"\n" +
                "  },\n" +
                "  \"reference_number\": null\n" +
                "}",
                businessUnitIdsJson,
                forenames,
                surname
            );
        }

        public static String buildMinorCreditorSearchAccountRequestBody(Session session) {

            String businessUnitIdsJson = session.get("businessUnitIds") != null
                    ? session.get("businessUnitIds").toString()
                    : "[]";

            String forenames = session.getString("forename");
            String surname = session.getString("surname");

            return String.format(
                "{\n" +
                "  \"account_number\": null,\n" +
                "  \"active_accounts_only\": false,\n" +
                "  \"business_unit_ids\": %s,\n" +
                "  \"creditor\": {\n" +
                "    \"address_line_1\": null,\n" +
                "    \"exact_match_forenames\": null,\n" +
                "    \"exact_match_organisation_name\": null,\n" +
                "    \"exact_match_surname\": null,\n" +
                "    \"forenames\": \"%s\",\n" +
                "    \"organisation\": false,\n" +
                "    \"organisation_name\": null,\n" +
                "    \"postcode\": null,\n" +
                "    \"surname\": \"%s\"\n" +
                "  }\n" +
                "}",
                businessUnitIdsJson,
                forenames,
                surname
            );           
        }

        

        public static String BuildremovePGRequestBody(Session session) {

             // Get defendant account party id from session
            String defendant_account_id = session.get("defendant_account_id") != null
                    ? session.get("defendant_account_id").toString().trim().toUpperCase()
                    : "";

            return String.format(
                "{\n" +
                " \"defendant_account_party_id\": \"%s\"\n" +
                "}",
                defendant_account_id
            );
        }

        public static String BuildDefendantAccountPartiesRequestBody(Session session) {

            String addressLine1 = session.get("generatedAddressLine1") != null ? session.get("generatedAddressLine1").toString() : DataGenerator.generateRandomAddress();
            String addressLine2 = session.get("generatedAddressLine2") != null ? session.get("generatedAddressLine2").toString() : DataGenerator.generateRandomAddress();
            String adultDob = DataGenerator.generateRandomAdultDateOfBirth();
            String primaryEmailAddress = session.get("getPrimaryEmailAddress") != null ? session.get("getPrimaryEmailAddress").toString().trim().toUpperCase() : "";
            String forename = session.get("generatedForename") != null ? session.get("generatedForename").toString() : DataGenerator.generateRandomFirstName();
            String surname = session.get("generatedSurname") != null ? session.get("generatedSurname").toString() : DataGenerator.generateRandomLastName();
            // Generate random address
            String randomAddressline3 = DataGenerator.generateRandomAddress();
            session = session.set("randomAddressline3", randomAddressline3);           
                        
           return String.format(
                "{\n" +
                "  \"defendant_account_party\": {\n" +
                "    \"address\": {\n" +
                "      \"address_line_1\": \"%s\",\n" +
                "      \"address_line_2\": \"%s\",\n" +
                "      \"address_line_3\": null,\n" +
                "      \"address_line_4\": null,\n" +
                "      \"address_line_5\": null,\n" +
                "      \"postcode\": null\n" +
                "    },\n" +
                "    \"contact_details\": {\n" +
                "      \"home_telephone_number\": null,\n" +
                "      \"mobile_telephone_number\": null,\n" +
                "      \"primary_email_address\": \"%s\",\n" +
                "      \"secondary_email_address\": null,\n" +
                "      \"work_telephone_number\": null\n" +
                "    },\n" +
                "    \"defendant_account_party_type\": \"Parent/Guardian\",\n" +
                "    \"employer_details\": null,\n" +
                "    \"is_debtor\": false,\n" +
                "    \"language_preferences\": {\n" +
                "      \"document_language_preference\": null,\n" +
                "      \"hearing_language_preference\": null\n" +
                "    },\n" +
                "    \"party_details\": {\n" +
                "      \"individual_details\": {\n" +
                "        \"age\": null,\n" +
                "        \"date_of_birth\": \"%s\",\n" +
                "        \"forenames\": \"%s\",\n" +
                "        \"individual_aliases\": null,\n" +
                "        \"national_insurance_number\": null,\n" +
                "        \"surname\": \"%s\",\n" +
                "        \"title\": null\n" +
                "      },\n" +
                "      \"organisation_flag\": false,\n" +
                "      \"party_id\": \"\"\n" +
                "    },\n" +
                "    \"vehicle_details\": null\n" +
                "  }\n" +
                "}",
                addressLine1,
                addressLine2,
                primaryEmailAddress,
                adultDob,
                forename,
                surname
            );
          
        }

          public static String BuildUpdateDefendantAccountPartiesRequestBody(Session session) {

            String addressLine1 = session.get("getAddressLine1") != null
            ? session.get("getAddressLine1").toString().trim().toUpperCase()
            : "";

            String addressLine2 = session.get("getAddressLine2") != null
                    ? session.get("getAddressLine2").toString().trim().toUpperCase()
                    : "";

            String primaryEmailAddress = session.get("getPrimaryEmailAddress") != null
                    ? session.get("getPrimaryEmailAddress").toString().trim().toUpperCase()
                    : "";

            String employerAddressLine1 = session.get("getEmployerAddressLine1") != null
                    ? session.get("getEmployerAddressLine1").toString().trim().toUpperCase()
                    : "";

            String employerName = session.get("getEmployerName") != null
                    ? session.get("getEmployerName").toString().trim().toUpperCase()
                    : "";

            String employerReference = session.get("getEmployerReference") != null
                    ? session.get("getEmployerReference").toString().trim().toUpperCase()
                    : "";

            String individualForenames = session.get("getIndividualForenames") != null
                    ? session.get("getIndividualForenames").toString().trim().toUpperCase()
                    : "";

            String individualSurnames = session.get("getIndividualSurnames") != null
                    ? session.get("getIndividualSurnames").toString().trim().toUpperCase()
                    : "";

            String individualPartyId = session.get("getIndividualPartyId") != null
                    ? session.get("getIndividualPartyId").toString().trim().toUpperCase()
                    : "";

            String vehicleMakeAndModel = session.get("getVehicleMakeAndModel") != null
                    ? session.get("getVehicleMakeAndModel").toString().trim().toUpperCase()
                    : "";

            String vehicleRegistration = session.get("getVehicleRegistration") != null
                    ? session.get("getVehicleRegistration").toString().trim().toUpperCase()
                    : "";


            // Generate random address
            String randomAddressline3 = DataGenerator.generateRandomAddress();

            session = session.set("randomAddressline3", randomAddressline3);  
                        
         return String.format(
                "{\n" +
                "    \"address\": {\n" +
                "      \"address_line_1\": \"%s\",\n" +
                "      \"address_line_2\": null,\n" +
                "      \"address_line_3\": null,\n" +
                "      \"address_line_4\": null,\n" +
                "      \"address_line_5\": null,\n" +
                "      \"postcode\": null\n" +
                "    },\n" +
                "    \"contact_details\": {\n" +
                "      \"home_telephone_number\": null,\n" +
                "      \"mobile_telephone_number\": null,\n" +
                "      \"primary_email_address\": \"%s\",\n" +
                "      \"secondary_email_address\": null,\n" +
                "      \"work_telephone_number\": null\n" +
                "    },\n" +
                "    \"defendant_account_party_type\": \"Parent/Guardian\",\n" +
                "    \"employer_details\": null,\n" +
                "    \"is_debtor\": false,\n" +
                "    \"language_preferences\": {\n" +
                "      \"document_language_preference\": null,\n" +
                "      \"hearing_language_preference\": null\n" +
                "    },\n" +
                "    \"party_details\": {\n" +
                "      \"individual_details\": {\n" +
                "        \"age\": null,\n" +
                "        \"date_of_birth\": null,\n" +
                "        \"forenames\": \"%s\",\n" +
                "        \"individual_aliases\": null,\n" +
                "        \"national_insurance_number\": null,\n" +
                "        \"surname\": \"%s\",\n" +
                "        \"title\": null\n" +
                "      },\n" +
                "      \"organisation_details\": null,\n" +
                "      \"organisation_flag\": false,\n" +
                "      \"party_id\": \"%s\"\n" +
                "    },\n" +
                "    \"vehicle_details\": null\n" +
                "  }",

                addressLine1,
                primaryEmailAddress,
                individualForenames,
                individualSurnames,
                individualPartyId
            );
        }

        public static String buildUpdateMinorCreditorAccountRequestBody(Session session) {

            String getAddressLine1 = session.getString("getAddressLine1");
            String getAddressLine2 = session.getString("getAddressLine2");

            String randomAddressline3 = DataGenerator.generateRandomAddress();
            session.set("randomAddressline3", randomAddressline3);

            String getCreditorAccountId = session.getString("getCreditorAccountId");
            String getIndividualForenames = session.getString("getIndividualForenames");
            String getIndividualSurname = session.getString("getIndividualSurname");
            String getIndividualTitle = session.getString("getIndividualTitle");
            String getPartyId = session.getString("getPartyId");

            String accountName = DataGenerator.generateRandomAccountName();
            String accountNumber = DataGenerator.generateRandomAccountNumber();
            String accountReference = DataGenerator.generateRandomAccountReference();
            String sortCode = DataGenerator.generateRandomSortCode();

            boolean holdPayment = false;
            boolean payByBacs = true;

            return String.format(
                "{\n" +
                "  \"address\": {\n" +
                "    \"address_line_1\": \"%s\",\n" +
                "    \"address_line_2\": \"%s\",\n" +
                "    \"address_line_3\": \"%s\",\n" +
                "    \"address_line_4\": null,\n" +
                "    \"address_line_5\": null,\n" +
                "    \"postcode\": null\n" +
                "  },\n" +
                "  \"creditor_account_id\": \"%s\",\n" +
                "  \"party_details\": {\n" +
                "    \"individual_details\": {\n" +
                "      \"age\": null,\n" +
                "      \"date_of_birth\": \"\",\n" +
                "      \"forenames\": \"%s\",\n" +
                "      \"individual_aliases\": null,\n" +
                "      \"national_insurance_number\": null,\n" +
                "      \"surname\": \"%s\",\n" +
                "      \"title\": \"%s\"\n" +
                "    },\n" +
                "    \"organisation_details\": null,\n" +
                "    \"organisation_flag\": false,\n" +
                "    \"party_id\": \"%s\"\n" +
                "  },\n" +
                "  \"payment\": {\n" +
                "    \"account_name\": \"%s\",\n" +
                "    \"account_number\": \"%s\",\n" +
                "    \"account_reference\": \"%s\",\n" +
                "    \"hold_payment\": %b,\n" +
                "    \"pay_by_bacs\": %b,\n" +
                "    \"sort_code\": \"%s\"\n" +
                "  }\n" +
                "}",
                getAddressLine1,
                getAddressLine2,
                randomAddressline3,
                getCreditorAccountId,
                getIndividualForenames,
                getIndividualSurname,
                getIndividualTitle,
                getPartyId,
                accountName,
                accountNumber,
                accountReference,
                holdPayment,
                payByBacs,
                sortCode
            );
        }
          public static String buildRemovePaymentMinorCreditorAccountRequestBody(Session session) {

            String getAddressLine1 = session.getString("getAddressLine1");
            String getAddressLine2 = session.getString("getAddressLine2");

            String randomAddressline3 = DataGenerator.generateRandomAddress();
            session.set("randomAddressline3", randomAddressline3);

            String getCreditorAccountId = session.getString("getCreditorAccountId");
            String getIndividualForenames = session.getString("getIndividualForenames");
            String getIndividualSurname = session.getString("getIndividualSurname");
            String getIndividualTitle = session.getString("getIndividualTitle");
            String getPartyId = session.getString("getPartyId");
         
            boolean holdPayment = false;
            boolean payByBacs = true;

            return String.format(
                "{\n" +
                "  \"address\": {\n" +
                "    \"address_line_1\": \"%s\",\n" +
                "    \"address_line_2\": \"%s\",\n" +
                "    \"address_line_3\": \"%s\",\n" +
                "    \"address_line_4\": null,\n" +
                "    \"address_line_5\": null,\n" +
                "    \"postcode\": null\n" +
                "  },\n" +
                "  \"creditor_account_id\": \"%s\",\n" +
                "  \"party_details\": {\n" +
                "    \"individual_details\": {\n" +
                "      \"age\": null,\n" +
                "      \"date_of_birth\": \"\",\n" +
                "      \"forenames\": \"%s\",\n" +
                "      \"individual_aliases\": null,\n" +
                "      \"national_insurance_number\": null,\n" +
                "      \"surname\": \"%s\",\n" +
                "      \"title\": \"%s\"\n" +
                "    },\n" +
                "    \"organisation_details\": null,\n" +
                "    \"organisation_flag\": false,\n" +
                "    \"party_id\": \"%s\"\n" +
                "  },\n" +
                "  \"payment\": {\n" +
                "    \"account_name\": null,\n" +
                "    \"account_number\": null,\n" +
                "    \"account_reference\": null,\n" +
                "    \"hold_payment\": false,\n" +
                "    \"pay_by_bacs\": false,\n" +
                "    \"sort_code\": null\n" +
                "  }\n" +
                "}",
                getAddressLine1,
                getAddressLine2,
                randomAddressline3,
                getCreditorAccountId,
                getIndividualForenames,
                getIndividualSurname,
                getIndividualTitle,
                getPartyId,
                holdPayment,
                payByBacs
            );
        }
        

        public static String BuildRemoveEnforcementRequestBody(Session session) {

                DataGenerator randomStringGenerator = new DataGenerator();
                String reasonText1 = randomStringGenerator.generateRandomString(10);

            return String.format(
                "{\n" +
                " \"reason\": \"%s\"\n" +
                "}",
                reasonText1
            );
        }

        public static String buildAmendCollectionOrderEnforcementRequestBody(Session session) {

            String collectionOrderFlagValue = session.get("collectionOrderFlag") != null
                    ? session.get("collectionOrderFlag").toString().trim()
                    : "false";

            boolean collectionOrderFlag = Boolean.parseBoolean(collectionOrderFlagValue);

            // Invert the value
            boolean amendedCollectionOrderFlag = !collectionOrderFlag;

            return String.format(
                "{\n" +
                "  \"collection_order\": {\n" +
                "    \"collection_order_date\": \"null\",\n" +
                "    \"collection_order_flag\": \"%s\"\n" +
                "  }\n" +
                "}",
                amendedCollectionOrderFlag
            );
        }

         public static String buildCovertToCompanyRequestBody(Session session) {

            String partyId = session.get("partyId") != null ? session.get("partyId").toString() : ""; 

            // Retrieve reused data from session (generated in BuildDraftAccountRequestBody)
            String forename = session.get("generatedForename") != null ? session.get("generatedForename").toString() : DataGenerator.generateRandomFirstName();
            String surname = session.get("generatedSurname") != null ? session.get("generatedSurname").toString() : DataGenerator.generateRandomLastName();
            String vehicleReg = session.get("generatedVehicleReg") != null ? session.get("generatedVehicleReg").toString() : DataGenerator.generateRandomVehicleRegistration();
            String email1 = session.get("generatedEmail1") != null ? session.get("generatedEmail1").toString() : (forename.toLowerCase() + "." + surname.toLowerCase() + "@example.com");
            String addressLine1 = session.get("generatedAddressLine1") != null ? session.get("generatedAddressLine1").toString() : DataGenerator.generateRandomAddress();
            String addressLine2 = session.get("generatedAddressLine2") != null ? session.get("generatedAddressLine2").toString() : DataGenerator.generateRandomCity();
            String vehicleMake = session.get("generatedVehicleMake") != null ? session.get("generatedVehicleMake").toString() : DataGenerator.generateRandomVehicleMake();

            return String.format(
                "{\n" +
                "  \"address\": {\n" +
                "    \"address_line_1\": \"%s\",\n" +
                "    \"address_line_2\": \"%s\",\n" +
                "    \"address_line_3\": null,\n" +
                "    \"address_line_4\": null,\n" +
                "    \"address_line_5\": null,\n" +
                "    \"postcode\": null\n" +
                "  },\n" +
                "  \"contact_details\": {\n" +
                "    \"home_telephone_number\": null,\n" +
                "    \"mobile_telephone_number\": null,\n" +
                "    \"primary_email_address\": \"%s\",\n" +
                "    \"secondary_email_address\": null,\n" +
                "    \"work_telephone_number\": null\n" +
                "  },\n" +
                "    \"defendant_account_party_type\": \"Defendant\",\n" +
                "    \"employer_details\": null,\n" +
                "    \"is_debtor\": true,\n" +
                "  \"language_preferences\": {\n" +
                "    \"document_language_preference\": null,\n" +
                "    \"hearing_language_preference\": null\n" +
                "  },\n" +
                "  \"party_details\": {\n" +
                "       \"individual_details\": null,\n" +
                "           \"organisation_details\": {\n" +
                "               \"organisation_aliases\": null,\n" +
                "               \"organisation_name\": \" Company %s\"\n" +          
                "           },\n" +
                "  \"organisation_flag\": true,\n" +
                "  \"party_id\": \"%s\"\n" +
                "  },\n" +
                "  \"vehicle_details\": {\n" +
                "       \"vehicle_make_and_model\": \"%s\",\n" +
                "       \"vehicle_registration\": \"%s\"\n" +
                "  }\n" +
                "}",
                addressLine1, addressLine2, email1, forename, partyId, vehicleMake, vehicleReg
            );
        }

        public static String buildCovertToDefendantRequestBody(Session session) {

            String partyId = session.get("partyId") != null ? session.get("partyId").toString() : ""; 

            // Retrieve reused data from session (generated in BuildDraftAccountRequestBody)
            String forename = session.get("generatedForename") != null ? session.get("generatedForename").toString() : DataGenerator.generateRandomFirstName();
            String surname = session.get("generatedSurname") != null ? session.get("generatedSurname").toString() : DataGenerator.generateRandomLastName();
            String vehicleReg = session.get("generatedVehicleReg") != null ? session.get("generatedVehicleReg").toString() : DataGenerator.generateRandomVehicleRegistration();
            String email1 = session.get("generatedEmail1") != null ? session.get("generatedEmail1").toString() : (forename.toLowerCase() + "." + surname.toLowerCase() + "@example.com");
            String addressLine1 = session.get("generatedAddressLine1") != null ? session.get("generatedAddressLine1").toString() : DataGenerator.generateRandomAddress();
            String addressLine2 = session.get("generatedAddressLine2") != null ? session.get("generatedAddressLine2").toString() : DataGenerator.generateRandomCity();
            String vehicleMake = session.get("generatedVehicleMake") != null ? session.get("generatedVehicleMake").toString() : DataGenerator.generateRandomVehicleMake();
            String adultDob = DataGenerator.generateRandomAdultDateOfBirth();

            return String.format(
                "{\n" +
                "  \"address\": {\n" +
                "    \"address_line_1\": \"%s\",\n" +
                "    \"address_line_2\": \"%s\",\n" +
                "    \"address_line_3\": null,\n" +
                "    \"address_line_4\": null,\n" +
                "    \"address_line_5\": null,\n" +
                "    \"postcode\": null\n" +
                "  },\n" +
                "  \"contact_details\": {\n" +
                "    \"home_telephone_number\": null,\n" +
                "    \"mobile_telephone_number\": null,\n" +
                "    \"primary_email_address\": \"%s\",\n" +
                "    \"secondary_email_address\": null,\n" +
                "    \"work_telephone_number\": null\n" +
                "  },\n" +
                "    \"defendant_account_party_type\": \"Defendant\",\n" +
                "    \"employer_details\": null,\n" +
                "    \"is_debtor\": true,\n" +
                "  \"language_preferences\": {\n" +
                "    \"document_language_preference\": null,\n" +
                "    \"hearing_language_preference\": null\n" +
                "  },\n" +
                "  \"party_details\": {\n" +
                "       \"individual_details\": {\n" +
                "           \"age\": \"null\",\n" +
                "           \"date_of_birth\": \"%s\",\n" +
                "           \"forenames\": \"Convert %s\",\n" +
                "           \"individual_aliases\": null,\n" +
                "           \"national_insurance_number\": null,\n" +
                "           \"surname\": \"Convert %s\",\n" +
                "           \"title\": \"Mr\"\n" +
                "  },\n" +
                "  \"organisation_details\": null,\n" +
                "  \"organisation_flag\": false,\n" +
                "  \"party_id\": \"%s\"\n" +
                "  },\n" +
                "  \"vehicle_details\": {\n" +
                "       \"vehicle_make_and_model\": \"%s\",\n" +
                "       \"vehicle_registration\": \"%s\"\n" +
                "  }\n" +
                "}",
                addressLine1, addressLine2, email1, adultDob, forename, surname, partyId, vehicleMake, vehicleReg
            );
        }

        public static String buildOverrideEnforcementRequestBody(Session session) {

            // Get enforcement from Gatling session
            String enforcement = session.get("EnforcementId") != null
                    ? session.get("EnforcementId").toString().trim().toUpperCase()
                    : "";
          

            // Stop early if enforcement is missing
            if (enforcement.isEmpty()) {
                throw new IllegalArgumentException(
                    "Enforcement value is NULL or EMPTY. " +
                    "Check that 'enforcement' is being added to the Gatling Session."
                );
            }

            DataGenerator randomStringGenerator = new DataGenerator();

            String reasonText1 = randomStringGenerator.generateRandomString(10);
            String reasonText2 = randomStringGenerator.generateRandomString(10);

            switch (enforcement) {

                /*
                * NOENF / CONF / INTL
                * All use the same parameters.
                */
                case "SUMA": 
                    return String.format(
                        "{\n" +
                        "  \"enforcement_override\": {\n" +
                        "      \"enforcement_override_result\": {\n" +
                        "           \"enforcement_override_result_id\": \"%s\"\n" +
                        "    },\n" +
                        "  \"enforcer\": \"null,\"\n" +
                        "  \"lja\": \"null\"\n" +
                            "}" +
                        "}",
                        enforcement
                    );  
                
                case "ABDC": 
                    return String.format(
                        "{\n" +
                        "  \"enforcement_override\": {\n" +
                        "      \"enforcement_override_result\": {\n" +
                        "           \"enforcement_override_result_id\": \"%s\"\n" +
                        "    },\n" +
                        "  \"enforcer\": \"null,\"\n" +
                        "  \"lja\": \"null\"\n" +
                            "}" +
                        "}",
                        enforcement
                    );
               
                /*
                * CWN
                */
                case "CWN":               
                     return String.format(
                        "{\n" +
                        "  \"enforcement_result_responses\": [\n" +
                        "    {\n" +
                        "      \"parameter_name\": \"reason\",\n" +
                        "      \"response\": \"%s\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"parameter_name\": \"hearing_date\",\n" +
                        "      \"response\": \"2026-08-12\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"parameter_name\": \"court_code\",\n" +
                        "      \"response\": \"1\"\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"result_id\": \"%s\"\n" +
                        "}",
                        reasonText1,
                        enforcement
                    );  

                /*
                * Unknown enforcement
                */
                default:

                    throw new IllegalArgumentException(
                        "Unknown enforcement type: [" + enforcement + "]"
                    );
            }
        }

        public static String buildRemoveOverrideEnforcementRequestBody(Session session) {
        
            return String.format(

                "{\n" +
                "  \"enforcement_override\": {\n" +
                "  \"enforcement_override_result\": \"null,\"\n" +                     
                "  \"enforcer\": \"null,\"\n" +
                "  \"lja\": \"null\"\n" +
                "   }" +
                "}"
            );
        }
        public static String buildEnforcementRequestBody(Session session) {

            // Get enforcement from Gatling session
            String enforcement = session.get("EnforcementId") != null
                    ? session.get("EnforcementId").toString().trim().toUpperCase()
                    : "";
          

            // Stop early if enforcement is missing
            if (enforcement.isEmpty()) {
                throw new IllegalArgumentException(
                    "Enforcement value is NULL or EMPTY. " +
                    "Check that 'enforcement' is being added to the Gatling Session."
                );
            }

            DataGenerator randomStringGenerator = new DataGenerator();

            String reasonText1 = randomStringGenerator.generateRandomString(10);
            String reasonText2 = randomStringGenerator.generateRandomString(10);

            switch (enforcement) {

                /*
                * NOENF / CONF / INTL
                * All use the same parameters.
                */
                case "NOENF":
                case "CONF":
                case "INTL":
                case "WDN":
                case "NAP":
                case "REM":
                case "HTT": 
                case "FSN":                   

                    return String.format(
                        "{\n" +
                        "  \"enforcement_result_responses\": [\n" +
                        "    {\n" +
                        "      \"parameter_name\": \"reason\",\n" +
                        "      \"response\": \"%s\"\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"result_id\": \"%s\"\n" +
                        "}",
                        reasonText1,
                        enforcement
                    );                   


                /*
                * COLLO
                */
                case "COLLO":

                    return String.format(
                        "{\n" +
                        "  \"enforcement_result_responses\": [\n" +
                        "    {\n" +
                        "      \"parameter_name\": \"reason\",\n" +
                        "      \"response\": \"%s\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"parameter_name\": \"collectiontype\",\n" +
                        "      \"response\": \"Wages\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"parameter_name\": \"reserveterms\",\n" +
                        "      \"response\": \"%s\"\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"payment_terms\": {\n" +
                        "    \"date_days_in_default_imposed\": \"2026-08-14\",\n" +
                        "    \"days_in_default\": \"1\",\n" +
                        "    \"effective_date\": \"2026-08-04\",\n" +
                        "    \"extension\": \"true\",\n" +
                        "    \"instalment_amount\": \"10.99\",\n" +
                        "    \"instalment_period\": {\n" +
                        "      \"instalment_period_code\": \"M\",\n" +
                        "      \"instalment_period_display_name\": \"Monthly\"\n" +
                        "    },\n" +
                        "    \"lump_sum_amount\": \"null\",\n" +
                        "    \"payment_terms_type\": {\n" +
                        "      \"payment_terms_type_code\": \"I\",\n" +
                        "      \"payment_terms_type_display_name\": \"Instalments\"\n" +
                        "    },\n" +
                        "    \"posted_details\": {\n" +
                        "      \"posted_by\": \"\",\n" +
                        "      \"posted_by_name\": \"\",\n" +
                        "      \"posted_date\": \"\"\n" +
                        "    }\n" +
                        "  },\n" +
                        "  \"result_id\": \"COLLO\"\n" +
                        "}",
                        reasonText1,
                        reasonText2
                    );  

                /*
                * SC
                */
                case "SC":

                    return String.format(
                        "{\n" +
                        "  \"enforcement_result_responses\": [\n" +
                        "    {\n" +
                        "      \"parameter_name\": \"reason\",\n" +
                        "      \"response\": \"%s\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"parameter_name\": \"days_in_default\",\n" +
                        "      \"response\": \"3\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"parameter_name\": \"totalamount\",\n" +
                        "      \"response\": \"9.99\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"parameter_name\": \"paymentterms\",\n" +
                        "      \"response\": \"1\"\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"result_id\": \"SC\"\n" +
                        "}",
                        reasonText1
                    );

                /*
                * MPSO
                */
                case "MPSO":

                    return String.format(
                        "{\n" +
                        "  \"enforcement_result_responses\": [\n" +
                        "    {\n" +
                        "      \"parameter_name\": \"reason\",\n" +
                        "      \"response\": \"%s\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"parameter_name\": \"supervisor\",\n" +
                        "      \"response\": \"Probation officer\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"parameter_name\": \"detailsifother\",\n" +
                        "      \"response\": \"TEST\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"parameter_name\": \"prisondetention\",\n" +
                        "      \"response\": \"Prison\"\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"result_id\": \"%s\"\n" +
                        "}",
                        reasonText1,
                        enforcement
                    );
                   
                /*
                * CWN
                */
                case "CWN": //Need to remove?
                case "NAWT":
                case "REW":

                     return String.format(
                        "{\n" +
                        "  \"enforcement_result_responses\": [\n" +
                        "    {\n" +
                        "      \"parameter_name\": \"reason\",\n" +
                        "      \"response\": \"%s\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"parameter_name\": \"hearing_date\",\n" +
                        "      \"response\": \"2026-08-12\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"parameter_name\": \"court_code\",\n" +
                        "      \"response\": \"1\"\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"result_id\": \"%s\"\n" +
                        "}",
                        reasonText1,
                        enforcement
                    );
                /*
                * SUMM
                */
                case "SUMM":

                    return String.format(
                        "{\n" +
                        "  \"enforcement_result_responses\": [\n" +
                        "    {\n" +
                        "      \"parameter_name\": \"reason\",\n" +
                        "      \"response\": \"%s\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"parameter_name\": \"hearing_date\",\n" +
                        "      \"response\": \"2026-08-12\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"parameter_name\": \"court_code\",\n" +
                        "      \"response\": \"1\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"parameter_name\": \"prisondetention\",\n" +
                        "      \"response\": \"prison\"\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"result_id\": \"SUMM\"\n" +
                        "}",
                        reasonText1
                    );
                    
                /*
                * PGPAY
                */
                case "PGPAY":

                     return String.format(
                        "{\n" +
                        "  \"enforcement_result_responses\": [],\n" +
                        "  \"result_id\": \"PGPAY\"\n" +
                        "}"
                    );

                /*
                * PRIS
                */
                case "PRIS":

                     return String.format(
                        "{\n" +
                        "  \"enforcement_result_responses\": [\n" +
                        "    {\n" +
                        "      \"parameter_name\": \"earliestreleasedate\",\n" +
                        "      \"response\": \"2026-08-05\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"parameter_name\": \"prisonandprisonnumber\",\n" +
                        "      \"response\": \"A1234AA\"\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"result_id\": \"%s\"\n" +
                        "}",
                        reasonText1,
                        enforcement
                    );

                /*
                * S136
                */
                case "S136":

                     return String.format(
                        "{\n" +
                        "  \"enforcement_result_responses\": [\n" +
                        "    {\n" +
                        "      \"parameter_name\": \"reason\",\n" +
                        "      \"response\": \"test5\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"parameter_name\": \"timeofrelease\",\n" +
                        "      \"response\": \"14.30\"\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"result_id\": \"%s\"\n" +
                        "}",
                        reasonText1,
                        enforcement
                    );
                /*
                * AEO
                */
                case "AEO":

                    return String.format(
                        "{\n" +
                        "  \"enforcement_result_responses\": [\n" +
                        "    {\n" +
                        "      \"parameter_name\": \"reason\",\n" +
                        "      \"response\": \"%s\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"parameter_name\": \"normal_deduction_rate\",\n" +
                        "      \"response\": \"100.99\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"parameter_name\": \"protectedearningsrate\",\n" +
                        "      \"response\": \"100.99\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"parameter_name\": \"payperiod\",\n" +
                        "      \"response\": \"Monthly\"\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"result_id\": \"AEO\"\n" +
                        "}",
                        reasonText1
                    );

                /*
                * S18
                */
                case "S18": //Need to remove?

                    return String.format(
                        "{\n" +
                        "  \"enforcement_result_responses\": [\n" +
                        "    {\n" +
                        "      \"parameter_name\": \"reason\",\n" +
                        "      \"response\": \"%s\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"parameter_name\": \"datesuspcom\",\n" +
                        "      \"response\": \"2026-08-05\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"parameter_name\": \"previouspaymentterms\",\n" +
                        "      \"response\": \"TEST\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"parameter_name\": \"replydate\",\n" +
                        "      \"response\": \"2026-08-05\"\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"result_id\": \"S18\"\n" +
                        "}",
                        reasonText1
                    );

                     /*
                * UPWO
                */
                case "UPWO":

                    return String.format(
                        "{\n" +
                        "  \"enforcement_result_responses\": [\n" +
                        "    {\n" +
                        "      \"parameter_name\": \"reason\",\n" +
                        "      \"response\": \"%s\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"parameter_name\": \"noofhours\",\n" +
                        "      \"response\": \"1\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"parameter_name\": \"consecconcurrent\",\n" +
                        "      \"response\": \"consecutive\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"parameter_name\": \"completiondate\",\n" +
                        "      \"response\": \"2026-08-05\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"parameter_name\": \"supervisor\",\n" +
                        "      \"response\": \"An officer of a local probation board\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"parameter_name\": \"supervisingcourt\",\n" +
                        "      \"response\": \"test\"\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"result_id\": \"UPWO\"\n" +
                        "}",
                        reasonText1
                    );
                /*
                * CW
                */
                case "CW":

                    return String.format(
                        "{\n" +
                        "  \"enforcement_result_responses\": [\n" +
                        "    {\n" +
                        "      \"parameter_name\": \"reason\",\n" +
                        "      \"response\": \"%s\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"parameter_name\": \"prison\",\n" +
                        "      \"response\": \"Test\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"parameter_name\": \"commitaldays\",\n" +
                        "      \"response\": \"1\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"parameter_name\": \"consecconcurrent\",\n" +
                        "      \"response\": \"consecutive\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"parameter_name\": \"detailsconsecutive\",\n" +
                        "      \"response\": \"TEST\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"parameter_name\": \"processserver\",\n" +
                        "      \"response\": \"TEST\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"parameter_name\": \"basisofcommital\",\n" +
                        "      \"response\": \"TEST\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"parameter_name\": \"reasonnoaltused\",\n" +
                        "      \"response\": \"TEST\"\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"result_id\": \"CW\"\n" +
                        "}",
                        reasonText1
                    );

                /*
                * Unknown enforcement
                */
                default:

                    throw new IllegalArgumentException(
                        "Unknown enforcement type: [" + enforcement + "]"
                    );
            }
        }

            public static String BuildSearchEnforcementAccountRequestBody(Session session) {

            String businessUnitIdsJson = session.get("businessUnitIds") != null
                    ? session.get("businessUnitIds").toString()
                    : "[]";

            String forenames = session.getString("forename");
            String surname = session.getString("surname");

            return String.format(
                "{\n" +
                "  \"active_accounts_only\": true,\n" +
                "  \"business_unit_ids\": %s,\n" +
                "  \"defendant\": {\n" +
                "    \"address_line_1\": null,\n" +
                "    \"birth_date\": null,\n" +
                "    \"exact_match_forenames\": false,\n" +
                "    \"exact_match_organisation_name\": null,\n" +
                "    \"exact_match_surname\": false,\n" +
                "    \"forenames\": \"%s\",\n" +
                "    \"include_aliases\": false,\n" +
                "    \"national_insurance_number\": null,\n" +
                "    \"organisation\": false,\n" +
                "    \"organisation_name\": null,\n" +
                "    \"postcode\": null,\n" +
                "    \"surname\": \"%s\"\n" +
                "  },\n" +
                "    \"reference_number\": null\n" +
                "}",
                businessUnitIdsJson,
                forenames,
                surname
            );
        }        
    }
}