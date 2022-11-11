package pl.gemiusz;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class Case0018GetTokenWhenStatus401Simulation extends Simulation {

    HttpProtocolBuilder httpProtocol =
            http
                    .baseUrl("https://postman-echo.com");

    Iterator<Map<String, Object>> feeder200or401 =
            Stream.generate((Supplier<Map<String, Object>>) () -> {
                        int randomOfTwoInts = new Random().nextBoolean() ? 200 : 401;
                        return Collections.singletonMap("statusHttp", randomOfTwoInts);
                    }
            ).iterator();

    Iterator<Map<String, Object>> feederUUID =
            Stream.generate((Supplier<Map<String, Object>>) () -> {
                        String uuidString = UUID.randomUUID().toString();
                        return Collections.singletonMap("uuidString", uuidString);
                    }
            ).iterator();

    String globalToken = "StartingToken";

    ScenarioBuilder scn =
            scenario("GeMi_GetTokenWhenStatus401")
                    .feed(feeder200or401)
                    .exec(
                            http("GeMi_GetTokenWhenStatus401_get_status")
                                    .get("/status/#{statusHttp}")
                                    .header("Token", session -> this.globalToken)
                                    .check(
                                            status()
                                                    .isEL("#{statusHttp}")
                                                    .saveAs("GeMi_Status_Code")
                                    )
                    )
                    .doIfEquals("#{GeMi_Status_Code}", 401)
                    .then(
                            feed(feederUUID)
                                    .exec(
                                            http("GeMi_GetTokenWhenStatus401_get_token")
                                                    .post("/post")
                                                    .body(StringBody("""
                                                            {
                                                              "UUIDasToken": "#{uuidString}"
                                                            }
                                                            """)
                                                    )
                                                    .asJson()
                                                    .check(
                                                            jmesPath("json.UUIDasToken")
                                                                    .isEL("#{uuidString}")
                                                                    .saveAs("uuidAsToken")
                                                    )
                                    )
                                    .exec(session -> {
                                                this.globalToken = session.getString("uuidAsToken");
                                                return session;
                                            }
                                    )
                    );

    {
        setUp(scn.injectOpen(constantUsersPerSec(1).during(3)).protocols(httpProtocol));
    }
}
