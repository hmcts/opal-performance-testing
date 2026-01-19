package simulations.Scripts.PerformanceTests;

import simulations.Scripts.Utilities.AppConfig;
import simulations.Scripts.Utilities.AssertionsConfig;
import simulations.Scripts.ScenarioBuilder.CreateAccountScenarioBuild;
import simulations.Scripts.ScenarioBuilder.LoginScenarioBuild;
import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class CreateAccountSimulation extends Simulation {   

    public static AtomicInteger global400ErrorCounter = new AtomicInteger(0);
    private static final String OPAL_LOGIN_TEST = "Opal Manual Account Creation Test";

    @Override
    public void before() {
        System.out.println("Simulation starting...");
        System.out.println("User Count: " + AppConfig.PerformanceConfig.getUserCount());
        System.out.println("Ramp Duration: " + AppConfig.PerformanceConfig.getRampDuration());
    }    
// 2 and 6 simple
// 5 and 15 complex


    public CreateAccountSimulation() {
        HttpProtocolBuilder httpProtocol = configureHttp();
        setUpScenarios(httpProtocol);
    }

    private void setUpScenarios(HttpProtocolBuilder httpProtocol) {
        setUp(
            CreateAccountScenarioBuild.build(OPAL_LOGIN_TEST)
                .injectOpen(
                     rampUsers(AppConfig.PerformanceConfig.getUserCount())
                .during(AppConfig.PerformanceConfig.getRampDuration()))
                .protocols(httpProtocol))           
                .assertions(AssertionsConfig.getCreateAccountAssertions());
    }   

private HttpProtocolBuilder configureHttp() {
    return http
        .proxy(Proxy(AppConfig.ProxyConfig.HOST, AppConfig.ProxyConfig.PORT))
        .baseUrl(AppConfig.UrlConfig.AUTH_URL)
        .inferHtmlResources();        
    } 
}
