package pl.gemiusz;

import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class Case0025JSONfeederRandomSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol =
            http
                    .baseUrl("https://postman-echo.com");

    FeederBuilder<Object> feederJSON = jsonFile("feeders\\dzejson.json").random();

    ScenarioBuilder scn =
            scenario("GeMi_JSONfeederRandomSimulation")
                    .feed(feederJSON)
                    .exec(
                            http("GeMi_JSONfeederRandomSimulation_get_username")
                                    .get("/get?foo=#{username}")
                                    .check(jmesPath("args.foo").saveAs("usernameRsp"))
                    ).exec(session -> {
                        System.out.println("GeMi_usernameRsp: " + session.get("usernameRsp"));
                        return session;
                    })
                    .exec(
                            http("GeMi_JSONfeederRandomSimulation_get_random_patientID")
                                    .get("/get?foo=#{patients.random().patientID}")
                                    .check(jmesPath("args.foo").saveAs("patientIdRsp"))
                    ).exec(session -> {
                        System.out.println("GeMi_patientIdRsp: " + session.get("patientIdRsp"));
                        return session;
                    });

    {
        setUp(scn.injectOpen(atOnceUsers(1)).protocols(httpProtocol));
    }
}
