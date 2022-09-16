package pl.gemiusz;

import io.gatling.javaapi.core.OpenInjectionStep;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;
import java.util.stream.Stream;

import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class Case0014Loop5times1RPSand3sPauseSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol =
            http
                    .baseUrl("https://postman-echo.com");

    ScenarioBuilder scn =
            scenario("GeMi_Loop5times1RPSand3sPauseSimulation")
                    .exec(
                            http("GeMi_Loop5times1RPSand3sPauseSimulation_get")
                                    .get("/status/200")
                                    .check(status().is(200).saveAs("GeMi_Status_Code"))
                    );

    {
        setUp(scn.injectOpen(
                Stream.generate(
                        () -> new OpenInjectionStep[]{
                                OpenInjectionStep.atOnceUsers(1),
                                OpenInjectionStep.nothingFor(Duration.ofSeconds(3))
                        }
        ).limit(5).flatMap(Stream::of).toArray(OpenInjectionStep[]::new)
        )
                .protocols(httpProtocol));
    }
}
