package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU04 extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.9,en;q=0.8")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36")

	val headers_0 = Map("Proxy-Connection" -> "keep-alive")

	val headers_2 = Map(
		"Proxy-Connection" -> "keep-alive",
		"Purpose" -> "prefetch")



	val scn = scenario("HU04")
		.exec(http("request_0")
			.get("/")
			.headers(headers_0))
		.pause(5)
		// Home
		.exec(http("request_1")
			.get("/jams")
			.headers(headers_0))
		.pause(9)
		// ListJams
		.exec(http("request_2")
			.get("/")
			.headers(headers_2))
		.pause(14)
		// Home
		.exec(http("request_3")
			.get("/jams")
			.headers(headers_0))
		.pause(2)
		// ListJams
		.exec(http("request_4")
			.get("/jams/1")
			.headers(headers_0))
		.pause(3)
		// ShowJam

	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}