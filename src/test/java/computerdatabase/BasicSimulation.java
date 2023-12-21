package computerdatabase; // 1

// 2

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.atOnceUsers;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;

/***
 * What does it mean?
 *
 * 1. The optional package.
 * 2. The required imports.
 * 3. The class declaration. Note that it extends Simulation.
 * 4. The common configuration to all HTTP requests.
 * 5. The baseUrl that will be prepended to all relative urls.
 * 6. Common HTTP headers that will be sent with all the requests.
 * 7. The scenario definition.
 * 8. An HTTP request, named request_1. This name will be displayed in the final reports.
 * 9. The url this request targets with the GET method.
 * 10. Some pause/think time.
 * Duration units default to seconds, e.g. pause(5) is equivalent to java.time.Duration.ofSeconds(5) in Java or pause(5.seconds) in Scala.
 * 11. Where one sets up the scenarios that will be launched in this Simulation.
 * 12. Declaring that we will inject one single user into the scenario named scn.
 * 13. Attaching the HTTP configuration declared above.
 */
public class BasicSimulation extends Simulation { // 3

  HttpProtocolBuilder httpProtocol = http // 4
          .baseUrl("http://computer-database.gatling.io") // 5
          .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // 6
          .doNotTrackHeader("1")
          .acceptLanguageHeader("en-US,en;q=0.5")
          .acceptEncodingHeader("gzip, deflate")
          .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0");

  ScenarioBuilder scn = scenario("BasicSimulation") // 7
          .exec(http("request_1") // 8
                  .get("/")) // 9
          .pause(5); // 10

  {
    setUp( // 11
            scn.injectOpen(atOnceUsers(1)) // 12
    ).protocols(httpProtocol); // 13
  }
}
