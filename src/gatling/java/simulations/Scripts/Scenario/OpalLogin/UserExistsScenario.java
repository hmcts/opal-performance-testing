package simulations.Scripts.Scenario.OpalLogin;

import simulations.Scripts.Headers.Headers;
import simulations.Scripts.Utilities.AppConfig;
import simulations.Scripts.Utilities.UserInfoLogger;
import io.gatling.javaapi.core.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
                http("OPAL - Opal-user-service - Users - 0 - State")
                .get(AppConfig.UrlConfig.BASE_URL + "/opal-user-service/users/0/state")
                .headers(Headers.getHeaders(12))
            )
            .exec(UserInfoLogger.logDetailedErrorMessage("OPAL - Opal-user-service - Users - 0 - State"))

            .exec(
                http("OPAL - Sso - Authenticated")
                .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                .headers(Headers.getHeaders(11))
                .check(status().is(200))
            )
            .exitHereIfFailed()

            .exec(
                http("OPAL - Opal-user-service - Users - 0 - State")
                .get(AppConfig.UrlConfig.BASE_URL + "/opal-user-service/users/0/state")
                .headers(Headers.getHeaders(12))
            )
        );
    }
}
