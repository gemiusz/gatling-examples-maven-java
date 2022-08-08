package pl.gemiusz;

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

public class Case0007AsyncReqSimulation extends Simulation {

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
            scenario("GeMi_AsyncReqSimulation")
                    .feed(feederInt)
                    .exec(
                            http("GeMi_AsyncReqSimulation_get")
                                    .get("/get?foo=#{randomInt}")
                                    .check(jmesPath("args.foo").isEL("#{randomInt}"))
                    )
                    .repeat("#{randomInt}", "counterRandomInt").on(
                            exec(
                                    http("GeMi_AsyncReqSimulation_async_#{counterRandomInt}")
                                            .get("/get?foo=#{counterRandomInt}")
                                            .check(jmesPath("args.foo").isEL("#{counterRandomInt}"))
                            )
                    );

    {
        setUp(scn.injectOpen(atOnceUsers(1)).protocols(httpProtocol));
    }
}
