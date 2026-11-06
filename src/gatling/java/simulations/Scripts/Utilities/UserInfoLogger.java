package simulations.Scripts.Utilities;

import io.gatling.javaapi.core.ChainBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.gatling.javaapi.core.CoreDsl.exec;

public class UserInfoLogger {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(UserInfoLogger.class);

    private static boolean isGateway(Integer code) {
        return code != null && (code == 502 || code == 504);
    }

    public static ChainBuilder logSessionStatus(String stepName) {
        return exec(session -> {
            LOGGER.info(
                    "STEP: {} | session.isFailed()={}",
                    stepName,
                    session.isFailed()
            );

            return session;
        });
    }

    public static ChainBuilder logDetailedErrorMessage(String requestName) {
        return exec(session -> {
            Integer statusCode =
                    session.contains("httpStatus")
                            ? session.getInt("httpStatus")
                            : null;

            String responseBody =
                    session.contains("responseBody")
                            ? session.getString("responseBody")
                            : "N/A";

            String errorType =
                    session.contains("errorType")
                            ? session.getString("errorType")
                            : "N/A";

            String errorTitle =
                    session.contains("errorTitle")
                            ? session.getString("errorTitle")
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

            boolean sessionFailed = session.isFailed();
            boolean gatewayError = isGateway(statusCode);

            // This request passes only when its HTTP status is exactly 200.
            boolean requestSucceeded =
                    statusCode != null && statusCode == 200 || statusCode == 201;

            if (AppConfig.LoggingConfig.ENABLE_DIAGNOSTIC_LOGGING) {
                LOGGER.info(
                        """
                        Request '{}'
                        Session Failed: {}
                        Request Successful by HTTP status: {}
                        Gateway Error: {}
                        Status Code: {}
                        """,
                        requestName,
                        sessionFailed,
                        requestSucceeded,
                        gatewayError,
                        statusCode
                );
            }

            if (requestSucceeded) {
                LOGGER.info(
                        "Request '{}' SUCCEEDED | Status Code: {} | User: {}",
                        requestName,
                        statusCode,
                        userName
                );

                return session;
            }

            // Count every status that is not 200, including a missing status.
            int totalErrorCount = AccountCounters.ERROR_COUNT.incrementAndGet();
            int statusCodeCount =
                    AccountCounters.incrementErrorCountForCode(statusCode);

            String statusCodeLabel =
                    statusCode == null
                            ? "NO_HTTP_STATUS"
                            : statusCode.toString();

            if (gatewayError) {
                LOGGER.error(
                        """
                        Total Error Count: {}
                        Count for HTTP status {}: {}
                        Request '{}' FAILED (Gateway Error)
                        Status Code: {}
                        User: {}
                        Response Body:
                        {}
                        """,
                        totalErrorCount,
                        statusCodeLabel,
                        statusCodeCount,
                        requestName,
                        statusCodeLabel,
                        userName,
                        responseBody
                );

                return session;
            }

            LOGGER.error(
                    """
                    Total Error Count: {}
                    Count for HTTP status {}: {}
                    Request '{}' FAILED
                    Status Code: {}
                    Error Type: {}
                    Error Title: {}
                    Error Status: {}
                    User: {}
                    Detail: {}
                    Response Body:
                    {}
                    """,
                    totalErrorCount,
                    statusCodeLabel,
                    statusCodeCount,
                    requestName,
                    statusCodeLabel,
                    errorType,
                    errorTitle,
                    errorStatus,
                    userName,
                    detail,
                    responseBody
            );

            return session;
        });
    }
}