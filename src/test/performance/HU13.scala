package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU13 extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.png""", """.*.ico""", """.*.js"""), WhiteList())
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.9")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36")

	val headers_0 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Proxy-Connection" -> "keep-alive")

	val headers_3 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Origin" -> "http://www.dp2.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_8 = Map(
		"Pragma" -> "no-cache",
		"Proxy-Connection" -> "keep-alive")

	val headers_15 = Map("Proxy-Connection" -> "keep-alive")

    val uri2 = "http://www.gstatic.com/generate_204"


	object Home{
		val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(12)
	}

	object LoginAsMember{
		val loginAsMember = exec(http("LoginAsMember")
			.get("/login")
			.headers(headers_0)
			.resources(http("request_2")
			.get("/login")
			.headers(headers_2)))
		.pause(18)
	}

	object LoggedAsMember{
		val loggedAsMember = exec(http("LoggedAsMember")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "member1")
			.formParam("password", "member1")
			.formParam("_csrf", "2bc019ec-6ace-4e0f-a5dc-9c85a80bfffc"))
		.pause(11)
	}

	object ListJams{
		val listJams = exec(http("ListJams")
			.get("/jams")
			.headers(headers_0))
		.pause(16)
	}

	object ShowInProgressJam{
		val showInProgressJam = exec(http("ShowInProgressJam")
			.get("/jams/3")
			.headers(headers_0))
		.pause(20)
	}

	object ShowRatingJam{
		val showRatingJam = exec(http("ShowRatingJam")
			.get("/jams/4")
			.headers(headers_0))
		.pause(20)
	}

	object ShowTeamDetails{
		val showTeamDetails = exec(http("ShowTeamDetails")
			.get("/jams/3/teams/4")
			.headers(headers_0))
		.pause(80)
	}

	object ShowRatingTeamDetails{
		val showRatingTeamDetails = exec(http("ShowRatingTeamDetails")
			.get("/jams/4/teams/6")
			.headers(headers_0))
		.pause(80)
	}

	object NewDeliveryForm{
		val newDeliveryForm = exec(http("NewDeliveryForm")
			.get("/jams/3/teams/4/deliveries/new")
			.headers(headers_0))
		.pause(34)
	}

	object DeliveryCreated{
		val deliveryCreated = exec(http("DeliveryCreated")
			.post("/jams/3/teams/4/deliveries/new")
			.headers(headers_3)
			.formParam("downloadURL", "https://maps.google.com/")
			.formParam("description", "Una gran descripcion")
			.formParam("_csrf", "b956e63b-8fc4-484f-882d-a41dcac42860"))
		.pause(19)
	}

	object DeliveryDeleted{
		val deliveryDeleted = exec(http("DeliveryDeleted")
			.get("/jams/3/teams/4/deliveries/2/delete")
			.headers(headers_0))
		.pause(130)
	}

	object ErrorNotInProgressJam{
		val errorNotInProgressJam = exec(http("ErrorNotInProgressJam")
			.get("/jams/4/teams/6/deliveries/3/delete")
			.headers(headers_0))
		.pause(40)
	}

	val createDeliveryScenario = scenario("CreateDelivery").exec(
		Home.home, 
		LoginAsMember.loginAsMember,
		LoggedAsMember.loggedAsMember,
		ListJams.listJams,
		ShowInProgressJam.showInProgressJam,
		ShowTeamDetails.showTeamDetails,
		NewDeliveryForm.newDeliveryForm,
		DeliveryCreated.deliveryCreated)

	val deleteDeliveryScenario = scenario("DeleteDelivery").exec(
		Home.home, 
		LoginAsMember.loginAsMember,
		LoggedAsMember.loggedAsMember,
		ListJams.listJams,
		ShowInProgressJam.showInProgressJam,
		ShowTeamDetails.showTeamDetails,
		DeliveryDeleted.deliveryDeleted)

	val errorNotInProgressJamScenario = scenario("ErrorNotInProgressJam").exec(
		Home.home, 
		LoginAsMember.loginAsMember,
		LoggedAsMember.loggedAsMember,
		ListJams.listJams,
		ShowRatingJam.showRatingJam,
		ShowRatingTeamDetails.showRatingTeamDetails,
		ErrorNotInProgressJam.errorNotInProgressJam
	)

	setUp(
		createDeliveryScenario.inject(rampUsers(500) during (100 seconds)),
		deleteDeliveryScenario.inject(rampUsers(500) during (100 seconds)),
		errorNotInProgressJamScenario.inject(rampUsers(500) during (100 seconds))
		).protocols(httpProtocol)
		.assertions(
		global.responseTime.max.lt(5000),
			global.responseTime.mean.lt(1000),
			global.successfulRequests.percent.gt(95)
		)
}