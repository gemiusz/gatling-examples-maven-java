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

public class Case0015UUIDfeederTwoRecordsAtTheSameTimeSimulation extends Simulation {

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
            scenario("GeMi_UUIDfeederTwoRecordsAtTheSameTimeSimulation")
                    .feed(feederUUID, 2)
                    .exec(
                            http("GeMi_UUIDfeederTwoRecordsAtTheSameTimeSimulation_get")
                                    .get("/get?foo=#{uuidString(0)}&bar=#{uuidString(1)}")
                                    .check(jmesPath("args.foo").isEL("#{uuidString(0)}"))
                                    .check(jmesPath("args.bar").isEL("#{uuidString(1)}"))
                    ).exec(session -> {
                        Object[] uuidStringArray = session.get("uuidString");
                        System.out.println("GeMi_feederUUID_uuidString(0): " + uuidStringArray[0]);
                        System.out.println("GeMi_feederUUID_uuidString(1): " + uuidStringArray[1]);
                        return session;
                    });

    {
        setUp(scn.injectOpen(atOnceUsers(1)).protocols(httpProtocol));
    }
}
