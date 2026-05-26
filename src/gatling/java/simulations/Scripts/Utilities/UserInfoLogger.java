package simulations.Scripts.Utilities;

import io.gatling.javaapi.core.ChainBuilder;

import static io.gatling.javaapi.core.CoreDsl.exec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserInfoLogger {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(UserInfoLogger.class);

    /**
     * Overload with no status key required
     */
    public static ChainBuilder logDetailedErrorMessage(String requestName) {
    return logDetailedErrorMessage(requestName, null);
    }

    /**
     * Main logging method
     */
    public static ChainBuilder logDetailedErrorMessage(
            String requestName,
            String statusKey
    ) {

        return exec(session -> {

            int reqStatus = -1;

            if (statusKey != null
                    && !statusKey.isBlank()
                    && session.contains(statusKey)) {

                reqStatus = session.getInt(statusKey);
            }

            boolean isSuccess = reqStatus == 200 || reqStatus == 302;

            String userName =
                    session.contains("Username")
                            ? session.getString("Username")
                            : "N/A";

            String email =
                    session.contains("Email")
                            ? session.getString("Email")
                            : "N/A";

            // ✅ NEW: capture API error detail if it exists
            String detail =
                    session.contains("getDetail")
                            ? session.getString("getDetail")
                            : "N/A";

            if (isSuccess) {

                LOGGER.info(
                        "Request '{}' was successful. User: {}. Email: {}. Status: {}",
                        requestName,
                        userName,
                        email,
                        reqStatus == -1 ? "N/A" : reqStatus
                );

            } else {

                LOGGER.error(
                        "Request '{}' FAILED. User: {}. Email: {}. Status: {}. Detail: {}",
                        requestName,
                        userName,
                        email,
                        reqStatus == -1 ? "N/A" : reqStatus,
                        detail
                );
            }

            return session;
        });
    }

    /**
     * Regex failure logger
     */
    public static ChainBuilder logDetailedErrorMessage(
            String requestName,
            String regexName,
            String expectedPattern
    ) {

        return exec(session -> {

            String email =
                    session.contains("Email")
                            ? session.getString("Email")
                            : "N/A";

            String password =
                    session.contains("Password")
                            ? session.getString("Password")
                            : "N/A";

            String userName =
                    session.contains("user_name")
                            ? session.getString("user_name")
                            : "N/A";

            boolean regexFailed =
                    session.contains("regexFailed")
                            && session.getBoolean("regexFailed");

            if (regexFailed) {

                String errorMessage = String.format(
                        "Request '%s' encountered a regex issue for user: " +
                        "Email=%s, Password=%s, User Name=%s. " +
                        "Failed to match regex '%s' with pattern '%s'.",
                        requestName,
                        email,
                        password,
                        userName,
                        regexName,
                        expectedPattern
                );

                LOGGER.error(errorMessage);
            }

            return session;
        });
    }
}