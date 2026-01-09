package simulations.Scripts.Scenario.OpalLogin;

import simulations.Scripts.Headers.Headers;
import simulations.Scripts.Utilities.AppConfig;
import simulations.Scripts.Utilities.Feeders;
import simulations.Scripts.Utilities.UserInfoLogger;
import io.gatling.javaapi.core.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import simulations.Scripts.RequestBodyBuilder.RequestBodyBuilder;

public final class CreateAccountScenario {

    private CreateAccountScenario() {}
    private static final Logger logger = LoggerFactory.getLogger("OPAL");

    public static ChainBuilder CreateAccountRequest() {

        return group("OPAL Create Manual Account").on(
                exec(http("OPAL - Sso - Authenticated")
                        .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                        .headers(Headers.getHeaders(11))
                        .check(status().is(200))                                         
                )
                .exec(UserInfoLogger.logDetailedErrorMessage("OPAL - Sso - Authenticated"))
                
                .exec(http("OPAL - Sso - Authenticated")
                        .get(AppConfig.UrlConfig.BASE_URL + "/sso/authenticated")
                        .headers(Headers.getHeaders(11))
                        .check(status().is(200))                                         
                )
                .exitHereIfFailed() 
                .exec(
                    http("request_2")
                    .get(AppConfig.UrlConfig.BASE_URL + "/opal-fines-service/business-units?permission=CREATE_MANAGE_DRAFT_ACCOUNTS")
                    .headers(Headers.getHeaders(12))
                    .check(status().is(200)) 
                )               
                .exitHereIfFailed()                       
           
        );            
    }
}
                     



    