package pl.gemiusz;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Session;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.util.Map;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class Case0017ForeachAfterForeachSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol =
            http
                    .baseUrl("https://postman-echo.com");

    ScenarioBuilder scn =
            scenario("GeMi_ForeachAfterForeachSimulation")
                    .exec(
                            http("GeMi_ForeachAfterForeachSimulation_langs_post")
                                    .post("/post")
                                    .body(StringBody("""
                                            {
                                              "langs": [
                                                "pl",
                                                "fr",
                                                "en"
                                              ]
                                            }
                                            """)
                                    )
                                    .asJson()
                                    .check(
                                            jmesPath("json.langs")
                                                    .ofList()
                                                    .saveAs("GeMi_langs")
                                    )
                    ).exec(session -> {
                        System.out.println("GeMi_langs: " + session.getString("GeMi_langs"));
                        return session;
                    }).foreach("#{GeMi_langs}", "GeMi_langs_el").on(
                            exec(session -> {
                                System.out.println("GeMi_langs_el: " + session.getString("GeMi_langs_el"));
                                return session;
                            })
                    ).exec(
                            http("GeMi_ForeachAfterForeachSimulation_curses_post")
                                    .post("/post")
                                    .body(StringBody("""
                                            {
                                              "curses": [
                                                {
                                                  "pictureId": 1,
                                                  "language": "pl"
                                                },
                                                {
                                                  "pictureId": 3,
                                                  "language": "en"
                                                },
                                                {
                                                  "pictureId": 2,
                                                  "language": "fr"
                                                },
                                                {
                                                  "pictureId": 5,
                                                  "language": "pl"
                                                }
                                              ]
                                            }
                                            """)
                                    )
                                    .asJson()
                                    .check(
                                            jmesPath("json.curses")
                                                    .ofList()
                                                    .saveAs("GeMi_curses")
                                    )
                    ).exec(session -> {
                        System.out.println("GeMi_curses: " + session.getString("GeMi_curses"));
                        return session;
                    }).foreach("#{GeMi_curses}", "GeMi_curses_el").on(
                            exec(session -> {
                                System.out.println("GeMi_curses_el: " + session.getString("GeMi_curses_el"));
                                return session;
                            })
                    ).foreach("#{GeMi_langs}", "GeMi_langs_el").on(
                            exec(session -> {
                                System.out.println("------------------------------------------------------------------------------");
                                System.out.println("- GeMi_langs_el: " + session.getString("GeMi_langs_el"));
                                return session;
                            }).foreach("#{GeMi_curses}", "GeMi_curses_el").on(
                                    exec(session -> {
                                        System.out.println("-- GeMi_curses_el: " + session.getString("GeMi_curses_el"));
                                        Map<String, Object> gemiCursesElObj = session.getMap("GeMi_curses_el");
                                        if (session.getString("GeMi_langs_el").equals(gemiCursesElObj.get("language").toString())) {
                                            System.out.println("--- GeMi_langs_el EQ GeMi_curses_el_language -> " + session.getString("GeMi_langs_el") + " " + session.getString("GeMi_curses_el"));
                                            Session sessionTrue = session.set("GeMi_pictureId", gemiCursesElObj.get("pictureId")).set("GeMi_isThisLang", true);
                                            return sessionTrue;
                                        }
                                        System.out.println("--- GeMi_langs_el NE GeMi_curses_el_language -> " + session.getString("GeMi_langs_el") + " " + session.getString("GeMi_curses_el"));
                                        Session sessionFalse = session.set("GeMi_isThisLang", false);
                                        return sessionFalse;
                                    }).doIf("#{GeMi_isThisLang}").then(
                                            exec(session -> {
                                                System.out.println("---- Do something using GeMi_pictureId: " + session.getString("GeMi_pictureId"));
                                                return session;
                                            }).exec(
                                                    http("GeMi_ForeachAfterForeachSimulation_pictureId_#{GeMi_pictureId}_get")
                                                            .get("/get?pictureId=#{GeMi_pictureId}")
                                                            .check(jmesPath("args.pictureId").isEL("#{GeMi_pictureId}"))
                                            )
                                    )
                            )
                    );

    {
        setUp(scn.injectOpen(atOnceUsers(1)).protocols(httpProtocol));
    }
}
