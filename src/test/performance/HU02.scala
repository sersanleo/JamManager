package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU02 extends Simulation {

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
		.pause(14)
	}

	object Login{
		val login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(24)
		.exec(http("LoginAsJamOrganizator")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "jamOrganizator1")
			.formParam("password", "jamOrganizator1")
			.formParam("_csrf", "${stoken}"))
		.pause(35)
	}
	
	object ListJams{
		val listJams = exec(http("ListJams")
			.get("/jams")
			.headers(headers_0))
		.pause(14)
	}

	object ShowInProgressJam{
		val showInProgressJam = exec(http("ShowInProgressJam")
			.get("/jams/3")
			.headers(headers_0))
		.pause(17)
	}

	object NewJamResourceForm{
		val newJamResourceForm = exec(http("NewJamResourceForm")
			.get("/jams/3/jamResources/new")
			.headers(headers_0)
	.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(46)
		.exec(http("JamResourceCreation")
			.post("/jams/3/jamResources/new")
			.headers(headers_3)
			.formParam("description", "Una buena descripcion")
			.formParam("downloadUrl", "https://www.youtube.com/")
			.formParam("_csrf", "${stoken}") )
		.pause(44)
	}

	object EditJamResourceForm{
		val editJamResourceForm = exec(http("EditJamResourceForm")
			.get("/jams/3/jamResources/3/edit")
			.headers(headers_0)
				.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(218)
		.exec(http("JamResourceEdited")
			.post("/jams/3/jamResources/3/edit")
			.headers(headers_3)
			.formParam("description", "Otra buena descripcion buena descripcion")
			.formParam("downloadUrl", "https://www.youtube.com/")
				.formParam("_csrf", "${stoken}") )
		.pause(21)
	}



	object JamResourceDeleted{
		val jamResourceDeleted =exec(http("JamResourceDeleted")
			.get("/jams/3/jamResources/11/delete")
			.headers(headers_0))
		.pause(36)
	}
	val createJamResourceScenario = scenario("CreateJamResource").exec(
		Home.home, 
		Login.login,
		ListJams.listJams,
		ShowInProgressJam.showInProgressJam,
		NewJamResourceForm.newJamResourceForm)
	
	val editJamResourceScenario = scenario("EditJamResource").exec(
		Home.home, 
		Login.login,
		ListJams.listJams,
		ShowInProgressJam.showInProgressJam,
		EditJamResourceForm.editJamResourceForm)

	val deleteJamResourceScenario = scenario("DeleteJamResource").exec(
		Home.home, 
		Login.login,
		ListJams.listJams,
		ShowInProgressJam.showInProgressJam,
		JamResourceDeleted.jamResourceDeleted)
	
	setUp(
		createJamResourceScenario.inject(rampUsers(2000) during (100 seconds)),
		editJamResourceScenario.inject(rampUsers(2000) during (100 seconds)),
		deleteJamResourceScenario.inject(rampUsers(2000) during (100 seconds))
		).protocols(httpProtocol)
		.assertions(
		global.responseTime.max.lt(5000),
			global.responseTime.mean.lt(1000),
			global.successfulRequests.percent.gt(95)
		)
}