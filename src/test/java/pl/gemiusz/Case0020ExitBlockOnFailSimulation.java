package pl.gemiusz;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class Case0020ExitBlockOnFailSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol =
            http
                    .baseUrl("https://postman-echo.com");

    ScenarioBuilder scn =
            scenario("GeMi_ExitBlockOnFailSimulation")
                    .exitBlockOnFail().on(
                            exec(
                                    http("GeMi_ExitBlockOnFailSimulatio_get_200")
                                            .get("/status/200")
                                            .check(status().is(200))
                            ).exec(
                                    http("GeMi_ExitBlockOnFailSimulatio_get_200_or_exit")
                                            .get("/status/20#{randomInt(0,2)}")
                                            .check(status().is(200))
                            ).exec(
                                    http("GeMi_ExitBlockOnFailSimulatio_get_400")
                                            .get("/status/400")
                                            .check(status().is(400))
                            )
                    );

    {
        setUp(scn.injectOpen(constantUsersPerSec(1).during(Duration.ofSeconds(10))).protocols(httpProtocol));
    }
}
