package simulations.Scripts.Utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppConfig {
    private static final Logger logger = Logger.getLogger(AppConfig.class.getName());
    private static final Properties config = new Properties();
    private static final String CONFIG_FILE = "config/application.properties";
    private static volatile AppConfig instance;

    private AppConfig() {
        loadConfiguration();
    }

    public static AppConfig getInstance() {
        if (instance == null) {
            synchronized (AppConfig.class) {
                if (instance == null) {
                    instance = new AppConfig();
                }
            }
        }
        return instance;
    }

    // ------------------------- Utility Methods -----------------------------

    private void loadConfiguration() {
        try (FileInputStream input = new FileInputStream(CONFIG_FILE)) {
            config.load(input);
            logger.info("Configuration loaded successfully");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading configuration file", e);
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    private static String getConfigProperty(String key, String defaultValue) {
        return Optional.ofNullable(System.getProperty(key))
            .or(() -> Optional.ofNullable(config.getProperty(key)))
            .or(() -> Optional.ofNullable(System.getenv(key)))
            .orElse(defaultValue);
    }

    private static int getConfigPropertyAsInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(getConfigProperty(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            logger.warning("Invalid int for " + key + ", using default " + defaultValue);
            return defaultValue;
        }
    }

    private static long getConfigPropertyAsLong(String key, long defaultValue) {
        try {
            return Long.parseLong(getConfigProperty(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            logger.warning("Invalid long for " + key + ", using default " + defaultValue);
            return defaultValue;
        }
    }

    private static boolean getConfigPropertyAsBoolean(String key, boolean defaultValue) {
        return Boolean.parseBoolean(getConfigProperty(key, String.valueOf(defaultValue)));
    }

    // ------------------------- Enums -----------------------------

    public enum TestType {
        SMOKE("smoke"), NORMAL("normal"), PEAK("peak");

        private final String value;

        TestType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static TestType fromString(String text) {
            for (TestType type : values()) {
                if (type.value.equalsIgnoreCase(text)) return type;
            }
            return NORMAL;
        }
    }
    // ------------------------- URL Configuration -----------------------------
    public static class UrlConfig {
        public static final String BASE_URL = getConfigProperty("url.rrems.base", "test1");
        public static final String AUTH_URL = getConfigProperty("url.auth.base", "test1");
    }

    // ------------------------- Tenant Configuration -----------------------------

    public static class TenantConfig {
        public static final String CLIENT_ID = getConfigProperty("tenant.client.id", "test1");
        public static final String CLIENT_REQUEST_ID = getConfigProperty("tenant.client.request.id", "test1");
        public static final String SCOPE = getConfigProperty("tenant.scope", "test1");
        public static final String REDIRECT_URL = getConfigProperty("tenant.redirect.url", "test1");
        public static final String AAD_TENANT_ID = getConfigProperty("tenant.aad.tenant.id", "test1");
    }

    // ------------------------- Test Configuration -----------------------------

    public static class TestConfig {
        private static final TestType currentTestType =
            TestType.fromString(getConfigProperty("test.type", "normal"));

        public static TestType getCurrentTestType() {
            return currentTestType;
        }

        public static final long TEST_DURATION_MINUTES =
            getConfigPropertyAsLong("test.duration.minutes", 30);
        public static final long TEST_DURATION_SECONDS =
            getConfigPropertyAsLong("test.duration.seconds", 600);
        public static final long RAMP_UP_TIME_SECONDS =
            getConfigPropertyAsLong("test.rank.up.time.seconds", 120);
        public static final long RAMP_DOWN_TIME_SECONDS =
            getConfigPropertyAsLong("test.rank.down.time.seconds", 120);
    }

    // ------------------------- Database Configuration -----------------------------

    public static class DatabaseConfig {
        public static final String URL = getConfigProperty("db.url", "jdbc:postgresql://localhost:5432/test");
        public static final String USERNAME = getConfigProperty("db.username", "pgadmin");
        public static final String PASSWORD = getConfigProperty("db.password", "test");

        public static final boolean IS_FIXED = getConfigPropertyAsBoolean("data.fixed", false);
        public static final boolean DYNAMIC_CASES = getConfigPropertyAsBoolean("cases.dynamic", false);
    }

    // ------------------------- File Configuration -----------------------------

    public static class FileConfig {
        public static final String BASE_DIR = System.getProperty("user.dir");
        private static final String CSV_BASE_PATH = Paths.get(BASE_DIR, "src/gatling/resources").toString();

        public static String getCsvPath(String filename) {
            return Paths.get(CSV_BASE_PATH, filename).toString();
        }        
		

        public static class CsvFiles {
            public static final String USERS_CSV = "Users.csv";
            public static final String CHECKER_USERS_CSV = "CheckersUsers.csv";
            public static final String INPUTTER_USERS_CSV = "InputterUsers.csv";

            public static final String USERS_FILE_PATH = Paths.get(USERS_CSV).toString();  
            public static final String CHECKER_USERS_FILE_PATH = Paths.get(CHECKER_USERS_CSV).toString();    
            public static final String INPUTTER_USERS_FILE_PATH = Paths.get(INPUTTER_USERS_CSV).toString();    

			

        }

        public static class DocumentFiles {
            private static final String[] AVAILABLE_DOCUMENTS = {"SampleDoc2.docx"};

            public static String getRandomDocument() {
                return AVAILABLE_DOCUMENTS[new Random().nextInt(AVAILABLE_DOCUMENTS.length)];
            }
        }

        public static Path getFullPath(String filename) {
            return Paths.get(CSV_BASE_PATH, filename);
        }
    }
 // ------------------------- Performance Configuration -----------------------------
        public static class PerformanceConfig {

																	 
        public static final int INPUTTER_USERS = Integer.parseInt(
            System.getProperty("performance.inputters", "2")
        );

        public static final int CHECKER_USERS = Integer.parseInt(
            System.getProperty("performance.checkers", "2")
        );

        public static final int EXISTING_USERS = Integer.parseInt(
            System.getProperty("performance.existing", "2")
        );

        public static final int RAMP_DURATION_MINUTES = Integer.parseInt(
            System.getProperty("performance.rampup.minutes", "10")
        );

											 
										  
							  
		 

        public static Duration getRampDuration() {
            return Duration.ofMinutes(RAMP_DURATION_MINUTES);
        }

        public static int getSimulationDuration() {
            return 300;
        }
    }


    // ------------------------- Proxy Configuration -----------------------------

    public static class ProxyConfig {
        public static final String HOST = getConfigProperty("proxy.host", "127.0.0.1");
        public static final int PORT = getConfigPropertyAsInt("proxy.port", 8888);
    }

    public static final class TestingConfig {


        // Total accounts to create
        public static final int TOTAL_ACCOUNTS = Integer.parseInt(
            System.getProperty("performance.totalAccounts", "1000")
        );

        // Percent split
        public static final int FIXED_PERCENT = 50;
        public static final int FINE_PERCENT = 40;
        public static final int CONDITIONAL_PERCENT = 10;

        // Derived targets
        public static final int FIXED_TARGET =
            TOTAL_ACCOUNTS * FIXED_PERCENT / 100;

        public static final int FINE_TARGET =
            TOTAL_ACCOUNTS * FINE_PERCENT / 100;

        public static final int CONDITIONAL_TARGET =
            TOTAL_ACCOUNTS * CONDITIONAL_PERCENT / 100;
    }
}