package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU11Test extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.png""", """.*.js""", """.*.ico""", """.*.css"""), WhiteList())
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

	object Home {
		val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(6)
	}

	object Login {
		val login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.resources(http("request_2")
			.get("/login")
			.headers(headers_2)))
		.pause(9)
	}

	object Logged {
		val logged = exec(http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "member1")
			.formParam("password", "member1")
			.formParam("_csrf", "e6bd9cbd-f692-4a1d-baa5-e12ffcd20b41"))
		.pause(6)
	}

	object JamList {
		val jamList = exec(http("JamList")
			.get("/jams")
			.headers(headers_0))
		.pause(7)
	}

	object ShowFullJam {
		val showFullJam = exec(http("ShowFullJam")
			.get("/jams/7")
			.headers(headers_0))
		.pause(16)
	}

	object ErrorCreationTeam {
		val errorCreationTeam = exec(http("ErrorCreationTeam")
			.get("/jams/7/teams/new")
			.headers(headers_0))
		.pause(12)
	}

	val errorScn = scenario("HU11Test").exec(Home.home, Login.login, Logged.logged, JamList.jamList, ShowFullJam.showFullJam, ErrorCreationTeam.errorCreationTeam)
	setUp(errorScn.inject(rampUsers(5000) during (100 seconds))).protocols(httpProtocol)
}