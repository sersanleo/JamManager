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
		.pause(14)
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
			.formParam("_csrf", "a78d07c6-876d-4c4b-b068-25c0ab4bfa7b"))
		.pause(3)
		// Logged
		.exec(http("request_4")
			.get("/jams")
			.headers(headers_0))
		.pause(23)
		// ListJams
		.exec(http("request_5")
			.get("/jams/4")
			.headers(headers_0))
		.pause(5)
		// ShowJam
		.exec(http("request_6")
			.get("/jams/4/publish")
			.headers(headers_0))
		.pause(8)
		// PublishResultsForm
		.exec(http("request_7")
			.post("/jams/4/publish")
			.headers(headers_3)
			.formParam("winner.id", "7")
			.formParam("_csrf", "eb037b18-f48a-4736-afcc-3acd9a8ef4ea"))
		.pause(5)
		// PublishResults

	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}