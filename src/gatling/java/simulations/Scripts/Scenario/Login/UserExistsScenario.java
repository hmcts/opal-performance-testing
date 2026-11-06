package simulations.Scripts.Scenario.Login;

import simulations.Scripts.Headers.Headers;
import simulations.Scripts.Utilities.AppConfig;
import simulations.Scripts.Utilities.Feeders;
import simulations.Scripts.Utilities.UserInfoLogger;
import io.gatling.javaapi.core.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public final class UserExistsScenario {

    private UserExistsScenario() {}

    // Loop every 110 seconds
    public static ChainBuilder UserExistsRequestLoop() {
        return asLongAs(session -> true).on(
            exec(UserExistsRequest())
            .pause(10) // pause 110 seconds
        );
    }

    public static ChainBuilder UserExistsRequest() {
        return group("User Refresh - Keep Alive").on(
            exec(
                http("OPAL - API - Users-state")
                .get(AppConfig.UrlConfig.BASE_URL + "/api/user-state")
                .headers(Headers.getHeaders(12))
            )

                .exec(
                    http("OPAL - Sso - Authenticated")
                        .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                        .headers(Headers.getHeaders(11))
                        .check(status().is(200))
                        .check(Feeders.saveErrorDetails())
                )
                .exec(UserInfoLogger.logDetailedErrorMessage("OPAL - Sso - Authenticated"))
                .exitHereIfFailed() 

            .exec(
                http("OPAL - API - Users-state")
                .get(AppConfig.UrlConfig.BASE_URL + "/api/user-state")
                .headers(Headers.getHeaders(12))
            )
        );
    }
}
