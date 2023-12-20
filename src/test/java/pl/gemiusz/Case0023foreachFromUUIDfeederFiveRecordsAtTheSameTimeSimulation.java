package pl.gemiusz;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Session;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class Case0023foreachFromUUIDfeederFiveRecordsAtTheSameTimeSimulation extends Simulation {

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
            scenario("GeMi_foreachFromUUIDfeederFiveRecordsAtTheSameTimeSimulation")
                    .feed(feederUUID, 5)
                    .exec(session -> {
                        ArrayList<String> uuidStringArrayList = session.get("uuidString");
                        Session session1 = session.set("uuidStringArrayList", uuidStringArrayList);
                        return session1;
                    })
                    .foreach("#{uuidStringArrayList}", "uuidStringEl", "counter").on(
                            exec(session -> {
                                System.out.println("counter: " + session.getString("counter"));
                                System.out.println("uuidStringEl: " + session.getString("uuidStringEl"));
                                return session;
                            })
                                    .exec(
                                            http("GeMi_UUIDfeederSimulation_get")
                                                    .get("/get?foo=#{uuidStringEl}")
                                                    .check(jmesPath("args.foo").isEL("#{uuidStringEl}"))
                                    )
                    );

    {
        setUp(scn.injectOpen(atOnceUsers(1)).protocols(httpProtocol));
    }
}
