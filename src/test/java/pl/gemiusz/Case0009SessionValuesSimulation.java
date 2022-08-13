package pl.gemiusz;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Session;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class Case0009SessionValuesSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol =
            http
                    .baseUrl("https://postman-echo.com");

    ScenarioBuilder scn =
            scenario("GeMi_SessionValuesSimulation")
                    .exec(
                            http("GeMi_SessionValuesSimulation_get_first")
                                    .get("/status/414")
                                    .check(status().is(414).saveAs("GeMi_Response_Code"))
                    ).exec(session -> {
                        System.out.println("GeMi_Response_Code: " + session.get("GeMi_Response_Code").toString());
                        Session session1 = session.set("GeMi_Response_Code_1", session.get("GeMi_Response_Code").toString() + "_1");
                        return session1;
                    }).exec(session -> {
                        System.out.println("GeMi_Response_Code_1: " + session.get("GeMi_Response_Code_1").toString());
                        Session session2 = session.set("GeMi_Response_Code_2", session.get("GeMi_Response_Code_1").toString() + "_2");
                        return session2;
                    }).exec(session -> {
                        System.out.println("GeMi_Response_Code_2: " + session.get("GeMi_Response_Code_2").toString());
                        return session;
                    }).exec(
                            http("GeMi_SessionValuesSimulation_get_later")
                                    .get("/get?foo=#{GeMi_Response_Code_2}")
                                    .check(jmesPath("args.foo").is("414_1_2")));

    {
        setUp(scn.injectOpen(atOnceUsers(1)).protocols(httpProtocol));
    }
}
