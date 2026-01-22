package simulations.Scripts.PerformanceTests;

import simulations.Scripts.Utilities.AppConfig;
import simulations.Scripts.ScenarioBuilder.UserExistsScenarioBuild;
import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class UserExistsSimulation extends Simulation {   

    @Override
    public void before() {
        System.out.println("Simulation starting...");
        System.out.println("User Count: " + AppConfig.PerformanceConfig.getUserCount());
        System.out.println("Ramp Duration: " + AppConfig.PerformanceConfig.getRampDuration());
    }    

    public UserExistsSimulation() {
        HttpProtocolBuilder httpProtocol = configureHttp();
        setUpScenarios(httpProtocol);
    }

    private void setUpScenarios(HttpProtocolBuilder httpProtocol) {

        // Use the total simulation duration to keep users alive
        int totalSimulationSeconds = AppConfig.PerformanceConfig.getSimulationDuration();

        setUp(
            UserExistsScenarioBuild.build("User Exists Test", totalSimulationSeconds)
                .injectOpen(
                    rampUsers(AppConfig.PerformanceConfig.getUserCount())
                    .during(AppConfig.PerformanceConfig.getRampDuration())
                )
                .protocols(httpProtocol)
        )
        .assertions(
            global().responseTime().max().lt(60000)
        );
    }   

    private HttpProtocolBuilder configureHttp() {
        return http
            .proxy(Proxy(AppConfig.ProxyConfig.HOST, AppConfig.ProxyConfig.PORT))
            .baseUrl(AppConfig.UrlConfig.AUTH_URL)
            .inferHtmlResources();        
    } 
}
