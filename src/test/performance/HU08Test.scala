package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU08Test extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png"""), WhiteList())
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
			.formParam("_csrf", "b13bbc3c-a3a8-4f76-821e-6f3dff39851f"))
		.pause(6)
	}

	object ViewInvitations {
		val viewInvitations = exec(http("ViewInvitations")
			.get("/invitations")
			.headers(headers_0))
		.pause(14)
	}

	object RejectInvitation {
		val rejectInvitation = exec(http("RejectInvitation")
			.get("/invitations/5/reject")
			.headers(headers_0))
		.pause(18)
	}

	object AcceptInvitation {
		val acceptInvitation = exec(http("AcceptInvitation")
			.get("/invitations/1/accept")
			.headers(headers_0))
		.pause(7)
	}

	val acceptScn = scenario("Accept").exec(Home.home, Login.login, Logged.logged, ViewInvitations.viewInvitations, AcceptInvitation.acceptInvitation)
	val rejectScn = scenario("Reject").exec(Home.home, Login.login, Logged.logged, ViewInvitations.viewInvitations, RejectInvitation.rejectInvitation)

	setUp(acceptScn.inject(rampUsers(5000) during (100 seconds)), rejectScn.inject(rampUsers(5000) during (100 seconds))).protocols(httpProtocol)
}