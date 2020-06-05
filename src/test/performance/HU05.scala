package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU05GestionarTeams extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources()
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

	val headers_12 = Map(
		"Proxy-Connection" -> "Keep-Alive",
		"User-Agent" -> "Microsoft-WNS/10.0")

    val uri2 = "http://tile-service.weather.microsoft.com/es-ES/livetile/preinstall"

	object Home {
		val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(11)
	}

	object Login1 {
		val login1 = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.resources(http("request_2")
			.get("/favicon.ico")
			.headers(headers_2)))
		.pause(17)
		.exec(http("LogedMember1")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "member1")
			.formParam("password", "member1")
			.formParam("_csrf", "0abe2e05-1e2b-4172-b629-d5c8860d9648"))
		.pause(15)
	}


		object Login2 {
		val login2 = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.resources(http("request_2")
			.get("/favicon.ico")
			.headers(headers_2)))
		.pause(17)
		.exec(http("LogedMember2")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "member2")
			.formParam("password", "member2")
			.formParam("_csrf", "b6125be6-e601-495b-94a0-fb507d997653"))
		.pause(13)
	}


	object ShowJams {
		val showJams = exec(http("ShowJams")
			.get("/jams")
			.headers(headers_0))
		.pause(13)
	}

	object ShowJam {
		val showJam = exec(http("ShowJam")
			.get("/jams/1")
			.headers(headers_0))
		.pause(30)
	}

	object CrearTeam {
		val crearTeam = exec(http("CrearTeam")
			.get("/jams/1/teams/new")
			.headers(headers_0))
		.pause(29)
	}

	object TeamCreado {
		val teamCreado = exec(http("TeamCreado")
			.post("/jams/1/teams/new")
			.headers(headers_3)
			.formParam("name", "mi grupo")
			.formParam("_csrf", "51556c76-0eba-4731-a5b3-395f157f6057"))
		.pause(43)
	}

	object ShowGrupoMember2 {
		val showGrupoMember2 = exec(http("ShowGrupoMember2")
			.get("/jams/1/teams/1")
			.headers(headers_0))
		.pause(33)
	}

	object ShowGrupoMember3 {
		val showGrupoMember3 = exec(http("ShowGrupoMember3")
			.get("/jams/1/teams/12")
			.headers(headers_0))
		.pause(28)
	}

	object GrupoMember2Borrado {
		val grupoMember2Borrado = exec(http("GrupoMember2Borrado")
			.get("/jams/1/teams/1/members/member2/delete")
			.headers(headers_0))
		.pause(34)
	}

	object EditGrupoMember3 {
		val editGrupoMember3 = exec(http("EditGrupoMember3")
			.get("/jams/1/teams/12/edit")
			.headers(headers_0))
		.pause(34)
	}

	object EditedGrupoMember3 {
		val editedGrupoMember3 = exec(http("EditedGrupoMember3")
			.post("/jams/1/teams/12/edit")
			.headers(headers_3)
			.formParam("name", "Grupo 2 hola")
			.formParam("_csrf", "58ec37c5-25ca-4a2d-963c-eba34f247613"))
		.pause(17)
	}

	val createTeamScn = scenario("CreateJam").exec(
		Home.home,
		Login1.login1,
		ShowJams.showJams,
		ShowJam.showJam,
		CrearTeam.crearTeam,
		TeamCreado.teamCreado)



	val deleteTeamScn = scenario("DeleteJam").exec(
		Home.home,
		Login2.login2,
		ShowJams.showJams,
		ShowJam.showJam,
		ShowGrupoMember2.showGrupoMember2,
		GrupoMember2Borrado.grupoMember2Borrado)
	

	setUp(
		createTeamScn.inject(rampUsers(700) during (100 seconds)),
		deleteTeamScn.inject(rampUsers(700) during (100 seconds))
		).protocols(httpProtocol)
		.assertions(
			global.responseTime.max.lt(5000),    
			global.responseTime.mean.lt(1000),
			global.successfulRequests.percent.gt(95)
		)

}