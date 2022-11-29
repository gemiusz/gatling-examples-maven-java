package pl.gemiusz;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

/**
 * HOW TO RUN:
 * mvnw gatling:test -Dgatling.simulationClass=pl.gemiusz.Case0019WhenStatusCode400ThenFailSimulation
 */
public class Case0019WhenStatusCode400ThenFailSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol =
            http
                    .baseUrl("https://postman-echo.com");

    ScenarioBuilder scn =
            scenario("GeMi_WhenStatusCode400ThenFailSimulation")
                    .exec(
                            http("GeMi_WhenStatusCode400ThenFailSimulation_get")
                                    .get("/status/400")
                                    .check(status().not(400))
                    );

    {
        setUp(scn.injectOpen(atOnceUsers(1)).protocols(httpProtocol))
                .assertions(
                        global().failedRequests().count().is(0L)
                );
    }
}
