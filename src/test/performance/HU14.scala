package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU14 extends Simulation {

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

	object Home {
		val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(3)
	}

	object Login {
		val login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.resources(http("request_2")
			.get("/login")
			.headers(headers_2)))
		.pause(3)
	}

	object LoggedAsJudge {
		val loggedAsJudge = exec(http("LoggedAsJudge")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "judge1")
			.formParam("password", "judge1")
			.formParam("_csrf", "e6475e82-afc4-45e3-8845-350dd147c045"))
		.pause(7)
	}

	object ListJams {
		val listJams = exec(http("ListJams")
			.get("/jams")
			.headers(headers_0))
		.pause(9)
	}

	object ShowJam {
		var showJam = exec(http("ShowJam")
			.get("/jams/4")
			.headers(headers_0))
		.pause(6)
	}

	object RateTeamForm {
		var rateTeamForm = exec(http("RateTeamForm")
			.get("/jams/4/teams/7/marks")
			.headers(headers_0))
		.pause(10)
	}

	object RateTeam {
		var rateTeam = exec(http("RateTeam")
			.post("/jams/4/teams/7/marks")
			.headers(headers_3)
			.formParam("value", "5")
			.formParam("comments", "Great work")
			.formParam("_csrf", "4b8cd936-1111-432c-851c-26147734b05e"))
		.pause(7)
	}

	val rateTeam = scenario("RateTeam").exec(Home.home, Login.login, LoggedAsJudge.loggedAsJudge, ListJams.listJams, ShowJam.showJam, RateTeamForm.rateTeamForm, RateTeam.rateTeam)

	setUp(rateTeam.inject(atOnceUsers(1))).protocols(httpProtocol)
}