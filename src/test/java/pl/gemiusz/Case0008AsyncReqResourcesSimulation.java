package pl.gemiusz;

import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import io.netty.util.internal.ThreadLocalRandom;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class Case0008AsyncReqResourcesSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol =
            http
                    .baseUrl("https://postman-echo.com");

    Iterator<Map<String, Object>> feederInt =
            Stream.generate((Supplier<Map<String, Object>>) () -> {
                        int min = 3;
                        int max = 6;
                        int randomInt = ThreadLocalRandom.current().nextInt(min, max);
                        return Collections.singletonMap("randomInt", randomInt);
                    }
            ).iterator();

    ScenarioBuilder scn =
            scenario("GeMi_AsyncReqResourcesSimulation")
                    .feed(feederInt)
                    .doSwitch("#{randomInt}").on(
                            Choice.withKey(3,
                                    exec(
                                            http("GeMi_AsyncReqResourcesSimulation_get")
                                                    .get("/get?foo=#{randomInt}")
                                                    .check(jmesPath("args.foo").isEL("#{randomInt}"))
                                                    .resources(
                                                            http("GeMi_AsyncReqResourcesSimulation_async_0")
                                                                    .get("/get?foo=0")
                                                                    .check(jmesPath("args.foo").isEL("0")),
                                                            http("GeMi_AsyncReqResourcesSimulation_async_1")
                                                                    .get("/get?foo=1")
                                                                    .check(jmesPath("args.foo").isEL("1")),
                                                            http("GeMi_AsyncReqResourcesSimulation_async_2")
                                                                    .get("/get?foo=2")
                                                                    .check(jmesPath("args.foo").isEL("2"))
                                                    )
                                    )
                            ),
                            Choice.withKey(4,
                                    exec(
                                            http("GeMi_AsyncReqResourcesSimulation_get")
                                                    .get("/get?foo=#{randomInt}")
                                                    .check(jmesPath("args.foo").isEL("#{randomInt}"))
                                                    .resources(
                                                            http("GeMi_AsyncReqResourcesSimulation_async_0")
                                                                    .get("/get?foo=0")
                                                                    .check(jmesPath("args.foo").isEL("0")),
                                                            http("GeMi_AsyncReqResourcesSimulation_async_1")
                                                                    .get("/get?foo=1")
                                                                    .check(jmesPath("args.foo").isEL("1")),
                                                            http("GeMi_AsyncReqResourcesSimulation_async_2")
                                                                    .get("/get?foo=2")
                                                                    .check(jmesPath("args.foo").isEL("2")),
                                                            http("GeMi_AsyncReqResourcesSimulation_async_3")
                                                                    .get("/get?foo=3")
                                                                    .check(jmesPath("args.foo").isEL("3"))
                                                    )
                                    )
                            ),
                            Choice.withKey(5,
                                    exec(
                                            http("GeMi_AsyncReqResourcesSimulation_get")
                                                    .get("/get?foo=#{randomInt}")
                                                    .check(jmesPath("args.foo").isEL("#{randomInt}"))
                                                    .resources(
                                                            http("GeMi_AsyncReqResourcesSimulation_async_0")
                                                                    .get("/get?foo=0")
                                                                    .check(jmesPath("args.foo").isEL("0")),
                                                            http("GeMi_AsyncReqResourcesSimulation_async_1")
                                                                    .get("/get?foo=1")
                                                                    .check(jmesPath("args.foo").isEL("1")),
                                                            http("GeMi_AsyncReqResourcesSimulation_async_2")
                                                                    .get("/get?foo=2")
                                                                    .check(jmesPath("args.foo").isEL("2")),
                                                            http("GeMi_AsyncReqResourcesSimulation_async_3")
                                                                    .get("/get?foo=3")
                                                                    .check(jmesPath("args.foo").isEL("3")),
                                                            http("GeMi_AsyncReqResourcesSimulation_async_4")
                                                                    .get("/get?foo=4")
                                                                    .check(jmesPath("args.foo").isEL("4"))
                                                    )
                                    )
                            )
                    );

    {
        setUp(scn.injectOpen(atOnceUsers(1)).protocols(httpProtocol));
    }
}
