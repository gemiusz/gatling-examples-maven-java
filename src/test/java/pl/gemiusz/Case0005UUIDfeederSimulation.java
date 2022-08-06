package pl.gemiusz;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class Case0005UUIDfeederSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol =
            http
                    .baseUrl("https://postman-echo.com");

    Iterator<Map<String, Object>> feederUUID =
            Stream.generate((Supplier<Map<String, Object>>) () -> {
                        String uuidString = UUID.randomUUID().toString();
                        return Collections.singletonMap("uuidString", uuidString);
                    }
            ).iterator();

    ScenarioBuilder scn =
            scenario("GeMi_UUIDfeederSimulation")
                    .feed(feederUUID)
                    .exec(
                            http("GeMi_UUIDfeederSimulation")
                                    .get("/get?foo=#{uuidString}")
                                    .check(jmesPath("args.foo").isEL("#{uuidString}"))
                    ).exec(session -> {
                        System.out.println("GeMi_feederUUID_uuidString: " + session.get("uuidString"));
                        return session;
                    });

    {
        setUp(scn.injectOpen(atOnceUsers(1)).protocols(httpProtocol));
    }
}
