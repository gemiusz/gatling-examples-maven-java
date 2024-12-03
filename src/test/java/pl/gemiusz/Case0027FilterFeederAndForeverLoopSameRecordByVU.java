package pl.gemiusz;

import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class Case0027FilterFeederAndForeverLoopSameRecordByVU extends Simulation {

    HttpProtocolBuilder httpProtocol =
            http
                    .baseUrl("https://postman-echo.com");

    List<Map<String, Object>> allRecords = csv("feeders/searchToFilter.csv").readRecords();

    List<Map<String, Object>> filterdRecords = allRecords.stream()
            .filter(n -> n.get("searchCriterion").equals("eee"))
            .toList();

    FeederBuilder<Object> fedder = listFeeder(filterdRecords).circular();

    ScenarioBuilder scn =
            scenario("GeMi_FilterFeederAndForeverLoopSameRecordByVU")
                    .feed(fedder)
                    .forever()
                        .on(
                            exec(
                                http("GeMi_FilterFeederAndForeverLoopSameRecordByVU_get")
                                        .get("/get?foo=#{searchComputerName}")
                                        .check(jmesPath("args.foo").isEL("#{searchComputerName}"))
                                    )
                            .exec(session -> {
                                    System.out.println("GeMi_log: VU_id: "+ session.userId() + " | Fedder_record: " + session.get("searchCriterion").toString() + "   " + session.get("searchComputerName").toString());
                                    return session;
                                })
                                    .pause(Duration.ofSeconds(1))
                        );

    {
        setUp(scn.injectOpen(constantUsersPerSec(1).during(4)).protocols(httpProtocol)).maxDuration(Duration.ofSeconds(15));
    }
}
