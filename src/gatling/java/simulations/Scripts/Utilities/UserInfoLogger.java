package simulations.Scripts.Utilities;

import io.gatling.javaapi.core.ChainBuilder;

import static io.gatling.javaapi.core.CoreDsl.exec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserInfoLogger {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(UserInfoLogger.class);

    /**
     * Main request logger
     */
    public static ChainBuilder logDetailedErrorMessage(
            String requestName
    ) {

        return exec(session -> {

            String statusCode =
                    session.contains("httpStatus")
                            ? String.valueOf(session.getInt("httpStatus"))
                            : "N/A";

            String responseBody =
                    session.contains("responseBody")
                            ? session.getString("responseBody")
                            : "N/A";

            String errorType =
                    session.contains("Changetosomething2")
                            ? session.getString("Changetosomething2")
                            : "N/A";

            String errorTitle =
                    session.contains("Changetosomething")
                            ? session.getString("Changetosomething")
                            : "N/A";

            String errorStatus =
                    session.contains("errorStatus")
                            ? session.getString("errorStatus")
                            : "N/A";

            String detail =
                    session.contains("getDetail")
                            ? session.getString("getDetail")
                            : "N/A";

            String userName =
                    session.contains("Username")
                            ? session.getString("Username")
                            : "N/A";

            // -------------------------
            // SUCCESS
            // -------------------------
            if (!session.isFailed()) {

                LOGGER.info(
                        "Request '{}' succeeded. User: {}. Status: {}",
                        requestName,
                        userName,
                        statusCode
                );

                return session;
            }

            // -------------------------
            // FAILURE
            // -------------------------
            LOGGER.error(
                    """
                    Request '{}' FAILED

                    Status Code: {}
                    Error Type: {}
                    Error Title: {}
                    Error Status: {}

                    User:
                    detail={}
                    Username={}

                    Response Body:
                    {}
                    """,
                    requestName,
                    statusCode,
                    errorType,
                    errorTitle,
                    errorStatus,
                    detail,
                    userName,
                    responseBody
            );

            return session;
        });
    }
}
