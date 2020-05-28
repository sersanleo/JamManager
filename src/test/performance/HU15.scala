package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU15 extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.9,en;q=0.8")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36")

	val headers_0 = Map(
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Proxy-Connection" -> "keep-alive")

	val headers_3 = Map(
		"Origin" -> "http://www.dp2.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")



	val scn = scenario("HU15")
		.exec(http("request_0")
			.get("/")
			.headers(headers_0))
		.pause(3)
		// Home
		.exec(http("request_1")
			.get("/login")
			.headers(headers_0)
			.resources(http("request_2")
			.get("/login")
			.headers(headers_2)))
		.pause(5)
		// Login
		.exec(http("request_3")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "judge1")
			.formParam("password", "judge1")
			.formParam("_csrf", "50a711f6-695f-4a51-8f54-4d7e4da82673"))
		.pause(7)
		// Logged
		.exec(http("request_4")
			.get("/jams")
			.headers(headers_0))
		.pause(5)
		// ListJams
		.exec(http("request_5")
			.get("/jams/8")
			.headers(headers_0))
		.pause(7)
		// ShowJam
		.exec(http("request_6")
			.get("/jams/8/publish")
			.headers(headers_0))
		.pause(6)
		// PublishResultsForm
		.exec(http("request_7")
			.post("/jams/8/publish")
			.headers(headers_3)
			.formParam("winner.id", "14")
			.formParam("_csrf", "048279cb-cece-4df3-a187-c86e6de00f2a"))
		.pause(8)
		// Publish
		.exec(http("request_8")
			.get("/jams")
			.headers(headers_0))
		.pause(4)
		// ListJams
		.exec(http("request_9")
			.get("/jams/9")
			.headers(headers_0))
		.pause(5)
		// ShowJam
		.exec(http("request_10")
			.get("/jams/9/publish")
			.headers(headers_0))
		.pause(6)
		// PublishResultsForm
		.exec(http("request_11")
			.post("/jams/9/publish")
			.headers(headers_3)
			.formParam("winner.id", "15")
			.formParam("_csrf", "048279cb-cece-4df3-a187-c86e6de00f2a"))
		.pause(11)
		// PublishResults

	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}