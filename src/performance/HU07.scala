package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU07 extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.png""", """.*.ico""", """.*.js"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.9")
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

	object Home{
		val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(8)
	}

	object Login{
		val login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(24)
		.exec(http("LoggedAsMember")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "member2")
			.formParam("password", "member2")
			.formParam("_csrf", "${stoken}"))
		.pause(11)
	}

	object ListJams{
		val listJams = exec(http("ListJams")
			.get("/jams")
			.headers(headers_0))
		.pause(13)
	}

	object ShowInscriptionJam{
		val showInscriptionJam = exec(http("ShowInscriptionJam")
			.get("/jams/1")
			.headers(headers_0))
		.pause(16)
	}

	object ShowTeamDetails{
		val showTeamDetails = exec(http("ShowTeamDetails")
			.get("/jams/1/teams/1")
			.headers(headers_0))
		.pause(41)
	}

	object DeleteInvitation{
		val deleteInvitation = exec(http("DeleteInvitation")
			.get("/jams/1/teams/1/invitations/2/delete")
			.headers(headers_0))
		.pause(13)
	}

	object SendInvitationCreate{
		val sendInvitationCreate = exec(http("SendInvitationForm")
			.get("/jams/1/teams/1/invitations/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(26)
		.exec(http("InvitationCreated")
			.post("/jams/1/teams/1/invitations/new")
			.headers(headers_3)
			.formParam("to.username", "member4")
			.formParam("_csrf", "${stoken}"))
		.pause(17)
	}

	object SendInvitationErrorExistingInvitation{
		val sendInvitationErrorExistingInvitation = exec(http("SendInvitationForm")
			.get("/jams/1/teams/1/invitations/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(26)
		.exec(http("ErrorUserInOtherTeam")
			.post("/jams/1/teams/1/invitations/new")
			.headers(headers_3)
			.formParam("to.username", "member1")
				.formParam("_csrf", "${stoken}"))
		.pause(37)
	}

object SendInvitationErrorUserInOtherTeam{
		val sendInvitationErrorUserInOtherTeam = exec(http("SendInvitationForm")
			.get("/jams/1/teams/1/invitations/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(26)
		.exec(http("ErrorUserInOtherTeam")
			.post("/jams/1/teams/1/invitations/new")
			.headers(headers_3)
			.formParam("to.username", "member3")
				.formParam("_csrf", "${stoken}"))
		.pause(12)
}

val createInvitationScenario = scenario("CreateInvitation").exec(
		Home.home, 
		Login.login,
		ListJams.listJams,
		ShowInscriptionJam.showInscriptionJam,
		ShowTeamDetails.showTeamDetails,
		SendInvitationCreate.sendInvitationCreate)

val deleteInvitationScenario = scenario("DeleteInvitation").exec(
		Home.home, 
		Login.login,
		ListJams.listJams,
		ShowInscriptionJam.showInscriptionJam,
		ShowTeamDetails.showTeamDetails,
		DeleteInvitation.deleteInvitation)

val errorExistingInvitationScenario = scenario("ErrorExistingInvitation").exec(
		Home.home, 
		Login.login,
		ListJams.listJams,
		ShowInscriptionJam.showInscriptionJam,
		ShowTeamDetails.showTeamDetails,
		SendInvitationErrorExistingInvitation.sendInvitationErrorExistingInvitation)

val errorUserInOtherTeamScenario = scenario("ErrorUserInOtherTeam").exec(
		Home.home, 
		Login.login,
		ListJams.listJams,
		ShowInscriptionJam.showInscriptionJam,
		ShowTeamDetails.showTeamDetails,
		SendInvitationErrorUserInOtherTeam.sendInvitationErrorUserInOtherTeam)
	
	setUp(
		createInvitationScenario.inject(rampUsers(600) during (100 seconds)),
		deleteInvitationScenario.inject(rampUsers(600) during (100 seconds)),
		errorExistingInvitationScenario.inject(rampUsers(600) during (100 seconds)),
		errorUserInOtherTeamScenario.inject(rampUsers(600) during (100 seconds))
		).protocols(httpProtocol)
		.assertions(
		global.responseTime.max.lt(5000),
			global.responseTime.mean.lt(1000),
			global.successfulRequests.percent.gt(95)
		)

}