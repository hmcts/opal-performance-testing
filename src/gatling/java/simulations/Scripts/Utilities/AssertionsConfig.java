package simulations.Scripts.Utilities;

import static io.gatling.javaapi.core.CoreDsl.*;

import io.gatling.javaapi.core.Assertion;

/**
 * Configuration class for managing Gatling assertions.
 * Centralizes all performance assertion definitions to keep simulations clean and maintainable.
 */
public class AssertionsConfig {

    /**
     * Gets the default assertions for Create Account scenario testing.
     * @return Array of assertions to be used in setUp()
     */
    public static Assertion[] getCreateAccountAssertions() {
        return new Assertion[]{
            global().responseTime().max().lt(60000),
            details(
                "OPAL Login Requests",
                "OPAL Create Manual Account",
                "OPAL - Opal-fines-service - Business-units"
            ).responseTime().percentile(90).lt(2000),
            details(
                "OPAL Login Requests",
                "OPAL Create Manual Account",
                "OPAL - Opal-fines-service - Draft-accounts"
            ).responseTime().max().lt(30000)
        };
    }

    
    // Gets assertions for Login scenario testing.
    public static Assertion[] getMac01Assertions() {
        return new Assertion[]{

            // Default rule: ALL requests < 2s (p95)
            global()
                .responseTime()
                .percentile3()
                .lt(2000),

            // Exception 1: Login request < 5s
            details(
                "Inputter Workflow",
                "OPAL Login",
                "OPAL - Sso - Login - /"
            )
            .responseTime()
            .percentile3()
            .lt(5000),

            // Exception 2: Credential type lookup < 5s
            details(
                "Inputter Workflow",
                "OPAL Login",
                "OPAL - Common - GetCredentialType"
            )
            .responseTime()
            .percentile3()
            .lt(5000),

            // Optional but recommended
            global().failedRequests().count().is(0L)
        };
    }

    /**
     * Gets assertions for Draft Account creation.
     * @return Array of assertions to be used in setUp()
     */
    public static Assertion[] getDraftAccountAssertions() {
        return new Assertion[]{
            global().responseTime().percentile(95).lt(5000),
            global().responseTime().max().lt(30000)
        };
    }

    /**
     * Gets assertions for Account Approval workflow.
     * @return Array of assertions to be used in setUp()
     */
    public static Assertion[] getApprovalAssertions() {
        return new Assertion[]{
            global().responseTime().max().lt(15000),
            details("OPAL - Approval-service - Accounts").responseTime().percentile(90).lt(3000)
        };
    }

    
}
