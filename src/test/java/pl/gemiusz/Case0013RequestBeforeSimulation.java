package pl.gemiusz;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class Case0013RequestBeforeSimulation extends Simulation {

    final String JSONFOO = jsonValue();

    public String jsonValue() {
        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://postman-echo.com/get?foo=55555"))
                .setHeader("Accept", "application/json")
                .build();

        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = mapper.readTree(response.body());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        //System.out.println(jsonNode.get("args").get("foo").asText());
        return jsonNode.get("args").get("foo").asText();
    }

    HttpProtocolBuilder httpProtocol =
            http
                    .baseUrl("https://postman-echo.com");

    ScenarioBuilder scn =
            scenario("GeMi_RequestBeforeSimulation")
                    .exec(
                            http("GeMi_RequestBeforeSimulation_get")
                                    .get("/get?foo=" + JSONFOO)
                                    .check(jmesPath("args.foo").is("55555"))
                    );

    {
        setUp(scn.injectOpen(atOnceUsers(5)).protocols(httpProtocol));
    }
}
