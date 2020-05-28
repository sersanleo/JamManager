package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU03 extends Simulation {

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

	val headers_3 = Map(
		"Proxy-Connection" -> "keep-alive",
		"Purpose" -> "prefetch",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_6 = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Proxy-Connection" -> "keep-alive")

	val headers_7 = Map(
		"Origin" -> "http://www.dp2.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")



	val scn = scenario("HU03")
		.exec(http("request_0")
			.get("/")
			.headers(headers_0))
		.pause(7)
		// Home
		.exec(http("request_1")
			.get("/jams")
			.headers(headers_0))
		.pause(8)
		// ListJams
		.exec(http("request_2")
			.get("/jams/3")
			.headers(headers_0))
		.pause(24)
		// AnonymousShowJamResources
		.exec(http("request_3")
			.get("/")
			.headers(headers_3)
			.resources(http("request_4")
			.get("/")
			.headers(headers_0)))
		.pause(12)
		.exec(http("request_5")
			.get("/login")
			.headers(headers_0)
			.resources(http("request_6")
			.get("/login")
			.headers(headers_6)))
		.pause(18)
		// LoginForm
		.exec(http("request_7")
			.post("/login")
			.headers(headers_7)
			.formParam("username", "jamOrganizator1")
			.formParam("password", "jamOrganizator1")
			.formParam("_csrf", "f16329cf-c026-47ba-a66f-c3e0853b732e"))
		.pause(5)
		// Logged
		.exec(http("request_8")
			.get("/jams")
			.headers(headers_0))
		.pause(22)
		// ListJams
		.exec(http("request_9")
			.get("/jams/3")
			.headers(headers_0))
		.pause(12)
		// LoggedShowJamResources

	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}