package pl.gemiusz;

import io.gatling.javaapi.core.CoreDsl;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.util.List;
import java.util.Map;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class Case0024IterationLoopCondition extends Simulation {

    HttpProtocolBuilder httpProtocol =
            http
                    .baseUrl("https://postman-echo.com");

    ScenarioBuilder scn =
            scenario("GeMi_IterationLoopCondition")
                    .exec(
                            http("GeMi_IterationLoopCondition_post_to_get_accounts_list")
                                    .post("/post")
                                    .body(StringBody("{\"accounts\":[{\"accountID\":\"123\",\"status\":\"ACTIVE\",\"accountType\":\"X\"},{\"accountID\":\"456\",\"status\":\"ACTIVE\",\"accountType\":\"Y\"},{\"accountID\":\"789\",\"status\":\"ACTIVE\",\"accountType\":\"Z\"}]}"))
                                    .asJson()
                                    .check(
                                            jmesPath("json.accounts")
                                                    .ofList()
                                                    .is(
                                                            List.of(
                                                                    Map.of("accountID","123",
                                                                            "status", "ACTIVE",
                                                                            "accountType", "X"
                                                                    ),
                                                                    Map.of("accountID","456",
                                                                            "status", "ACTIVE",
                                                                            "accountType", "Y"
                                                                    ),
                                                                    Map.of("accountID","789",
                                                                            "status", "ACTIVE",
                                                                            "accountType", "Z"
                                                                    )
                                                            )
                                                    )
                                                    .saveAs("GeMi_accountsObj")
                                    )
                    )
                    .foreach("#{GeMi_accountsObj}", "account").on(
                            exec(
                                    doSwitch("#{account.accountType}").on(
                                            CoreDsl.onCase("X").then(
                                                    exec(
                                                            http("GeMi_IterationLoopCondition_get_account_X")
                                                                    .get("/get?foo=#{account.accountID}")
                                                                    .asJson()
                                                                    .check(jmesPath("args.foo").isEL("123"))
                                                    )
                                            ),
                                            CoreDsl.onCase("Y").then(
                                                    exec(
                                                            http("GeMi_IterationLoopCondition_get_account_Y")
                                                                    .get("/get?foo=#{account.accountID}")
                                                                    .asJson()
                                                                    .check(jmesPath("args.foo").isEL("456"))
                                                    )
                                            ),
                                            CoreDsl.onCase("Z").then(
                                                    exec(
                                                            http("GeMi_IterationLoopCondition_get_account_Z")
                                                                    .get("/get?foo=#{account.accountID}")
                                                                    .asJson()
                                                                    .check(jmesPath("args.foo").isEL("789"))
                                                    )
                                            )
                                    )
                            )
                    );

    {
        setUp(scn.injectOpen(atOnceUsers(1)).protocols(httpProtocol));
    }
}
