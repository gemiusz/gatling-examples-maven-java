package pl.gemiusz;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.Proxy;
import static io.gatling.javaapi.http.HttpDsl.http;

/**
 * HOW TO RUN:
 * mvnw gatling:test -Dgatling.simulationClass=pl.gemiusz.Case0011ProxyCommandLineParametersSimulation -Dfoo=10 -Dbar=GeMi -DuseProxy=true -DproxyHost=127.0.0.1 -DproxyPort=8080
 */
public class Case0011ProxyCommandLineParametersSimulation extends Simulation {

    int foo = Integer.getInteger("foo", 1);
    String bar = System.getProperty("bar");


    static boolean useProxy = Boolean.getBoolean("useProxy");
    static String proxyHost = System.getProperty("proxyHost");
    static int proxyPort = Integer.getInteger("proxyPort", 8080);

    static HttpProtocolBuilder httpProtocol = httpProtocolCustom();

    public static HttpProtocolBuilder httpProtocolCustom() {
        HttpProtocolBuilder httpProtocolB =
                http
                        .baseUrl("https://postman-echo.com");

        if (useProxy) {
            if (proxyHost.length() == 0) {
                proxyHost = "127.0.0.1";
            }
            httpProtocolB = httpProtocolB.proxy(Proxy(proxyHost, proxyPort));
        }
        return httpProtocolB;
    }

    ScenarioBuilder scn =
            scenario("GeMi_ProxyCommandLineParametersSimulation")
                    .exec(
                            http("GeMi_ProxyCommandLineParametersSimulation_get")
                                    .get("/get?foo=" + foo + "&bar=" + bar)
                                    .check(jmesPath("args.foo").is(String.valueOf(foo)))
                                    .check(jmesPath("args.bar").is(String.valueOf(bar)))
                    );

    {
        setUp(scn.injectOpen(atOnceUsers(1)).protocols(httpProtocol));
    }
}
