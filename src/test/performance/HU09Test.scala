package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU09Test extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.js""", """.*.css""", """.*.png""", """.*.ico"""), WhiteList())
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
		.pause(11)
	}

	object Login {
		val login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.resources(http("request_2")
			.get("/login")
			.headers(headers_2)))
		.pause(11)
	}

	object Logged {
		val logged = exec(http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "member2")
			.formParam("password", "member2")
			.formParam("_csrf", "ff1d1871-616a-43ee-aaa5-2d248a2d097d"))
		.pause(10)
	}

	object ShowJams {
		val showJams = exec(http("ShowJams")
			.get("/jams")
			.headers(headers_0))
		.pause(8)
	}

	object ListJam {
		val listJam = exec(http("ListJam")
			.get("/jams/1")
			.headers(headers_0))
		.pause(17)
	}

	object ViewTeam {
		val viewTeam = exec(http("ViewTeam")
			.get("/jams/1/teams/1")
			.headers(headers_0))
		.pause(13)
	}

	object SendInvitation {
		val sendInvitation = exec(http("SendInvitation")
			.get("/jams/1/teams/1/invitations/new")
			.headers(headers_0))
		.pause(22)
	}

	object SendingError {
		val sendingError = exec(http("SendingError")
			.post("/jams/1/teams/1/invitations/new")
			.headers(headers_3)
			.formParam("to.username", "NoExistMember")
			.formParam("_csrf", "58520cd7-fc3c-4184-986b-f5ae8d1fc2ca"))
		.pause(8)
	}

	val invitationErrorScn = scenario("HU09Test").exec(Home.home, Login.login, Logged.logged, ShowJams.showJams, ListJam.listJam, ViewTeam.viewTeam, SendInvitation.sendInvitation, SendingError.sendingError)
	setUp(invitationErrorScn.inject(rampUsers(5000) during (100 seconds))).protocols(httpProtocol)
        .assertions(global.responseTime.max.lt(5000),
			global.responseTime.mean.lt(1000),
			global.successfulRequests.percent.gt(95))
}
