package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU03 extends Simulation {

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

	object Home {
		val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(4)
	}

	object ListJams {
		val listJams = exec(http("ListJams")
			.get("/jams")
			.headers(headers_0))
		.pause(8)
	}

	object ShowJamResources {
		val showJamResources = exec(http("ShowJamResources")
			.get("/jams/3")
			.headers(headers_0))
		.pause(24)
	}

	val showJamResources = scenario("ShowJamResources")
		.exec(Home.home, ListJams.listJams, ShowJamResources.showJamResources)

	setUp(
		showJamResources.inject(rampUsers(10000) during (100 seconds))
		).protocols(httpProtocol)
		.assertions(
			global.responseTime.max.lt(5000),    
			global.responseTime.mean.lt(1000),
			global.successfulRequests.percent.gt(95)
		)
}