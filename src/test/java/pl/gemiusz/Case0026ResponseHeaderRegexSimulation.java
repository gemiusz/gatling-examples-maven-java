package pl.gemiusz;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.atOnceUsers;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.*;

public class Case0026ResponseHeaderRegexSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol =
            http
                    .baseUrl("https://postman-echo.com");

    ScenarioBuilder scn =
            scenario("GeMi_ResponseHeaderRegexSimulation")
                    .exec(
                            http("GeMi_ResponseHeaderRegexSimulation_get")
                                    .get("/response-headers")
                                    .queryParam("content-type", "text/html; charset=utf-8")
                                    .queryParam("sap-err-id", "ICFLOGONREQUIRED")
                                    .queryParam("location", "/sap/bc/ui2/flp?saml2=disabled&_sap-hash=JTIzU2hlbGwtaG9tZQ&sap-system-login=X&sap-system-login-cookie=X&**sap-contextid=**SID:ANON:IKAWEQQM1AS01_QM1_22:aK-vnihlk8JcmIebDTWefQVnZujrJlbmE8-etYjR-ATT")
                                    .queryParam("sap-server", true)
                                    .check(status().is(200))
                                    .check(
                                            headerRegex("location", "&\\*\\*sap-contextid=\\*\\*(.*)").saveAs("GeMi_header_location_sap-contextid")
                                    )
                    ).exec(session -> {
                        System.out.println("GeMi_header_location_sap-contextid: " + session.get("GeMi_header_location_sap-contextid"));
                        return session;
                    });

    {
        setUp(scn.injectOpen(atOnceUsers(1)).protocols(httpProtocol));
    }
}
