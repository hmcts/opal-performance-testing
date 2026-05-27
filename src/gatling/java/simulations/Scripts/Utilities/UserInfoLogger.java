package simulations.Scripts.Utilities;

import io.gatling.javaapi.core.ChainBuilder;

import static io.gatling.javaapi.core.CoreDsl.exec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserInfoLogger {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(UserInfoLogger.class);

    /**
     * Logs request outcome using Gatling session failure state
     */
    public static ChainBuilder logDetailedErrorMessage(
            String requestName
    ) {

        return exec(session -> {

            String userName =
                    session.contains("Username")
                            ? session.getString("Username")
                            : "N/A";

            String detail =
                    session.contains("getDetail")
                            ? session.getString("getDetail")
                            : "N/A";

            if (session.isFailed()) {

                LOGGER.error(
                        "Request '{}' FAILED. User: {}. Detail: {}",
                        requestName,
                        userName,
                        detail
                );

            } else {

                LOGGER.info(
                        "Request '{}' was successful. User: {}",
                        requestName,
                        userName
                );
            }

            return session;
        });
    }
}