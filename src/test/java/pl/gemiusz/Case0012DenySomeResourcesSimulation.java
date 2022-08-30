package pl.gemiusz;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class Case0012DenySomeResourcesSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol =
            http
                    .baseUrl("https://github.com")
                    .inferHtmlResources(
                            DenyList(
                                    ".*github\\.githubassets\\.com.*",
                                    ".*avatars\\.githubusercontent\\.com\\/facebook.*"));

    ScenarioBuilder scn =
            scenario("GeMi_DenySomeResourcesSimulation")
                    .exec(
                            http("GeMi_DenySomeResourcesSimulation_get")
                                    .get("/")
                    );

    {
        setUp(scn.injectOpen(atOnceUsers(1)).protocols(httpProtocol));
    }
}
