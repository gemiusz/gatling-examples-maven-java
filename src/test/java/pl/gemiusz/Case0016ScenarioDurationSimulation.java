package pl.gemiusz;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Session;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class Case0016ScenarioDurationSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol =
            http
                    .baseUrl("https://postman-echo.com");

    Iterator<Map<String, Object>> feederUUID =
            Stream.generate((Supplier<Map<String, Object>>) () -> {
                        String uuidString = UUID.randomUUID().toString();
                        return Collections.singletonMap("uuidString", uuidString);
                    }
            ).iterator();

    ScenarioBuilder scn1 =
            scenario("GeMi_ScenarioDurationSimulation_1")
                    .feed(feederUUID)
                    .exec(session -> {
                        Session session1 = session.set("GeMi_ScenarioDurationSimulation_1_start", System.nanoTime());
                        return session1;
                    })
                    .exec(
                            http("GeMi_ScenarioDurationSimulation_get_1_1")
                                    .get("/get?foo=#{uuidString}")
                                    .check(jmesPath("args.foo").isEL("#{uuidString}"))
                    ).exec(
                            http("GeMi_ScenarioDurationSimulation_get_1_2")
                                    .get("/get?foo=#{uuidString}")
                                    .check(jmesPath("args.foo").isEL("#{uuidString}"))
                    )
                    .pause(Duration.ofSeconds(5))
                    .exec(
                            http("GeMi_ScenarioDurationSimulation_get_1_3")
                                    .get("/get?foo=#{uuidString}")
                                    .check(jmesPath("args.foo").isEL("#{uuidString}"))
                    ).exec(session -> {
                        long howLong = System.nanoTime() - session.getLong("GeMi_ScenarioDurationSimulation_1_start");
                        System.out.println("Scenario GeMi_ScenarioDurationSimulation_1 take: " + howLong + " ns");
                        return session;
                    });

    ScenarioBuilder scn2 =
            scenario("GeMi_ScenarioDurationSimulation_2")
                    .feed(feederUUID)
                    .exec(session -> {
                        Session session1 = session.set("GeMi_ScenarioDurationSimulation_2_start", System.nanoTime());
                        return session1;
                    })
                    .exec(
                            http("GeMi_ScenarioDurationSimulation_get_2_1")
                                    .get("/get?foo=#{uuidString}")
                                    .check(jmesPath("args.foo").isEL("#{uuidString}"))
                    )
                    .pause(Duration.ofSeconds(10))
                    .exec(
                            http("GeMi_ScenarioDurationSimulation_get_2_2")
                                    .get("/get?foo=#{uuidString}")
                                    .check(jmesPath("args.foo").isEL("#{uuidString}"))
                    ).exec(session -> {
                        long howLong = System.nanoTime() - session.getLong("GeMi_ScenarioDurationSimulation_2_start");
                        System.out.println("Scenario GeMi_ScenarioDurationSimulation_2 take: " + howLong + " ns");
                        return session;
                    });

    {
        setUp(scn1.injectOpen(constantUsersPerSec(1).during(Duration.ofSeconds(30))).protocols(httpProtocol),
                scn2.injectOpen(constantUsersPerSec(1).during(Duration.ofSeconds(30))).protocols(httpProtocol));
    }
}
