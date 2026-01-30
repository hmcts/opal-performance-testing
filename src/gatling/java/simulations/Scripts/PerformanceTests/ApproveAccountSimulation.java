package simulations.Scripts.PerformanceTests;

import simulations.Scripts.Utilities.AppConfig;
import simulations.Scripts.ScenarioBuilder.ApproveAccountScenarioBuild;
import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import java.util.concurrent.atomic.AtomicInteger;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class ApproveAccountSimulation extends Simulation {   

    public static AtomicInteger global400ErrorCounter = new AtomicInteger(0);
    private static final String OPAL_LOGIN_TEST = "Opal Manual Account Creation Test";

    @Override
    public void before() {
        System.out.println("Simulation starting...");
        System.out.println("User Count: " + AppConfig.PerformanceConfig.CHECKER_USERS);
        System.out.println("Ramp Duration: " + AppConfig.PerformanceConfig.getRampDuration());
    }    
// 2 and 6 simple
// 5 and 15 complex


    public ApproveAccountSimulation() {
        HttpProtocolBuilder httpProtocol = configureHttp();
        setUpScenarios(httpProtocol);
    }

    private void setUpScenarios(HttpProtocolBuilder httpProtocol) {
        setUp(
            ApproveAccountScenarioBuild.build(OPAL_LOGIN_TEST)
                .injectOpen(
                     rampUsers(AppConfig.PerformanceConfig.CHECKER_USERS)
                .during(AppConfig.PerformanceConfig.getRampDuration()))
                .protocols(httpProtocol))           
                .assertions(global().responseTime().max().lt(60000),              
                    details(
                        "OPAL Login Requests",
                        "OPAL Approve Account",
                        "Opal - Opal-fines-service - Business-units"
                    ).responseTime().percentile(90).lt(2000),              
                    details(
                      "OPAL Login Requests",
                        "OPAL Approve Account",
                        "OPAL - Opal-fines-service - Draft-accounts"                    
                    ).responseTime().max().lt(30000)                
        );
    }   

private HttpProtocolBuilder configureHttp() {
    return http
        .proxy(Proxy(AppConfig.ProxyConfig.HOST, AppConfig.ProxyConfig.PORT))
        .baseUrl(AppConfig.UrlConfig.AUTH_URL)
        .inferHtmlResources();        
    } 
}
