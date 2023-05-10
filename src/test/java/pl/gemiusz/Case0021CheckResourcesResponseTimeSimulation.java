package pl.gemiusz;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class Case0021CheckResourcesResponseTimeSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol =
            http
                    .baseUrl("https://postman-echo.com");


    ScenarioBuilder scn =
            scenario("GeMi_CheckResourcesResponseTimeSimulation")
                    .exec(
                            http("GeMi_CheckResourcesResponseTimeSimulation_get")
                                    .get("/get?foo=01234")
                                    .check(jmesPath("args.foo").isEL("01234"))
                                    .resources(
                                            http("GeMi_CheckResourcesResponseTimeSimulation_async_0")
                                                    .get("/get?foo=0")
                                                    .check(jmesPath("args.foo").isEL("0"))
                                                    .check(responseTimeInMillis().lte(630)),
                                            http("GeMi_CheckResourcesResponseTimeSimulation_async_1")
                                                    .get("/get?foo=1")
                                                    .check(jmesPath("args.foo").isEL("1"))
                                                    .check(responseTimeInMillis().lte(630)),
                                            http("GeMi_CheckResourcesResponseTimeSimulation_async_2")
                                                    .get("/get?foo=2")
                                                    .check(jmesPath("args.foo").isEL("2"))
                                                    .check(responseTimeInMillis().lte(630)),
                                            http("GeMi_CheckResourcesResponseTimeSimulation_async_3")
                                                    .get("/get?foo=3")
                                                    .check(jmesPath("args.foo").isEL("3"))
                                                    .check(responseTimeInMillis().lte(630)),
                                            http("GeMi_CheckResourcesResponseTimeSimulation_async_4")
                                                    .get("/get?foo=4")
                                                    .check(jmesPath("args.foo").isEL("4"))
                                                    .check(responseTimeInMillis().lte(630))
                                    )
                    );

    {
        setUp(scn.injectOpen(constantUsersPerSec(1).during(Duration.ofSeconds(30))).protocols(httpProtocol));
    }
}
