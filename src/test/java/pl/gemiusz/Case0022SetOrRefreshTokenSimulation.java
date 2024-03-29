package pl.gemiusz;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;
import java.time.Instant;
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
    boolean tokenRefreshContinue = true;
    Instant tokenRefreshInstant = Instant.now();
    int refreshTokenEverySeconds = 10;

    HttpProtocolBuilder httpProtocol =
            http
                    .baseUrl("https://postman-echo.com").shareConnections();


    Iterator<Map<String, Object>> feederUUID =
            Stream.generate((Supplier<Map<String, Object>>) () -> {
                        String uuidString = UUID.randomUUID().toString();
                        return Collections.singletonMap("uuidString", uuidString);
                    }
            ).iterator();

    ChainBuilder chainGenerateAndSetToken =
            feed(feederUUID)
                    .exec(
                            http("GeMi_GenerateAndSetToken_get")
                                    .get("/get")
                                    .queryParam("foo", "#{uuidString}")
                                    .silent()
                                    .check(jmesPath("args.foo").isEL("#{uuidString}").saveAs("tokenStringNew"))
                    ).exec(session -> {
                        System.out.println("GeMi_GenerateAndSetToken_uuidString: " + session.get("uuidString"));
                        System.out.println("GeMi_GenerateAndSetToken_tokenStringNew: " + session.get("tokenStringNew"));
                        tokenString = session.get("tokenStringNew");
                        return session;
                    });

    ScenarioBuilder scnForSetFirstToken =
            scenario("GeMi_SetFirstToken").exec(chainGenerateAndSetToken);

    ScenarioBuilder scnForRefreshToken =
            scenario("GeMi_RefreshToken")
                    .asLongAs(session -> tokenRefreshContinue)
                    .on(
                            pause(Duration.ofSeconds(1))
                                    .doIf(session -> tokenRefreshInstant.plusSeconds(refreshTokenEverySeconds).isBefore(Instant.now()))
                                    .then(
                                            exec(session -> {
                                                tokenRefreshInstant = Instant.now();
                                                return session;
                                            })
                                            .exec(chainGenerateAndSetToken)
                                    )
                    );

    ScenarioBuilder scnForRefreshTokenAbort =
            scenario("GeMi_RefreshTokenAbort").exec(session -> {
                tokenRefreshContinue = false;
                return session;
            });

    ScenarioBuilder scn =
            scenario("GeMi_SomeRequestWithToken")
                    // Don't forget to set the uptodate token for every request
                    .exec(session -> session.set("tokenStringSession", tokenString))
                    .exec(
                            http("GeMi_SomeRequestWithToken_get")
                                    .get("/get")
                                    .queryParam("foo", "#{tokenStringSession}")
                                    .check(jmesPath("args.foo").saveAs("tokenString4Print"))
                                    .check(jmesPath("args.foo").not("NOT_SET"))
                                    //.check(jmesPath("args.foo").is(session -> tokenString))
                    ).exec(session -> {
                        System.out.println("----------------------------------------------------------");
                        System.out.println("GeMi_SomeRequestWithToken_tokenString: " + tokenString);
                        System.out.println("GeMi_SomeRequestWithToken_tokenStringSession: " + session.getString("tokenStringSession"));
                        System.out.println("GeMi_SomeRequestWithToken_tokenString4Print: " + session.getString("tokenString4Print"));
                        System.out.println("----------------------------------------------------------");
                        return session;
                    });


    {
        setUp(scnForSetFirstToken.injectOpen(atOnceUsers(1))
                .andThen(
                        scnForRefreshToken.injectOpen(atOnceUsers(1)),
                        //--------------------------------
                        scn.injectOpen(
                                constantUsersPerSec(10).during(30)
                                )
                        //--------------------------------
                                .andThen(scnForRefreshTokenAbort.injectOpen(atOnceUsers(1)))
                )
        ).protocols(httpProtocol);
    }
}
