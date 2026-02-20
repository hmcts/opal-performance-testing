package simulations.Scripts.Utilities;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.sql.Connection;

import io.gatling.javaapi.core.CoreDsl;
import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.jdbc.JdbcDsl;
import io.gatling.javaapi.core.CheckBuilder;
import java.util.*;
import static io.gatling.javaapi.core.CoreDsl.*;


public class Feeders {

    public static final FeederBuilder<String> Users;
    public static final FeederBuilder<String> CheckerUsers;
    public static final FeederBuilder<String> InputterUsers;

    private static final AtomicInteger COUNTER;
    private static final Logger log = Logger.getLogger(Feeders.class.getName());

   static { 
    try {            
        Users = CoreDsl.csv(AppConfig.FileConfig.CsvFiles.USERS_CSV).circular();
        CheckerUsers = CoreDsl.csv(AppConfig.FileConfig.CsvFiles.CHECKER_USERS_CSV).circular();
        InputterUsers = CoreDsl.csv(AppConfig.FileConfig.CsvFiles.INPUTTER_USERS_CSV).circular();        
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
        return CoreDsl.jsonPath("$.refData[*].business_unit_id").findRandom().saveAs("getBusinessUnitId");
    }
    public static CheckBuilder.Final saveCourtId() {
        return CoreDsl.jsonPath("$.refData[*].court_id").findRandom().saveAs("getCourtId");
    }
    public static CheckBuilder.Final saveProsecutorId() {
        return CoreDsl.jsonPath("$.ref_data[*].prosecutor_id").findRandom().saveAs("getProsecutorId");
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
