package pl.gemiusz;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Session;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.util.Map;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class Case0010JsonEditVariableSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol =
            http
                    .baseUrl("https://postman-echo.com");

    ScenarioBuilder scn =
            scenario("GeMi_JsonEditVariableSimulation")
                    .exec(
                            http("GeMi_JsonEditVariableSimulation_post")
                                    .post("/post")
                                    .body(StringBody("{\"status\":\"GOOD\",\"fruit\":\"Apple\"}"))
                                    .asJson()
                                    .check(
                                            jmesPath("json")
                                                    .ofObject()
                                                    .is(
                                                            Map.of("status", "GOOD",
                                                                    "fruit", "Apple"
                                                            )
                                                    )
                                                    .saveAs("GeMi_Obj")
                                    )
                    ).exec(session -> {
                        System.out.println("GeMi_Obj: " + session.get("GeMi_Obj").toString());
                        Map<String, Object> gemiObj = session.get("GeMi_Obj");
                        //changing value from GOOD to BAD
                        gemiObj.put("status", "BAD");
                        Session sessionGeMiObj = session.set("changed_GeMi_Obj", gemiObj);
                        return sessionGeMiObj;
                    }).exec(
                            http("GeMi_JsonEditVariableSimulation_post_changed")
                                    .post("/post")
                                    .body(StringBody("#{changed_GeMi_Obj.jsonStringify()}"))
                                    .asJson()
                                    .check(
                                            jmesPath("json")
                                                    .ofObject()
                                                    .is(
                                                            Map.of("status", "BAD",
                                                                    "fruit", "Apple"
                                                            )
                                                    )
                                                    .saveAs("GeMi_Obj_aftter_change")
                                    )
                    ).exec(session -> {
                        System.out.println("GeMi_Obj_aftter_change: " + session.get("GeMi_Obj_aftter_change").toString());
                        return session;
                    });

    {
        setUp(scn.injectOpen(atOnceUsers(1)).protocols(httpProtocol));
    }
}
