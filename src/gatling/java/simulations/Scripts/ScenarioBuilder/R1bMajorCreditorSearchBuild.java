package simulations.Scripts.ScenarioBuilder;

import io.gatling.javaapi.core.ChainBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class R1bMajorCreditorSearchBuild {

    public static ChainBuilder build() {
        return exec(
            http("Get major creditors for business unit")
                .get("/opal-fines-service/major-creditors")
                .queryParam("businessUnit", "#{businessUnit}")
                .check(status().is(200))
                .check(jsonPath("$.refData[0].major_creditor_id").saveAs("major_creditor_id"))
                .check(jsonPath("$.refData[0].name").saveAs("major_creditor_name"))
        )
        .pause(1)
        .exec(
            http("Open major creditor defendant view")
                .get("/fines/account/#{major_creditor_id}/defendant")
                .check(status().is(200))
        );
    }
}