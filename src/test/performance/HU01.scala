package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class RecordedSimulation extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36")

	val headers_0 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.9,en;q=0.8",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.9,en;q=0.8",
		"Proxy-Connection" -> "keep-alive")

	val headers_3 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.9,en;q=0.8",
		"Origin" -> "http://www.dp2.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_7 = Map(
		"Proxy-Connection" -> "Keep-Alive",
		"User-Agent" -> "Microsoft-WNS/10.0")

	val headers_8 = Map(
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.9,en;q=0.8",
		"Pragma" -> "no-cache",
		"Proxy-Connection" -> "keep-alive")

    val uri2 = "http://tile-service.weather.microsoft.com/es-ES/livetile/preinstall"
    val uri3 = "http://www.gstatic.com/generate_204"

	val scn = scenario("RecordedSimulation")
		.exec(http("request_0")
			.get("/")
			.headers(headers_0))
		.pause(4)
		// Home
		.exec(http("request_1")
			.get("/login")
			.headers(headers_0)
			.resources(http("request_2")
			.get("/login")
			.headers(headers_2)))
		.pause(12)
		// Login
		.exec(http("request_3")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "jamOrganizator1")
			.formParam("password", "jamOrganizator1")
			.formParam("_csrf", "3603a0fc-32db-44d9-af41-f551614059d0"))
		.pause(16)
		// Logged
		.exec(http("request_4")
			.get("/jams")
			.headers(headers_0))
		.pause(7)
		// ListJams
		.exec(http("request_5")
			.get("/jams/new")
			.headers(headers_0))
		.pause(79)
		// CreateJamForm
		.exec(http("request_6")
			.post("/jams/new")
			.headers(headers_3)
			.formParam("name", "Test Jam")
			.formParam("description", "Test Description")
			.formParam("difficulty", "5")
			.formParam("inscriptionDeadline", "2020-10-5 10:00")
			.formParam("maxTeamSize", "6")
			.formParam("minTeams", "5")
			.formParam("maxTeams", "10")
			.formParam("start", "2020-10-7 10:00")
			.formParam("end", "2020-10-8 10:00")
			.formParam("_csrf", "a62e0d98-ef39-4718-9f25-8113444dea44"))
		.pause(77)
		// CreateJam
		.exec(http("request_7")
			.get(uri2 + "?region=ES&appid=C98EA5B0842DBB9405BBF071E1DA76512D21FE36&FORM=Threshold")
			.headers(headers_7)
			.resources(http("request_8")
			.get(uri3)
			.headers(headers_8),
            http("request_9")
			.get(uri3)
			.headers(headers_8)))
		.pause(12)
		.exec(http("request_10")
			.get(uri3)
			.headers(headers_8)
			.resources(http("request_11")
			.get(uri3)
			.headers(headers_8)))
		.pause(107)
		.exec(http("request_12")
			.get(uri3)
			.headers(headers_8))
		.pause(22)
		.exec(http("request_13")
			.get("/jams/11/edit")
			.headers(headers_0))
		.pause(5)
		.exec(http("request_14")
			.post("/jams/11/edit")
			.headers(headers_3)
			.formParam("name", "Test Jam changed")
			.formParam("description", "Test Description")
			.formParam("difficulty", "5")
			.formParam("inscriptionDeadline", "2020-10-5 10:00")
			.formParam("maxTeamSize", "6")
			.formParam("minTeams", "5")
			.formParam("maxTeams", "10")
			.formParam("start", "2020-10-7 10:00")
			.formParam("end", "2020-10-8 10:00")
			.formParam("_csrf", "a62e0d98-ef39-4718-9f25-8113444dea44"))
		.pause(18)
		// EditJam
		.exec(http("request_15")
			.get("/jams/11/delete")
			.headers(headers_0))
		.pause(6)
		// DeleteJam

	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}