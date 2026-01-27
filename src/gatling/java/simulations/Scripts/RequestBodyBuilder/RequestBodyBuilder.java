package simulations.Scripts.RequestBodyBuilder;

import io.gatling.javaapi.core.Session;


/**
 * Factory class for building various request bodies used in the application.
 * This class delegates to specialised builders for each type of request.
 */
public class RequestBodyBuilder {

        public static String buildLoginRequestBody(Session session) {

        String userName = session.get("Username") != null ? session.get("Username").toString() : "";
        String getSFT = session.get("getSFT") != null ? "" + session.get("getSFT").toString() + "" : "null";
        String getSCtx = session.get("getSCtx") != null ? "" + session.get("getSCtx").toString() + "" : "null";


        return String.format("{\"checkPhones\": false," 
        + "\"country\":\"GB\","
        + "\"federationFlags\": 0,"
        + "\"flowToken\":\"%s\","
        + "\"forceotclogin\": false,"
        + "\"isAccessPassSupported\": true,"
        + "\"isCookieBannerShown\": false,"
        + "\"isExternalFederationDisallowed\": false,"
        + "\"isFidoSupported\": true,"
        + "\"isOtherIdpSupported\": false,"
        + "\"isQrCodePinSupported\": true,"
        + "\"isRemoteConnectSupported\": false,"
        + "\"isRemoteNGCSupported\": true,"
        + "\"isSignup\": false,"
        + "\"originalRequest\":\"%s\","
        +"\"username\":\"%s\"}",        
        getSFT, getSCtx, userName );
    }    

    public static String BuildDraftAccountRequestBody(Session session) {

        String userName = session.get("Username") != null ? session.get("Username").toString() : "";
        
        // Generate random values using DataGenerator
        String forename = DataGenerator.generateRandomFirstName();
        String surname = DataGenerator.generateRandomLastName();
        String employeeRef = DataGenerator.generateRandomEmployeeReference();
        String vehicleReg = DataGenerator.generateRandomVehicleRegistration();
        String nin = DataGenerator.generateRandomNationalInsuranceNumber();
        String mobilePhone = "07700" + DataGenerator.generateRandomNumber(100000, 999999);
        String fpRegNumber = DataGenerator.generateRandomFPRegistrationNumber();
        String noticeNumber = DataGenerator.generateRandomNoticeNumber();
        long imposingCourtId = 650000000000L + (1 + new java.util.Random().nextInt(100));
        int majorCreditorId = 1 + new java.util.Random().nextInt(50);

        // Store generated data in session for reuse in other methods
        session.set("generatedForename", forename);
        session.set("generatedSurname", surname);
        session.set("generatedNin", nin);
        session.set("generatedVehicleReg", vehicleReg);
        session.set("generatedEmployeeRef", employeeRef);
        session.set("generatedAddressLine1", DataGenerator.generateRandomAddress());
        session.set("generatedAddressLine2", DataGenerator.generateRandomCity());
        session.set("generatedVehicleMake", DataGenerator.generateRandomVehicleMake());
        session.set("generatedEmployerCompanyName", DataGenerator.generateRandomEmployerName());
        session.set("generatedProsecutorCaseRef", "CASE" + DataGenerator.generateRandomNumber(100000, 999999));
        session.set("generatedEmployerAddressLine1", DataGenerator.generateRandomAddress());
        session.set("generatedEmployerAddressLine2", DataGenerator.generateRandomCity());

        return String.format(
        "{\n" +
        "    \"account\": {\n" +
        "        \"account_notes\": [\n" +
        "            {\n" +
        "                \"account_note_serial\": 3,\n" +
        "                \"account_note_text\": \"Account under review\",\n" +
        "                \"note_type\": \"AC\"\n" +
        "            },\n" +
        "            {\n" +
        "                \"account_note_serial\": 2,\n" +
        "                \"account_note_text\": \"Payment arrangement proposed\\n\",\n" +
        "                \"note_type\": \"AA\"\n" +
        "            }\n" +
        "        ],\n" +
        "        \"account_sentence_date\": \"2026-01-07\",\n" +
        "        \"account_type\": \"Fixed Penalty\",\n" +
        "        \"collection_order_date\": null,\n" +
        "        \"collection_order_made\": null,\n" +
        "        \"collection_order_made_today\": null,\n" +
        "        \"defendant\": {\n" +
        "            \"address_line_1\": \"123 Main Street\",\n" +
        "            \"address_line_2\": \"London\",\n" +
        "            \"address_line_3\": \"United Kingdom\",\n" +
        "            \"address_line_4\": null,\n" +
        "            \"address_line_5\": null,\n" +
        "            \"company_flag\": false,\n" +
        "            \"company_name\": null,\n" +
        "            \"cro_number\": null,\n" +
        "            \"custody_status\": null,\n" +
        "            \"debtor_detail\": {\n" +
        "                \"aliases\": null,\n" +
        "                \"document_language\": null,\n" +
        "                \"employee_reference\": null,\n" +
        "                \"employer_address_line_1\": null,\n" +
        "                \"employer_address_line_2\": null,\n" +
        "                \"employer_address_line_3\": null,\n" +
        "                \"employer_address_line_4\": null,\n" +
        "                \"employer_address_line_5\": null,\n" +
        "                \"employer_company_name\": null,\n" +
        "                \"employer_email_address\": null,\n" +
        "                \"employer_post_code\": null,\n" +
        "                \"employer_telephone_number\": null,\n" +
        "                \"hearing_language\": null,\n" +
        "                \"vehicle_make\": null,\n" +
        "                \"vehicle_registration_mark\": null\n" +
        "            },\n" +
        "            \"dob\": \"1985-06-15\",\n" +
        "            \"driving_licence_number\": null,\n" +
        "            \"email_address_1\": null,\n" +
        "            \"email_address_2\": null,\n" +
        "            \"ethnicity_observed\": null,\n" +
        "            \"ethnicity_self_defined\": null,\n" +
        "            \"forenames\": \"%s\",\n" +
        "            \"gender\": null,\n" +
        "            \"interpreter_lang\": null,\n" +
        "            \"national_insurance_number\": null,\n" +
        "            \"nationality_1\": null,\n" +
        "            \"nationality_2\": null,\n" +
        "            \"occupation\": null,\n" +
        "            \"parent_guardian\": null,\n" +
        "            \"pnc_id\": null,\n" +
        "            \"post_code\": \"SW1A 1AA\",\n" +
        "            \"prison_number\": null,\n" +
        "            \"surname\": \"%s\",\n" +
        "            \"telephone_number_business\": null,\n" +
        "            \"telephone_number_home\": null,\n" +
        "            \"telephone_number_mobile\": null,\n" +
        "            \"title\": \"Mr\"\n" +
        "        },\n" +
        "        \"defendant_type\": \"adultOrYouthOnly\",\n" +
        "        \"enforcement_court_id\": 650000000004,\n" +
        "        \"fp_ticket_detail\": {\n" +
        "            \"date_of_issue\": \"2026-01-07\",\n" +
        "            \"fp_driving_licence_number\": \"ABCDE123456AA1B1\",\n" +
        "            \"fp_registration_number\": \"%s\",\n" +
        "            \"notice_number\": \"%s\",\n" +
        "            \"notice_to_owner_hirer\": \"ASFAS3523\",\n" +
        "            \"place_of_offence\": \"London High Street\",\n" +
        "            \"time_of_issue\": \"14:00\"\n" +
        "        },\n" +
        "        \"offences\": [\n" +
        "            {\n" +
        "                \"date_of_sentence\": \"2026-01-07\",\n" +
        "                \"imposing_court_id\": %d,\n" +
        "                \"impositions\": [\n" +
        "                    {\n" +
        "                        \"amount_imposed\": 800,\n" +
        "                        \"amount_paid\": 0,\n" +
        "                        \"major_creditor_id\": %d,\n" +
        "                        \"minor_creditor\": null,\n" +
        "                        \"result_id\": \"FO\"\n" +
        "                    }\n" +
        "                ],\n" +
        "                \"offence_id\": 33369\n" +
        "            }\n" +
        "        ],\n" +
        "        \"originator_id\": 23,\n" +
        "        \"originator_name\": \"undefined\",\n" +
        "        \"payment_card_request\": null,\n" +
        "        \"payment_terms\": {\n" +
        "            \"default_days_in_jail\": null,\n" +
        "            \"effective_date\": null,\n" +
        "            \"enforcements\": null,\n" +
        "            \"instalment_amount\": null,\n" +
        "            \"instalment_period\": null,\n" +
        "            \"lump_sum_amount\": null,\n" +
        "            \"payment_terms_type_code\": \"B\"\n" +
        "        },\n" +
        "        \"prosecutor_case_reference\": null,\n" +
        "        \"suspended_committal_date\": null\n" +
        "    },\n" +
        "    \"account_snapshot\": null,\n" +
        "    \"account_status\": \"Submitted\",\n" +
        "    \"account_status_date\": null,\n" +
        "    \"account_status_message\": null,\n" +
        "    \"account_type\": \"Fixed Penalty\",\n" +
        "    \"business_unit_id\": 65,\n" +
        "    \"created_at\": null,\n" +
        "    \"draft_account_id\": null,\n" +
        "    \"submitted_by\": \"L065JG\",\n" +
        "    \"submitted_by_name\": \"%s\",\n" +
        "    \"timeline_data\": [\n" +
        "        {\n" +
        "            \"reason_text\": null,\n" +
        "            \"status\": \"Submitted\",\n" +
        "            \"status_date\": \"2026-01-09\",\n" +
        "            \"username\": \"%s\"\n" +
        "        }\n" +
        "    ],\n" +
        "    \"version\": \"0\"\n" +
        "}",
        forename, surname, fpRegNumber, noticeNumber,
        imposingCourtId, majorCreditorId, userName, userName);
    }

    public static String BuildDraftAccountFineRequestBody(Session session) {

        String userName = session.get("Username") != null ? session.get("Username").toString() : "";
        
        // Retrieve reused data from session (generated in BuildDraftAccountRequestBody)
        String forename = session.get("generatedForename") != null ? session.get("generatedForename").toString() : DataGenerator.generateRandomFirstName();
        String surname = session.get("generatedSurname") != null ? session.get("generatedSurname").toString() : DataGenerator.generateRandomLastName();
        String nin = session.get("generatedNin") != null ? session.get("generatedNin").toString() : DataGenerator.generateRandomNationalInsuranceNumber();
        String vehicleReg = session.get("generatedVehicleReg") != null ? session.get("generatedVehicleReg").toString() : DataGenerator.generateRandomVehicleRegistration();
        String email1 = session.get("generatedEmail1") != null ? session.get("generatedEmail1").toString() : (forename.toLowerCase() + "." + surname.toLowerCase() + "@example.com");
        String employerCompanyName = session.get("generatedEmployerCompanyName") != null ? session.get("generatedEmployerCompanyName").toString() : DataGenerator.generateRandomEmployerName();
        String employeeRef = session.get("generatedEmployeeRef") != null ? session.get("generatedEmployeeRef").toString() : DataGenerator.generateRandomEmployeeReference();
        String addressLine1 = session.get("generatedAddressLine1") != null ? session.get("generatedAddressLine1").toString() : DataGenerator.generateRandomAddress();
        String addressLine2 = session.get("generatedAddressLine2") != null ? session.get("generatedAddressLine2").toString() : DataGenerator.generateRandomCity();
        String vehicleMake = session.get("generatedVehicleMake") != null ? session.get("generatedVehicleMake").toString() : DataGenerator.generateRandomVehicleMake();
        String prosecutorCaseRef = session.get("generatedProsecutorCaseRef") != null ? session.get("generatedProsecutorCaseRef").toString() : ("CASE" + DataGenerator.generateRandomNumber(100000, 999999));
        String employerAddressLine1 = session.get("generatedEmployerAddressLine1") != null ? session.get("generatedEmployerAddressLine1").toString() : DataGenerator.generateRandomAddress();
        String employerAddressLine2 = session.get("generatedEmployerAddressLine2") != null ? session.get("generatedEmployerAddressLine2").toString() : DataGenerator.generateRandomCity();
        String accountNoteText = "Account review for " + forename + " " + surname;

        return String.format(
        "{\n" +
        "    \"draft_account_id\": null,\n" +
        "    \"created_at\": null,\n" +
        "    \"account_snapshot\": null,\n" +
        "    \"account_status_date\": null,\n" +
        "    \"business_unit_id\": 77,\n" +
        "    \"submitted_by\": \"L077JG\",\n" +
        "    \"submitted_by_name\": \"%s\",\n" +
        "    \"account\": {\n" +
        "        \"account_type\": \"Fine\",\n" +
        "        \"defendant_type\": \"adultOrYouthOnly\",\n" +
        "        \"originator_name\": \"Aberdeen JP Court\",\n" +
        "        \"originator_id\": 9251,\n" +
        "        \"prosecutor_case_reference\": \"%s\",\n" +
        "        \"enforcement_court_id\": 770000000042,\n" +
        "        \"collection_order_made\": null,\n" +
        "        \"collection_order_made_today\": null,\n" +
        "        \"collection_order_date\": null,\n" +
        "        \"suspended_committal_date\": null,\n" +
        "        \"payment_card_request\": null,\n" +
        "        \"account_sentence_date\": \"2026-01-14\",\n" +
        "        \"defendant\": {\n" +
        "            \"company_flag\": false,\n" +
        "            \"title\": \"Mr\",\n" +
        "            \"surname\": \"%s\",\n" +
        "            \"forenames\": \"%s\",\n" +
        "            \"company_name\": null,\n" +
        "            \"dob\": \"2013-01-02\",\n" +
        "            \"address_line_1\": \"%s\",\n" +
        "            \"address_line_2\": \"%s\",\n" +
        "            \"address_line_3\": null,\n" +
        "            \"address_line_4\": null,\n" +
        "            \"address_line_5\": null,\n" +
        "            \"post_code\": null,\n" +
        "            \"telephone_number_home\": null,\n" +
        "            \"telephone_number_business\": null,\n" +
        "            \"telephone_number_mobile\": null,\n" +
        "            \"email_address_1\": \"%s\",\n" +
        "            \"email_address_2\": null,\n" +
        "            \"national_insurance_number\": \"%s\",\n" +
        "            \"driving_licence_number\": null,\n" +
        "            \"pnc_id\": null,\n" +
        "            \"nationality_1\": null,\n" +
        "            \"nationality_2\": null,\n" +
        "            \"ethnicity_self_defined\": null,\n" +
        "            \"ethnicity_observed\": null,\n" +
        "            \"cro_number\": null,\n" +
        "            \"occupation\": null,\n" +
        "            \"gender\": null,\n" +
        "            \"custody_status\": null,\n" +
        "            \"prison_number\": null,\n" +
        "            \"interpreter_lang\": null,\n" +
        "            \"debtor_detail\": {\n" +
        "                \"vehicle_make\": \"%s\",\n" +
        "                \"vehicle_registration_mark\": \"%s\",\n" +
        "                \"document_language\": null,\n" +
        "                \"hearing_language\": null,\n" +
        "                \"employee_reference\": \"%s\",\n" +
        "                \"employer_company_name\": \"%s\",\n" +
        "                \"employer_address_line_1\": \"%s\",\n" +
        "                \"employer_address_line_2\": \"%s\",\n" +
        "                \"employer_address_line_3\": null,\n" +
        "                \"employer_address_line_4\": null,\n" +
        "                \"employer_address_line_5\": null,\n" +
        "                \"employer_post_code\": null,\n" +
        "                \"employer_telephone_number\": null,\n" +
        "                \"employer_email_address\": null,\n" +
        "                \"aliases\": null\n" +
        "            },\n" +
        "            \"parent_guardian\": null\n" +
        "        },\n" +
        "        \"offences\": [\n" +
        "            {\n" +
        "                \"date_of_sentence\": \"2026-01-14\",\n" +
        "                \"imposing_court_id\": null,\n" +
        "                \"offence_id\": 33369,\n" +
        "                \"impositions\": [\n" +
        "                    {\n" +
        "                        \"result_id\": \"FCOMP\",\n" +
        "                        \"amount_imposed\": 500,\n" +
        "                        \"amount_paid\": 200,\n" +
        "                        \"major_creditor_id\": 770000000041,\n" +
        "                        \"minor_creditor\": null\n" +
        "                    }\n" +
        "                ]\n" +
        "            }\n" +
        "        ],\n" +
        "        \"fp_ticket_detail\": null,\n" +
        "        \"payment_terms\": {\n" +
        "            \"payment_terms_type_code\": \"B\",\n" +
        "            \"effective_date\": \"2026-01-14\",\n" +
        "            \"instalment_period\": null,\n" +
        "            \"lump_sum_amount\": null,\n" +
        "            \"instalment_amount\": null,\n" +
        "            \"default_days_in_jail\": null,\n" +
        "            \"enforcements\": null\n" +
        "        },\n" +
        "        \"account_notes\": [{\"account_note_serial\": 1, \"account_note_text\": \"%s\", \"note_type\": \"AC\"}]\n" +
        "    },\n" +
        "    \"account_type\": \"Fine\",\n" +
        "    \"account_status\": \"Submitted\",\n" +
        "    \"account_status_message\": null,\n" +
        "    \"timeline_data\": [\n" +
        "        {\n" +
        "            \"username\": \"%s\",\n" +
        "            \"status\": \"Submitted\",\n" +
        "            \"status_date\": \"2026-01-14\",\n" +
        "            \"reason_text\": null\n" +
        "        }\n" +
        "    ],\n" +
        "    \"version\": \"0\"\n" +
        "}",
        userName, prosecutorCaseRef, surname, forename, addressLine1, addressLine2, email1, nin, vehicleMake, vehicleReg, employeeRef, employerCompanyName, employerAddressLine1, employerAddressLine2, accountNoteText, userName);
    }

    public static String BuildDraftAccountConditionalCautionRequestBody(Session session) {

        String userName = session.get("Username") != null ? session.get("Username").toString() : "";
        
        // Retrieve reused data from session (generated in BuildDraftAccountRequestBody)
        String forename = session.get("generatedForename") != null ? session.get("generatedForename").toString() : DataGenerator.generateRandomFirstName();
        String surname = session.get("generatedSurname") != null ? session.get("generatedSurname").toString() : DataGenerator.generateRandomLastName();
        String nin = session.get("generatedNin") != null ? session.get("generatedNin").toString() : DataGenerator.generateRandomNationalInsuranceNumber();
        String vehicleReg = session.get("generatedVehicleReg") != null ? session.get("generatedVehicleReg").toString() : DataGenerator.generateRandomVehicleRegistration();
        String email1 = session.get("generatedEmail1") != null ? session.get("generatedEmail1").toString() : (forename.toLowerCase() + "." + surname.toLowerCase() + "@example.com");
        String employerCompanyName = session.get("generatedEmployerCompanyName") != null ? session.get("generatedEmployerCompanyName").toString() : DataGenerator.generateRandomEmployerName();
        String employeeRef = session.get("generatedEmployeeRef") != null ? session.get("generatedEmployeeRef").toString() : DataGenerator.generateRandomEmployeeReference();
        String addressLine1 = session.get("generatedAddressLine1") != null ? session.get("generatedAddressLine1").toString() : DataGenerator.generateRandomAddress();
        String vehicleMake = session.get("generatedVehicleMake") != null ? session.get("generatedVehicleMake").toString() : DataGenerator.generateRandomVehicleMake();
        String prosecutorCaseRef = session.get("generatedProsecutorCaseRef") != null ? session.get("generatedProsecutorCaseRef").toString() : ("CASE" + DataGenerator.generateRandomNumber(100000, 999999));
        String employerAddressLine1 = session.get("generatedEmployerAddressLine1") != null ? session.get("generatedEmployerAddressLine1").toString() : DataGenerator.generateRandomAddress();
        String accountNoteText = "Account review for " + forename + " " + surname;

        return String.format(
        "{\n" +
        "    \"draft_account_id\": null,\n" +
        "    \"created_at\": null,\n" +
        "    \"account_snapshot\": null,\n" +
        "    \"account_status_date\": null,\n" +
        "    \"business_unit_id\": 77,\n" +
        "    \"submitted_by\": \"L077JG\",\n" +
        "    \"submitted_by_name\": \"%s\",\n" +
        "    \"account\": {\n" +
        "        \"account_notes\": [\n" +
        "            {\n" +
        "                \"account_note_serial\": 3,\n" +
        "                \"account_note_text\": \"%s\",\n" +
        "                \"note_type\": \"AC\"\n" +
        "            },\n" +
        "            {\n" +
        "                \"account_note_serial\": 2,\n" +
        "                \"account_note_text\": \"%s\",\n" +
        "                \"note_type\": \"AA\"\n" +
        "            }\n" +
        "        ],\n" +
        "        \"account_sentence_date\": \"2026-01-14\",\n" +
        "        \"account_type\": \"Conditional Caution\",\n" +
        "        \"collection_order_date\": null,\n" +
        "        \"collection_order_made\": null,\n" +
        "        \"collection_order_made_today\": null,\n" +
        "        \"defendant\": {\n" +
        "            \"address_line_1\": \"%s\",\n" +
        "            \"address_line_2\": null,\n" +
        "            \"address_line_3\": null,\n" +
        "            \"address_line_4\": null,\n" +
        "            \"address_line_5\": null,\n" +
        "            \"company_flag\": false,\n" +
        "            \"company_name\": null,\n" +
        "            \"cro_number\": null,\n" +
        "            \"custody_status\": null,\n" +
        "            \"debtor_detail\": {\n" +
        "                \"aliases\": null,\n" +
        "                \"document_language\": null,\n" +
        "                \"employee_reference\": \"%s\",\n" +
        "                \"employer_address_line_1\": \"%s\",\n" +
        "                \"employer_address_line_2\": null,\n" +
        "                \"employer_address_line_3\": null,\n" +
        "                \"employer_address_line_4\": null,\n" +
        "                \"employer_address_line_5\": null,\n" +
        "                \"employer_company_name\": \"%s\",\n" +
        "                \"employer_email_address\": \"%s\",\n" +
        "                \"employer_post_code\": null,\n" +
        "                \"employer_telephone_number\": null,\n" +
        "                \"hearing_language\": null,\n" +
        "                \"vehicle_make\": \"%s\",\n" +
        "                \"vehicle_registration_mark\": \"%s\"\n" +
        "            },\n" +
        "            \"dob\": \"2015-01-08\",\n" +
        "            \"driving_licence_number\": null,\n" +
        "            \"email_address_1\": \"%s\",\n" +
        "            \"email_address_2\": null,\n" +
        "            \"ethnicity_observed\": null,\n" +
        "            \"ethnicity_self_defined\": null,\n" +
        "            \"forenames\": \"%s\",\n" +
        "            \"gender\": null,\n" +
        "            \"interpreter_lang\": null,\n" +
        "            \"national_insurance_number\": \"%s\",\n" +
        "            \"nationality_1\": null,\n" +
        "            \"nationality_2\": null,\n" +
        "            \"occupation\": null,\n" +
        "            \"parent_guardian\": null,\n" +
        "            \"pnc_id\": null,\n" +
        "            \"post_code\": null,\n" +
        "            \"prison_number\": null,\n" +
        "            \"surname\": \"%s\",\n" +
        "            \"telephone_number_business\": null,\n" +
        "            \"telephone_number_home\": null,\n" +
        "            \"telephone_number_mobile\": null,\n" +
        "            \"title\": \"Mr\"\n" +
        "        },\n" +
        "        \"defendant_type\": \"adultOrYouthOnly\",\n" +
        "        \"enforcement_court_id\": 770000000021,\n" +
        "        \"fp_ticket_detail\": null,\n" +
        "        \"offences\": [\n" +
        "            {\n" +
        "                \"date_of_sentence\": \"2026-01-14\",\n" +
        "                \"imposing_court_id\": null,\n" +
        "                \"impositions\": [\n" +
        "                    {\n" +
        "                        \"amount_imposed\": 500,\n" +
        "                        \"amount_paid\": 100,\n" +
        "                        \"major_creditor_id\": null,\n" +
        "                        \"minor_creditor\": null,\n" +
        "                        \"result_id\": \"FVS\"\n" +
        "                    }\n" +
        "                ],\n" +
        "                \"offence_id\": 33369\n" +
        "            }\n" +
        "        ],\n" +
        "        \"originator_id\": 9251,\n" +
        "        \"originator_name\": \"Aberdeen JP Court\",\n" +
        "        \"payment_card_request\": null,\n" +
        "        \"payment_terms\": {\n" +
        "            \"default_days_in_jail\": null,\n" +
        "            \"effective_date\": \"2026-01-31\",\n" +
        "            \"enforcements\": null,\n" +
        "            \"instalment_amount\": null,\n" +
        "            \"instalment_period\": null,\n" +
        "            \"lump_sum_amount\": null,\n" +
        "            \"payment_terms_type_code\": \"B\"\n" +
        "        },\n" +
        "        \"prosecutor_case_reference\": \"%s\",\n" +
        "        \"suspended_committal_date\": null\n" +
        "    },\n" +
        "    \"account_snapshot\": null,\n" +
        "    \"account_status\": \"Submitted\",\n" +
        "    \"account_status_date\": null,\n" +
        "    \"account_status_message\": null,\n" +
        "    \"account_type\": \"Conditional Caution\",\n" +
        "    \"business_unit_id\": 77,\n" +
        "    \"created_at\": null,\n" +
        "    \"draft_account_id\": null,\n" +
        "    \"submitted_by\": \"L077JG\",\n" +
        "    \"submitted_by_name\": \"%s\",\n" +
        "    \"timeline_data\": [\n" +
        "        {\n" +
        "            \"reason_text\": null,\n" +
        "            \"status\": \"Submitted\",\n" +
        "            \"status_date\": \"2026-01-15\",\n" +
        "            \"username\": \"%s\"\n" +
        "        }\n" +
        "    ],\n" +
        "    \"version\": \"0\"\n" +
        "}",
        userName, accountNoteText, accountNoteText, addressLine1, employeeRef, employerAddressLine1, employerCompanyName, email1, vehicleMake, vehicleReg, email1, forename, nin, surname, prosecutorCaseRef, userName, userName);
    }

   public static String BuildApproveAccountRequestBody(Session session) {

        // Gatling username
        String userName = session.get("Username") != null
            ? session.get("Username").toString()
            : "";

        // Business unit ID
        String businessUnitId = session.get("selectedBusinessUnitId") != null
            ? session.get("selectedBusinessUnitId").toString()
            : "65";

        // Extracted from draft summaries
        String validatedByCode = session.get("submittedBy") != null
            ? session.get("submittedBy").toString()
            : "";

        String validatedByName = session.get("submittedByName") != null
            ? session.get("submittedByName").toString()
            : "";

        // Current date
        String statusDate = java.time.LocalDate.now().toString();

        return String.format(
            "{\n" +
            "  \"account_status\": \"Publishing Pending\",\n" +
            "  \"business_unit_id\": %s,\n" +
            "  \"reason_text\": null,\n" +
            "  \"timeline_data\": [\n" +
            "    {\n" +
            "      \"reason_text\": null,\n" +
            "      \"status\": \"Submitted\",\n" +
            "      \"status_date\": \"2026-01-09\",\n" +
            "      \"username\": \"%s\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"reason_text\": null,\n" +
            "      \"status\": \"Publishing Pending\",\n" +
            "      \"status_date\": \"%s\",\n" +
            "      \"username\": \"%s\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"validated_by\": \"%s\",\n" +
            "  \"validated_by_name\": \"%s\",\n" +
            "  \"version\": \"\\\"0\\\"\"\n" +
            "}",
            businessUnitId, userName, statusDate, userName, validatedByCode, validatedByName
        );
    }

    public static String BuildRejectAccountRequestBody(Session session) {

        // Gatling username
        String userName = session.get("Username") != null
            ? session.get("Username").toString()
            : "";

        // Business unit ID
        String businessUnitId = session.get("selectedBusinessUnitId") != null
            ? session.get("selectedBusinessUnitId").toString()
            : "65";

        // Random rejection reason text
        String[] rejectionReasons = {
            "Incomplete documentation",
            "Invalid personal details",
            "Insufficient evidence",
            "Duplicate account detected",
            "Failed verification process",
            "Information does not match records",
            "Required documents missing",
            "Account data inconsistency",
            "Unable to verify identity",
            "Incorrect business unit assignment"
        };
        String reasonText = rejectionReasons[new java.util.Random().nextInt(rejectionReasons.length)];

        // Current date
        String statusDate = java.time.LocalDate.now().toString();

        return String.format(
            "{\n" +
            "  \"account_status\": \"Rejected\",\n" +
            "  \"business_unit_id\": %s,\n" +
            "  \"reason_text\": \"%s\",\n" +
            "  \"timeline_data\": [\n" +
            "    {\n" +
            "      \"reason_text\": null,\n" +
            "      \"status\": \"Submitted\",\n" +
            "      \"status_date\": \"2026-01-09\",\n" +
            "      \"username\": \"%s\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"reason_text\": \"%s\",\n" +
            "      \"status\": \"Rejected\",\n" +
            "      \"status_date\": \"%s\",\n" +
            "      \"username\": \"%s\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"validated_by\": null,\n" +
            "  \"validated_by_name\": null,\n" +
            "  \"version\": \"\\\"0\\\"\"\n" +
            "}",
            businessUnitId, reasonText, userName, reasonText, statusDate, userName
        );
    }

}