package pl.gemiusz;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class Case0002PDFdownloadSimulation extends Simulation {

    // https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf
    HttpProtocolBuilder httpProtocol =
            http
                    .baseUrl("https://www.w3.org");

    ScenarioBuilder scn =
            scenario("GeMi_PDFdownloadSimulation")
                    .exec(
                            http("GeMi_PDFdownloadSimulation")
                                    .get("/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf")
                                    .check(bodyBytes().is(RawFileBody("otherFiles/dummy.pdf")))
                                    .check(bodyLength().is(13264))
                    );

    {
        setUp(scn.injectOpen(atOnceUsers(1)).protocols(httpProtocol));
    }
}
