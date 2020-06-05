package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU20 extends Simulation {

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

	val headers_5 = Map(
		"Accept" -> "*/*",
		"Proxy-Connection" -> "keep-alive")


	object Home {
		val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(20)
	}

	object Login1 {
		val login1 = exec(http("Login1")
			.get("/login")
			.headers(headers_0)
        	.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(20)
		.exec(http("JamOrganizatorLogged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "jamOrganizator1")
			.formParam("password", "jamOrganizator1")
        	.formParam("_csrf", "${stoken}")
		).pause(33)
	}
		
	object Login2 {
		val login2 = exec(http("Login2")
			.get("/login")
			.headers(headers_0)
        	.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(20)
		.exec(http("JudgeLogged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "judge1")
			.formParam("password", "judge1")
        	.formParam("_csrf", "${stoken}")
		).pause(33)
	}

	object ShowDashboard {
		val showDashboard = exec(http("ShowDashboard")
			.get("/dashboard")
			.headers(headers_0))
		.pause(16)
	}

	val jamOrgScn = scenario("JamOrg").exec(
		Home.home,
		Login1.login1,
		ShowDashboard.showDashboard)
	

	val judgeScn = scenario("Judge").exec(
		Home.home,
		Login2.login2,
		ShowDashboard.showDashboard)



	setUp(
		jamOrgScn.inject(rampUsers(1000) during (100 seconds)),
		judgeScn.inject(rampUsers(1000) during (100 seconds))
		).protocols(httpProtocol)
		.assertions(
			global.responseTime.max.lt(5000),    
			global.responseTime.mean.lt(1000),
			global.successfulRequests.percent.gt(95)
		)
}