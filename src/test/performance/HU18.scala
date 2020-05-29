package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU18 extends Simulation {

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
			.formParam("_csrf", "50a711f6-695f-4a51-8f54-4d7e4da82673"))
		.pause(7)
	}

	object ListJams {
		val listJams = exec(http("ListJams")
			.get("/jams")
			.headers(headers_0))
		.pause(5)
	}

	object ShowJam1 {
		var showJam1 = exec(http("ShowJam1")
			.get("/jams/8")
			.headers(headers_0))
		.pause(7)
	}

	object PublishJam1ResultsForm {
		var publishJam1ResultsForm = exec(http("PublishJam1ResultsForm")
			.get("/jams/8/publish")
			.headers(headers_0))
		.pause(6)
	}

	object PublishJam1Results {
		var publishJam1Results = exec(http("PublishJam1Results")
			.post("/jams/8/publish")
			.headers(headers_3)
			.formParam("winner.id", "14")
			.formParam("_csrf", "048279cb-cece-4df3-a187-c86e6de00f2a"))
		.pause(8)
	}

	object ShowJam2Jam {
		var showUnpublishableJam = exec(http("ShowJam2Jam")
			.get("/jams/8")
			.headers(headers_0))
		.pause(7)
	}

	object PublishJam2ResultsForm {
		var publishUnpublishableResultsForm = exec(http("PublishJam2ResultsForm")
			.get("/jams/9/publish")
			.headers(headers_0))
		.pause(6)
	}

	object PublishJam2Results {
		var publishUnpublishableResults = exec(http("PublishJam2Results")
			.post("/jams/9/publish")
			.headers(headers_3)
			.formParam("winner.id", "15")
			.formParam("_csrf", "048279cb-cece-4df3-a187-c86e6de00f2a"))
		.pause(8)
	}

	val publishResultsNotEveryTeamHas1MarkAtLeast = scenario("PublishResultsNotEveryTeamHas1MarkAtLeast").exec(
		Home.home, Login.login, LoggedAsJudge.loggedAsJudge, ListJams.listJams, ShowJam1.showJam1, PublishJam1ResultsForm.publishJam1ResultsForm, PublishJam1Results.publishJam1Results
	)

	val publishResultsEveryTeamNeedsSameAmountOfMarks = scenario("PublishResultsEveryTeamNeedsSameAmountOfMarks").exec(
		Home.home, Login.login, LoggedAsJudge.loggedAsJudge, ListJams.listJams, ShowJam1.showJam1, PublishJam1ResultsForm.publishJam1ResultsForm, PublishJam1Results.publishJam1Results
	)

	setUp(
		publishResultsNotEveryTeamHas1MarkAtLeast.inject(atOnceUsers(1)),
		publishResultsEveryTeamNeedsSameAmountOfMarks.inject(atOnceUsers(1)),
		).protocols(httpProtocol)
}