package simulations.Scripts.PerformanceTests;

import simulations.Scripts.Utilities.AppConfig;
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
                .assertions(global().responseTime().max().lt(60000),              
                    details(
                        "OPAL Login Requests",
                        "OPAL Login",
                        "Sso - Login - /"
                    ).responseTime().max().lt(30000)  ,              
                    details(
                      "OPAL Login Requests",
                        "OPAL Login",
                        "Common - GetCredentialType"                    
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
