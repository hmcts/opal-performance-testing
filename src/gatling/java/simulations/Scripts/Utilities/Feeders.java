package simulations.Scripts.Utilities;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.sql.Connection;

import io.gatling.javaapi.core.CoreDsl;
import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.jdbc.JdbcDsl;
import io.gatling.javaapi.core.CheckBuilder;
import static io.gatling.javaapi.core.CoreDsl.*;


public class Feeders {

    public static final FeederBuilder<String> Users;
    public static final FeederBuilder<String> CheckerUsers;
    public static final FeederBuilder<String> InputterUsers;
    //MH adding R1b things
    public static final FeederBuilder<String> SearchViewUsers;
    public static final FeederBuilder<String> PGAccounts;
    public static final FeederBuilder<String> PGUsers;
    public static final FeederBuilder<String> MajorCreditorUsers;
    public static final FeederBuilder<String> MinorCreditorUsers;
    public static final FeederBuilder<String> AmendEnforcementUsers;
    public static final FeederBuilder<String> DefendantSearchAccounts;
    public static final FeederBuilder<String> DefendantSearchUsers;
    public static final FeederBuilder<String> CompanyAccountUsers;
    public static final FeederBuilder<String> ConvertDefendantAccounts;



    //Data Changes on accounts
    public static final FeederBuilder<String> DraftAccounts;
    public static final boolean USE_CSV_DRAFT_ACCOUNT = false;

    private static final AtomicInteger COUNTER;
    private static final Logger log = Logger.getLogger(Feeders.class.getName());

   static { 
    try {            
        Users = CoreDsl.csv(AppConfig.FileConfig.CsvFiles.USERS_CSV).circular();
        CheckerUsers = CoreDsl.csv(AppConfig.FileConfig.CsvFiles.CHECKER_USERS_CSV).circular();
        InputterUsers = CoreDsl.csv(AppConfig.FileConfig.CsvFiles.INPUTTER_USERS_CSV).circular();
        SearchViewUsers = CoreDsl.csv(AppConfig.FileConfig.CsvFiles.SEARCHVIEW_USERS_CSV).circular(); //MH added this as well for R1b
        PGAccounts = CoreDsl.csv(AppConfig.FileConfig.CsvFiles.PG_ACCOUNTS_CSV).circular();
        PGUsers = CoreDsl.csv(AppConfig.FileConfig.CsvFiles.PG_USERS_CSV).circular();
        MajorCreditorUsers = CoreDsl.csv(AppConfig.FileConfig.CsvFiles.MAJOR_CREDITOR_USERS_CSV).circular();
        MinorCreditorUsers = CoreDsl.csv(AppConfig.FileConfig.CsvFiles.MINOR_CREDITOR_USERS_CSV).circular();
        DraftAccounts = CoreDsl.csv(AppConfig.FileConfig.CsvFiles.DRAFT_ACCOUNTS_CSV).circular();
        AmendEnforcementUsers = CoreDsl.csv(AppConfig.FileConfig.CsvFiles.AMEND_ENFORCEMENT_USERS_CSV).circular();
        DefendantSearchAccounts = CoreDsl.csv(AppConfig.FileConfig.CsvFiles.DEFENDANTSEARCHACCOUNTS_CSV).circular();
        DefendantSearchUsers = CoreDsl.csv(AppConfig.FileConfig.CsvFiles.DEFENDANTSEARCH_USERS_CSV).circular();
        CompanyAccountUsers = CoreDsl.csv(AppConfig.FileConfig.CsvFiles.COMPANYACCOUNT_USERS_CSV).circular();
        ConvertDefendantAccounts  = CoreDsl.csv(AppConfig.FileConfig.CsvFiles.CONVERTDEFENDANTACCOUNT_CSV).circular();

    } catch (Exception e) {
        System.err.println("Error loading CSV: " + e.getMessage());
        throw e;
    }

        COUNTER = new AtomicInteger(0);
    }
    
    public static FeederBuilder<String> createUsers() {
        return Users;
    }  
    
   public static FeederBuilder<String> inputterUsers() {
        return InputterUsers;
    } 
    public static FeederBuilder<String> checkerUsers() {
        return CheckerUsers;
    }    
    //MH AND HAD TO ADD IT HERE
    public static FeederBuilder<String> searchViewUsers() {
        return SearchViewUsers;
    }

    public static FeederBuilder<String> pGAccounts() {
        return PGAccounts;
    } 

    public static FeederBuilder<String> pGUsers() {
        return PGUsers;
    }

    public static FeederBuilder<String> companyAccountUsers() {
        return CompanyAccountUsers;
    }

    public static FeederBuilder<String> convertDefendantAccounts() {
        return ConvertDefendantAccounts;
    }
    
    public static FeederBuilder<String> majorCreditorUsers() {
        return MajorCreditorUsers;
    }

    public static FeederBuilder<String> minorCreditorUsers() {
        return MinorCreditorUsers;
    }

    public static FeederBuilder<String> amendEnforcementUsers() {
        return AmendEnforcementUsers;
    } 
    public static FeederBuilder<String> defendantSearchAccounts() {
        return DefendantSearchAccounts;
    }
    public static FeederBuilder<String> defendantSearchUsers() {
        return DefendantSearchUsers;
    }

    public static CheckBuilder.Final saveTokenCode() {
        return CoreDsl.css("input[name='code']", "value").saveAs("TokenCode");
    }

    public static CheckBuilder.Final saveClientInfo() {
        return CoreDsl.css("input[name='client_info']", "value").saveAs("getClientInfo");
    }
        public static CheckBuilder.Final saveSessionState() {
        return CoreDsl.css("input[name='session_state']", "value").saveAs("getSessionState");
    }
        public static CheckBuilder.Final saveClientRequestId() 
        {return regex("client-request-id=([^\\\\u0026]+)").saveAs("getClientRequestId");
    }


    public static CheckBuilder.Final saveApiCanary() {
        return CoreDsl.regex("\"apiCanary\"\\s*:\\s*\"([^\"]+)\"").saveAs("apiCanary");
    }
    public static CheckBuilder.Final saveCanary() {
        return CoreDsl.regex("\"canary\"\\s*:\\s*\"([^\"]+)\"").saveAs("getCanary");
    }
    public static CheckBuilder.Final saveSessionId() {
        return CoreDsl.regex("\"sessionId\"\\s*:\\s*\"([^\"]+)\"").saveAs("sessionId");
    }    public static CheckBuilder.Final saveSFT() {
        return CoreDsl.regex("\"sFT\"\\s*:\\s*\"([^\"]+)\"").saveAs("getSFT");
    }
    public static CheckBuilder.Final saveSCtx() {
        return CoreDsl.regex("\"sCtx\"\\s*:\\s*\"([^\"]+)\"").saveAs("getSCtx");
    }

    public static CheckBuilder.Final saveBusinessUnitId() {
        return CoreDsl.jsonPath("$.refData[*].business_unit_id").findRandom().saveAs("selectedBusinessUnitId");
    }
    public static CheckBuilder.Final saveCourtId() {
        return CoreDsl.jsonPath("$.refData[*].court_id").findRandom().saveAs("getCourtId");
    }
    public static CheckBuilder.Final saveProsecutorId() {
        return CoreDsl.jsonPath("$.ref_data[*].prosecutor_id").findAll().saveAs("getProsecutorId");
    }
    public static CheckBuilder.Final saveProsecutorName() {
        return CoreDsl.jsonPath("$.ref_data[*].name").findAll().saveAs("getProsecutorName");
    }
    
    public static CheckBuilder.Final saveProsecutors() {
        return CoreDsl.jsonPath("$.ref_data[*]").findAll().saveAs("prosecutors");
    }
    public static CheckBuilder.Final saveErrorDetails() {
        return CoreDsl.jsonPath("$.detail").optional().saveAs("getDetail");
    }
    public static CheckBuilder.Final saveCreatedAccountId() {
        return CoreDsl.jsonPath("$.draft_account_id").findRandom().saveAs("getCreatedAccountId");
    }

    public static FeederBuilder<Object> listFeeder(String key, List<Object> items) {
        return CoreDsl.listFeeder(items.stream()
            .map(item -> Map.of(key, item)).toList()
        );
    }   
 
 
    public static void executeUpdate(String sql) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            log.info("Executing update: " + AppConfig.DatabaseConfig.URL + ", " + AppConfig.DatabaseConfig.USERNAME + ", " + sql);
            connection = DriverManager.getConnection(AppConfig.DatabaseConfig.URL, AppConfig.DatabaseConfig.USERNAME, AppConfig.DatabaseConfig.PASSWORD);
            preparedStatement = connection.prepareStatement(sql);
            int result = preparedStatement.executeUpdate();
            if (result > 0) {
                log.info("Update successful");
            } else {
                log.info("No rows affected");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }    

    // This method can be used for SELECT queries
    public static FeederBuilder<Object> jdbcFeeder(String sql) {
        log.info("Creating jdbcFeeder: " + AppConfig.DatabaseConfig.URL + ", " + AppConfig.DatabaseConfig.USERNAME + ", " + AppConfig.DatabaseConfig.PASSWORD + ", " + sql);
        return JdbcDsl.jdbcFeeder(AppConfig.DatabaseConfig.URL, AppConfig.DatabaseConfig.USERNAME, AppConfig.DatabaseConfig.PASSWORD, sql);
    }

    public static FeederBuilder<Object> jdbcFeeder2() {
        log.info("Creating jdbcFeeder dynamically...");
        
        // Fetch the SQL dynamically for each execution
        String sql = SQLQueryProvider.getDataQuery();  
        log.info("Executing SQL: " + sql);
    
        // Create the feeder
        FeederBuilder<Object> feeder = JdbcDsl.jdbcFeeder(AppConfig.DatabaseConfig.URL, AppConfig.DatabaseConfig.USERNAME, AppConfig.DatabaseConfig.PASSWORD, sql);
    
        return feeder;
    } 



}
