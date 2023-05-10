package pl.gemiusz;

import io.gatling.javaapi.core.OpenInjectionStep;
import io.gatling.javaapi.core.ScenarioBuilder;
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

public class Case0022SetOrRefreshTokenSimulation extends Simulation {

    String tokenString = "NOT_SET";
    HttpProtocolBuilder httpProtocol =
            http
                    .baseUrl("https://postman-echo.com").shareConnections();


    Iterator<Map<String, Object>> feederUUID =
            Stream.generate((Supplier<Map<String, Object>>) () -> {
                        String uuidString = UUID.randomUUID().toString();
                        return Collections.singletonMap("uuidString", uuidString);
                    }
            ).iterator();

    ScenarioBuilder scnForSetOrRefreshToken =
            scenario("GeMi_SetOrRefreshToken")
                    .feed(feederUUID)
                    .exec(
                            http("GeMi_SetOrRefreshToken_get")
                                    .get("/get?foo=#{uuidString}")
                                    .silent()
                                    .check(jmesPath("args.foo").isEL("#{uuidString}").saveAs("tokenStringNew"))
                    ).exec(session -> {
                        System.out.println("GeMi_SetOrRefreshToken_uuidString: " + session.get("uuidString"));
                        System.out.println("GeMi_SetOrRefreshToken_tokenStringNew: " + session.get("tokenStringNew"));
                        tokenString = session.get("tokenStringNew");
                        return session;
                    });


    ScenarioBuilder scn =
            scenario("GeMi_SomeRequestWithToken")
                    .exec(session -> session.set("tokenStringSession", tokenString))
                    .exec(
                            http("GeMi_SomeRequestWithToken_get")
                                    .get("/get?foo=#{tokenStringSession}")
                                    .check(jmesPath("args.foo").saveAs("tokenString4Print"))
                                    .check(jmesPath("args.foo").not("NOT_SET"))
                                    //.check(jmesPath("args.foo").is(session -> tokenString))
                    ).exec(session -> {
                        System.out.println("----------------------------------------------------------");
                        System.out.println("GeMi_SomeRequestWithToken_tokenString: " + tokenString);
                        System.out.println("GeMi_SomeRequestWithToken_tokenStringSession: " + session.get("tokenStringSession").toString());
                        System.out.println("GeMi_SomeRequestWithToken_tokenString4Print: " + session.get("tokenString4Print").toString());
                        System.out.println("----------------------------------------------------------");
                        return session;
                    });


    {
        setUp(
                scnForSetOrRefreshToken.injectOpen(
                        //Duration of this scenario = 1. Time between requests * (2. Number of requests - 1)
                        Stream.generate(
                                () -> new OpenInjectionStep[]{
                                        atOnceUsers(1),
                                        nothingFor(Duration.ofSeconds(10)) //1. Time between requests
                                }

                        ).limit(4) //2. Number of requests
                                .flatMap(Stream::of).toArray(OpenInjectionStep[]::new)),
                scn.injectOpen(
                        nothingFor(Duration.ofSeconds(2)),
                        constantUsersPerSec(20).during(30)
                )
        ).protocols(httpProtocol);
    }
}
