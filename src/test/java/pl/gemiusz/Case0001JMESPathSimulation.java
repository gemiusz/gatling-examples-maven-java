package pl.gemiusz;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.util.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class Case0001JMESPathSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol =
            http
                    .baseUrl("https://postman-echo.com");
    ScenarioBuilder scn =
            scenario("GeMi_JMESPathSimulation")
                    .exec(
                            http("GeMi_JMESPathSimulation")
                                    .post("/post")
                                    .body(StringBody("{\"status\":\"GOOD\",\"fruits\":[{\"name\":\"Apple\",\"details\":{\"size\":500}},{\"name\":\"Cherry\",\"details\":{\"size\":100}}]}"))
                                    .asJson()
                                    .check(
                                            jmesPath("json.fruits[?details.size >= `500`]")
                                                    .ofList()
                                                    .is(
                                                            List.of(
                                                                    Map.of("name","Apple",
                                                                            "details",Map.of(
                                                                                        "size",500
                                                                                        )
                                                                    )
                                                            )

                                                    )
                                                    .saveAs("GeMi_fruitObj")
                                    )
                                    .check(status().saveAs("GeMi_Response_Code"))
                    ).exec(session -> {
                        System.out.println("GeMi_fruitObj: " + session.get("GeMi_fruitObj").toString());
                        System.out.println("GeMi_Response_Code: " + session.get("GeMi_Response_Code").toString());
                        return session;
                    });

    {
        setUp(scn.injectOpen(atOnceUsers(1)).protocols(httpProtocol));
    }
}
