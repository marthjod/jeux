package jeux

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class JeuxSimulation extends Simulation {

    val responseThreshold = 500 // msec

    val httpProtocol = http
        .baseURL("<BASE_URL>")
        .inferHtmlResources()
        .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
        .acceptEncodingHeader("gzip, deflate")
        .acceptLanguageHeader("de,en-US;q=0.7,en;q=0.3")
        .connection("keep-alive")
        .userAgentHeader("gatling.io")

    val scn = scenario("Rankings")
        .exec(
            http("rankings")
                .get("/")
                .check(status.is(200))
                .check(regex("""api-status"""))
                .check(responseTimeInMillis.lessThan(responseThreshold)))

    setUp(scn
        .inject(
        rampUsers(10) over(5 seconds),
        constantUsersPerSec(30) during(30 seconds),
        constantUsersPerSec(30) during(30 seconds) randomized,
        rampUsersPerSec(10) to(40) during(15 seconds),
        rampUsersPerSec(10) to(40) during(15 seconds) randomized)
        .protocols(httpProtocol)
    )
}

