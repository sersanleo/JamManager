package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU01 extends Simulation {

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

	object Home {
		val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(4)
	}

	object Login {
		val login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
        	.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(12)
		.exec(http("LoggedAsJamOrganizator")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "jamOrganizator1")
			.formParam("password", "jamOrganizator1")
        	.formParam("_csrf", "${stoken}")
		).pause(16)
	}

	object ListJams {
		val listJams = exec(http("ListJams")
			.get("/jams")
			.headers(headers_0))
		.pause(7)
	}

	object CreateJam {
		val createJam = exec(http("CreateJamForm")
			.get("/jams/new")
			.headers(headers_0)
        	.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(79)
		.exec(http("CreateJam")
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
        	.formParam("_csrf", "${stoken}")
		).pause(77)
	}

	object ShowJamToEdit {
		var showJamToEdit = exec(http("ShowJamToEdit")
			.get("/jams/1")
			.headers(headers_0))
		.pause(4)
	}

	object EditJam {
		val editJam = exec(http("EditJamForm")
			.get("/jams/1/edit")
			.headers(headers_0)
        	.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(5)
		.exec(http("EditJam")
			.post("/jams/1/edit")
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
        	.formParam("_csrf", "${stoken}")
		).pause(18)
	}

	object ShowJamToDelete {
		var showJamToDelete = exec(http("ShowJamToDelete")
			.get("/jams/10")
			.headers(headers_0))
		.pause(4)
	}

	object DeleteJam {
		val deleteJam = exec(http("DeleteJam")
			.get("/jams/10/delete")
			.headers(headers_0))
		.pause(6)
	}

	val createJamScenario = scenario("CreateJam").exec(
		Home.home,
		Login.login,
		ListJams.listJams,
		CreateJam.createJam)

	val editJamScenario = scenario("EditJam").exec(
		Home.home,
		Login.login,
		ListJams.listJams,
		ShowJamToEdit.showJamToEdit,
		EditJam.editJam)

	val deleteJamScenario = scenario("DeleteJam").exec(
		Home.home,
		Login.login,
		ListJams.listJams,
		ShowJamToDelete.showJamToDelete,
		DeleteJam.deleteJam) // solo se usarán los dos primeros, que son los más significativos

	setUp(
		createJamScenario.inject(rampUsers(1800) during (100 seconds)),
		editJamScenario.inject(rampUsers(1800) during (100 seconds))
		).protocols(httpProtocol)
		.assertions(
			global.responseTime.max.lt(5000),    
			global.responseTime.mean.lt(1000),
			global.successfulRequests.percent.gt(95)
		)
}