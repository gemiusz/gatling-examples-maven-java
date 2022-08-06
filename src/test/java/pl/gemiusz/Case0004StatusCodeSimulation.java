package pl.gemiusz;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class Case0004StatusCodeSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol =
            http
                    .baseUrl("https://postman-echo.com");

    ScenarioBuilder scn =
            scenario("GeMi_StatusCodeSimulation")
                    .exec(
                            http("GeMi_StatusCodeSimulation")
                                    .get("/status/414")
                                    .check(status().is(414).saveAs("GeMi_Response_Code"))
                    ).exec(session -> {
                        System.out.println("GeMi_Response_Code: " + session.get("GeMi_Response_Code").toString());
                        return session;
                    });

    {
        setUp(scn.injectOpen(atOnceUsers(1)).protocols(httpProtocol));
    }
}
