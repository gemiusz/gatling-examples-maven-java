package pl.gemiusz;

import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class Case0003UnzipJsonForFeederSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol =
            http
                    .baseUrl("https://postman-echo.com");

    FeederBuilder<Object> feederUnzip = jsonFile("feeders/tounzip.json.zip").unzip().random();
    FeederBuilder<Object> feederUngzip = jsonFile("feeders/toungzip.json.gz").unzip().random();

    ScenarioBuilder scn =
            scenario("GeMi_UnzipJsonForFeederSimulation")
                    .feed(feederUnzip)
                    .exec(
                            http("GeMi_UnzipJsonForFeederSimulation_feederUnzip_post")
                                    .post("/post")
                                    .body(StringBody("#{foo}"))
                                    .asJson()
                                    .check(
                                            jmesPath("json.fruits[?details.size >= `500`].name").is("[\"Apple\"]")
                                    )
                    )
                    .feed(feederUngzip)
                    .exec(
                            http("GeMi_UnGzipJsonForFeederSimulation_feederUngzip_post")
                                    .post("/post")
                                    .body(StringBody("#{bar}"))
                                    .asJson()
                                    .check(
                                            jmesPath("json.fruits[?details.size >= `500`].name").is("[\"Apple\"]")
                                    )
                    );

    {
        setUp(scn.injectOpen(atOnceUsers(1)).protocols(httpProtocol));
    }
}
