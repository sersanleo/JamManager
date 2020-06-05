package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU19 extends Simulation {

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
		.pause(7)
	}

	object Login {
		val login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
        	.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(3)
		.exec(http("LoggedAsJudge")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "judge1")
			.formParam("password", "judge1")
        	.formParam("_csrf", "${stoken}")
		).pause(5)
	}

	object ListJams {
		val listJams = exec(http("ListJams")
			.get("/jams")
			.headers(headers_0))
		.pause(1)
	}

	object ShowJam {
		var showJam = exec(http("ShowJam")
			.get("/jams/4")
			.headers(headers_0))
		.pause(1)
	}

	object ShowTeam1 {
		var showTeam1 = exec(http("ShowTeam1")
			.get("/jams/4/teams/6")
			.headers(headers_0))
		.pause(1)
	}

	object ShowTeam2 {
		var showTeam2 = exec(http("ShowTeam2")
			.get("/jams/4/teams/7")
			.headers(headers_0))
		.pause(1)
	}

	val showTeam1 = scenario("ShowTeam1").exec(
		Home.home, Login.login, ListJams.listJams, ShowJam.showJam, ShowTeam1.showTeam1
	)

	val showTeam2 = scenario("ShowTeam2").exec(
		Home.home, Login.login, ListJams.listJams, ShowJam.showJam, ShowTeam2.showTeam2
	)

	setUp(
		showTeam1.inject(rampUsers(3500) during (100 seconds)),
		showTeam2.inject(rampUsers(3500) during (100 seconds))
		).protocols(httpProtocol)
		.assertions(
			global.responseTime.max.lt(5000),    
			global.responseTime.mean.lt(1000),
			global.successfulRequests.percent.gt(95)
		)
}