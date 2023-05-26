package pl.gemiusz;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

/**
 * HOW TO RUN:
 * mvnw gatling:test -Dgatling.simulationClass=pl.gemiusz.Case0006CommandLineParametersSimulation -Dfoo=10 -Dbar=GeMi -Dpercentile3responseTime=15
 */
public class Case0006CommandLineParametersSimulation extends Simulation {

    int foo = Integer.getInteger("foo", 1);
    String bar = System.getProperty("bar");
    int percentile3responseTime = Integer.getInteger("percentile3responseTime", 2000);

    HttpProtocolBuilder httpProtocol =
            http
                    .baseUrl("https://postman-echo.com");

    ScenarioBuilder scn =
            scenario("GeMi_CommandLineParametersSimulation")
                    .exec(
                            http("GeMi_CommandLineParametersSimulation_get_1")
                                    .get("/get?foo=" + foo + "&bar=" + bar)
                                    .check(jmesPath("args.foo").is(String.valueOf(foo)))
                                    .check(jmesPath("args.bar").is(String.valueOf(bar)))
                    ).exec(
                            http("GeMi_CommandLineParametersSimulation_get_2")
                                    .get("/get?foo=" + foo + "&bar=" + bar)
                                    .check(jmesPath("args.foo").is(String.valueOf(foo)))
                                    .check(jmesPath("args.bar").is(String.valueOf(bar)))
                    );

    {
        setUp(scn.injectOpen(atOnceUsers(1)).protocols(httpProtocol)).assertions(
                        global()
                                .successfulRequests()
                                .percent()
                                .is(100.0)
                )
                .assertions(
                        details("GeMi_CommandLineParametersSimulation_get_1")
                                .responseTime()
                                .percentile3()
                                .lt(percentile3responseTime),
                        details("GeMi_CommandLineParametersSimulation_get_2")
                                .responseTime()
                                .percentile3()
                                .lt(percentile3responseTime));

    }
}
