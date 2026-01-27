package simulations.Scripts.PerformanceTests;

import simulations.Scripts.Utilities.AppConfig;
import simulations.Scripts.Utilities.AssertionsConfig;
import simulations.Scripts.ScenarioBuilder.CheckerUsersScenarioBuild;
import simulations.Scripts.ScenarioBuilder.ExistingUsersScenarioBuild;
import simulations.Scripts.ScenarioBuilder.InputterUsersScenarioBuild;
import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.util.concurrent.atomic.AtomicInteger;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class MAC_01aSimulation extends Simulation {

    public static AtomicInteger global400ErrorCounter = new AtomicInteger(0);
    private static final String MAC_01A_TEST = "MAC 01a Test";

    @Override
    public void before() {
        System.out.println("Simulation starting...");
        System.out.println("User Count: " + AppConfig.PerformanceConfig.getUserCount());
        System.out.println("Ramp Duration: " + AppConfig.PerformanceConfig.getRampDuration());
    }

    public MAC_01aSimulation() {
        HttpProtocolBuilder httpProtocol = configureHttp();
        // Use the total simulation duration to keep users alive
        setUp(
            // Inputter users
            InputterUsersScenarioBuild.build(MAC_01A_TEST + " - Inputter")
                .injectOpen(
                    rampUsers(AppConfig.PerformanceConfig.getUserCount())
                        .during(AppConfig.PerformanceConfig.getRampDuration())
                ),

            // Checker users
            CheckerUsersScenarioBuild.build(MAC_01A_TEST + " - Checker")
                .injectOpen(
                    rampUsers(AppConfig.PerformanceConfig.getUserCount())
                        .during(AppConfig.PerformanceConfig.getRampDuration())
                ),

            // Existing users
            ExistingUsersScenarioBuild.build(MAC_01A_TEST + " - Exists User", AppConfig.PerformanceConfig.getSimulationDuration())
                .injectOpen(
                    rampUsers(AppConfig.PerformanceConfig.getUserCount())
                        .during(AppConfig.PerformanceConfig.getRampDuration())
                )
        )
        .protocols(httpProtocol)
        .assertions(AssertionsConfig.getMac01Assertions());
    }

    private HttpProtocolBuilder configureHttp() {
        return http
            .proxy(Proxy(AppConfig.ProxyConfig.HOST, AppConfig.ProxyConfig.PORT))
            .baseUrl(AppConfig.UrlConfig.AUTH_URL)
            .disableCaching()
            .acceptEncodingHeader("gzip, deflate, br")
            .acceptLanguageHeader("en-US,en;q=0.9");
    }
}
